package pl.krakow.uek.centrumWolontariatu.service;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.converter.VolunteerRequestConverter;
import pl.krakow.uek.centrumWolontariatu.domain.*;
import pl.krakow.uek.centrumWolontariatu.repository.*;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.repository.solr.VolunteerRequestSearchDao;
import pl.krakow.uek.centrumWolontariatu.util.rsql.CustomRsqlVisitor;
import pl.krakow.uek.centrumWolontariatu.util.rsql.GenericRsqlSpecification;
import pl.krakow.uek.centrumWolontariatu.web.rest.AuthenticationController;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.VolunteerRequestVM;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;


import static pl.krakow.uek.centrumWolontariatu.configuration.constant.UserConstant.UPLOADED_FOLDER_REQUESTS;
import static pl.krakow.uek.centrumWolontariatu.web.rest.util.ParserRSQLUtil.parse;

@Service
public class VolunteerRequestService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;
    private final MailService mailService;
    private final VolunteerRequestRepository volunteerRequestRepository;
    private final VolunteerAdRepository volunteerAdRepository;
    private final VolunteerRequestPictureRepository volunteerRequestPictureRepository;
    private final VolunteerRequestCategoryRepository volunteerRequestCategoryRepository;
    private final VolunteerAdCategoryRepository volunteerAdCategoryRepository;
    private final VolunteerRequestTypeRepository volunteerRequestTypeRepository;
    private final VolunteerRequestSearchDao volunteerRequestSearchDao;
    private final PictureService<VolunteerRequest> pictureService;

    public VolunteerRequestService(UserService userService, MailService mailService, VolunteerRequestRepository volunteerRequestRepository, VolunteerAdRepository volunteerAdRepository, VolunteerRequestPictureRepository volunteerRequestPictureRepository, VolunteerRequestCategoryRepository volunteerRequestCategoryRepository, VolunteerAdCategoryRepository volunteerAdCategoryRepository, VolunteerRequestTypeRepository volunteerRequestTypeRepository, VolunteerRequestSearchDao volunteerRequestSearchDao, PictureService<VolunteerRequest> pictureService) {
        this.userService = userService;
        this.mailService = mailService;
        this.volunteerRequestRepository = volunteerRequestRepository;
        this.volunteerAdRepository = volunteerAdRepository;
        this.volunteerRequestPictureRepository = volunteerRequestPictureRepository;
        this.volunteerRequestCategoryRepository = volunteerRequestCategoryRepository;
        this.volunteerAdCategoryRepository = volunteerAdCategoryRepository;
        this.volunteerRequestTypeRepository = volunteerRequestTypeRepository;
        this.volunteerRequestSearchDao = volunteerRequestSearchDao;
        this.pictureService = pictureService;
    }


    /*
     * don't refactor!!,
     * split creating process into 2 methods:GenerateVolunteerRequest + createVolunteerRequest
     * to provide integrity between tables in database: cw_volunteer_request_category, cw_volunteer_request_type and the table
     * cw_volunteer_request. Without spliting into 2 methods, it was allowed to insert to database not permitted values in type/category fields.
     */

    public long generateVolunteerRequest() {
        VolunteerRequest volunteerRequest = new VolunteerRequest();
        User user = userService.getUserWithAuthorities().get();
        volunteerRequest.setUser(user);

        volunteerRequestRepository.save(volunteerRequest);
        return volunteerRequest.getId();
    }

    @CacheEvict(value = "volunteerRequestsByRsql", allEntries = true)
    public VolunteerRequest createVolunteerRequest(VolunteerRequestVM volunteerRequestVM) {
        long id = generateVolunteerRequest();
        try {
            return volunteerRequestRepository.findById(id)
                .map(volunteerRequest -> {
                    ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
                    long epochMillis = utc.toEpochSecond() * 1000;
                    volunteerRequest.setTimestamp(epochMillis);
                    volunteerRequest.setExpirationDate(volunteerRequestVM.getExpirationDate());
                    volunteerRequest.setDescription(volunteerRequestVM.getDescription());
                    volunteerRequest.setTitle(volunteerRequestVM.getTitle());
                    volunteerRequest.setVolunteersAmount(volunteerRequestVM.getVolunteersAmount());
                    volunteerRequest.setIsForTutors(parse(volunteerRequestVM.isForTutors()));
                    volunteerRequest.setIsForStudents(parse(volunteerRequestVM.isForStudents()));

                    User user = userService.getUserWithAuthorities().get();

                    if (volunteerRequestVM.getCategories() != null) { volunteerRequest.setCategories(getCategoriesFromRequest(volunteerRequestVM.getCategories())); }
                    if (volunteerRequestVM.getTypes() != null) { volunteerRequest.setVolunteerRequestTypes(getTypesFromRequest(volunteerRequestVM.getTypes()));}

                    if (volunteerRequestVM.getImages() != null){ volunteerRequest.setPictures((Set<VolunteerRequestPicture>) pictureService.addPicturesToDatabase(volunteerRequestVM.getImages(), volunteerRequest));}


                    volunteerRequestRepository.save(volunteerRequest);
                    log.debug("User id={} created new volunteer request id={}", user.getId(), volunteerRequest.getId());

                    return volunteerRequest;
                }).get();
        } catch (JpaObjectRetrievalFailureException e) {
            volunteerRequestRepository.deleteById(id);
            throw new BadRequestAlertException("Unable to find category/type of volunteerRequest in database", "volunteerRequestManagement", "nocategoryortypelinkstodatabase");
        }
    }


    public Set<String> addPicturesToVolunteerRequest(MultipartFile[] file) {
        for (MultipartFile multipartFile : file) {
            if (!multipartFile.getContentType().matches("^(image).*$")) {
                throw new BadRequestAlertException("Not allowed format file", "volunteerManagement", "notallowedformatfile");
            }
        }
        User user = userService.getUserWithAuthorities().get();
        HashSet<String> hashPicturesWithReferences = new HashSet<>();
        for (MultipartFile multipartFile : file) {
            try {
                byte[] bytes = multipartFile.getBytes();

                String fileTypes[] = multipartFile.getOriginalFilename().split("\\.");
                String fileType = fileTypes[fileTypes.length - 1];

                MessageDigest salt = MessageDigest.getInstance("SHA-256");
                salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
                String hexString = DatatypeConverter.printHexBinary(salt.digest());

                Path path = Paths.get(UPLOADED_FOLDER_REQUESTS + hexString + "." + fileType);
                Files.write(path, bytes);
                log.debug("User id={} uploaded picture: {}", user.getId(), hexString + "." + fileType);

                hashPicturesWithReferences.add(hexString + "." + fileType);

                createThumbnailFromPicture(bytes, hexString, fileType);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return hashPicturesWithReferences;
    }

    private void createThumbnailFromPicture(byte[] bytes, String hexString, String fileType) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Path pathThumbnail = Paths.get(UPLOADED_FOLDER_REQUESTS + hexString + "_thumbnail." + fileType);
        try {
            BufferedImage img = Thumbnails.of(bis)
                .size(400, 400)
                .asBufferedImage();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, fileType, baos);
            Files.write(pathThumbnail, baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @CacheEvict(value = "categories", allEntries = true)
    public void createVolunteerRequestCategory(String name) {
        if(volunteerRequestCategoryRepository.findById(name).isPresent()){
            throw new BadRequestAlertException("category already exist in database", "categoryManagement", "categoryexistindatabase");
        }
        VolunteerRequestCategory volunteerRequestCategory = new VolunteerRequestCategory();
        volunteerRequestCategory.setName(name);
        volunteerRequestCategoryRepository.save(volunteerRequestCategory);

        VolunteerAdCategory volunteerAdCategory = new VolunteerAdCategory();
        volunteerAdCategory.setName(name);
        volunteerAdCategoryRepository.save(volunteerAdCategory);
    }

    @Cacheable(value = "categories")
    public List<VolunteerRequestCategory> getAllCategories() {
        return volunteerRequestCategoryRepository.findAll();
    }

    public void createVolunteerRequestType(String name) {
        if(volunteerRequestTypeRepository.findById(name).isPresent()){
            throw new BadRequestAlertException("type already exist in database", "categoryManagement", "typeexistindatabase");
        }
        VolunteerRequestType volunteerRequestType = new VolunteerRequestType();
        volunteerRequestType.setName(name);
        volunteerRequestTypeRepository.save(volunteerRequestType);
    }

    public List<VolunteerRequestType> getAllTypes() {
        return volunteerRequestTypeRepository.findAll();
    }

    private Set<VolunteerRequestCategory> getCategoriesFromRequest(Set<String> categories) {
        HashSet<VolunteerRequestCategory> volunteerRequestCategories = new HashSet<>();
        for (String string : categories) {
            volunteerRequestCategories.add(new VolunteerRequestCategory(string));
        }
        return volunteerRequestCategories;
    }

    private Set<VolunteerRequestType> getTypesFromRequest(Set<String> types) {
        HashSet<VolunteerRequestType> volunteerRequestTypes = new HashSet<>();
        for (String string : types) {
            volunteerRequestTypes.add(new VolunteerRequestType(string));
        }
        return volunteerRequestTypes;
    }

    @Cacheable(value = "volunteerRequestsByRsql")
    @Transactional
    public Page<VolunteerRequestDTO> findAllByRsql(Pageable pageable, com.google.common.base.Optional<String> search) {
        if(search.isPresent()) {
            final Node rootNode = new RSQLParser().parse(search.get());
            Specification<VolunteerRequest> spec = rootNode.accept(new CustomRsqlVisitor<VolunteerRequest>());

            Page<VolunteerRequest> volunteerRequests = volunteerRequestRepository.findAll(spec, pageable);
            return VolunteerRequestConverter.mapEntityPageIntoDTOPage(pageable, volunteerRequests);
        } else return volunteerRequestRepository.findAllBy(pageable);
    }

    @Cacheable(value = "volunteerRequestsWithCategories")
    @Transactional
    public Page<VolunteerRequestDTO> findAllByRsqlWithCategories(Pageable pageable, String[] categories, boolean isForStudents, boolean isForTutors) {
        Set<VolunteerRequestCategory> categorySet = new HashSet<>();
        if(categories!=null) {
            for (String cat : categories) {
                categorySet.add(new VolunteerRequestCategory(cat));
            }
            if(isForStudents == true && isForTutors == true){
                return VolunteerRequestConverter.mapEntityPageIntoDTOPage(pageable, volunteerRequestRepository.findDistinctByAcceptedIsAndExpiredIsAndVolunteersAmountGreaterThanAndCategoriesIn(pageable, (byte) 1, (byte) 0 , 0, categorySet));
            }
            if(isForStudents == true && isForTutors == false){
                return VolunteerRequestConverter.mapEntityPageIntoDTOPage(pageable, volunteerRequestRepository.findDistinctByAcceptedIsAndExpiredIsAndIsForStudentsIsAndVolunteersAmountGreaterThanAndCategoriesIn(pageable, (byte) 1, (byte) 0 , parse(true), 0, categorySet));
            }
            if(isForTutors == true && isForStudents == false){
                return VolunteerRequestConverter.mapEntityPageIntoDTOPage(pageable, volunteerRequestRepository.findDistinctByAcceptedIsAndExpiredIsAndIsForTutorsIsAndVolunteersAmountGreaterThanAndCategoriesIn(pageable, (byte) 1, (byte) 0 , parse(true), 0, categorySet));
            }
                return VolunteerRequestConverter.mapEntityPageIntoDTOPage(pageable, volunteerRequestRepository.findDistinctByAcceptedIsAndExpiredIsAndVolunteersAmountGreaterThanAndCategoriesIn(pageable, (byte) 1, (byte) 0 , 0, categorySet));
        } else {
            if(isForStudents == true && isForTutors == true){
                return VolunteerRequestConverter.mapEntityPageIntoDTOPage(pageable, volunteerRequestRepository.findAllByAcceptedIsAndExpiredIsAndVolunteersAmountGreaterThan((byte)1, (byte) 0, 0, pageable));
            }
            if(isForStudents == true && isForTutors == false){
                return VolunteerRequestConverter.mapEntityPageIntoDTOPage(pageable, volunteerRequestRepository.findAllByAcceptedIsAndExpiredIsAndIsForStudentsIsAndVolunteersAmountGreaterThan(pageable, (byte) 1, (byte) 0 , parse(true), 0));
            }
            if(isForTutors == true && isForStudents == false){
                return VolunteerRequestConverter.mapEntityPageIntoDTOPage(pageable, volunteerRequestRepository.findAllByAcceptedIsAndExpiredIsAndIsForTutorsIsAndVolunteersAmountGreaterThan(pageable, (byte) 1, (byte) 0 , parse(true), 0));
            }
            return VolunteerRequestConverter.mapEntityPageIntoDTOPage(pageable, volunteerRequestRepository.findAllByAcceptedIsAndExpiredIsAndVolunteersAmountGreaterThan((byte)1, (byte) 0, 0, pageable));

        }

    }


    @Transactional
    public Page<VolunteerRequestDTO> findAllByUserId(Pageable pageable) {
        Long id = userService.getUserWithAuthorities().get().getId();
        return volunteerRequestRepository.findAllByUserIdAndExpiredIsAndVolunteersAmountGreaterThan(pageable, id, parse(false), 0);
    }


    @Transactional
    public List<Long> findAllMineIdsByUserId(Pageable pageable) {
        Long id = userService.getUserWithAuthorities().get().getId();

        List<Long> idList = new ArrayList<>();
        for(VolunteerRequestDTO volunteerRequestDTO: volunteerRequestRepository.findAllByUserId(pageable, id)){
            idList.add(volunteerRequestDTO.getId());
        }
        return idList;
    }

    public void deleteType(String name){
        volunteerRequestTypeRepository.deleteById(name);
    }

    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(String name){
        volunteerRequestCategoryRepository.deleteById(name);
    }

    @CacheEvict(value = {"volunteerRequestsByRsql", "volunteerRequestsWithCategories"}, allEntries = true)
    public void acceptVolunteerRequest(long id){
        volunteerRequestRepository.findById(id).ifPresent(volunteerRequest -> {
            volunteerRequest.setAccepted(parse(true));
            volunteerRequestRepository.save(volunteerRequest);
        });
    }

    @CacheEvict(value = {"volunteerRequestsByRsql", "volunteerRequestsWithCategories"}, allEntries = true)
    public void setExpired(long id){
        volunteerRequestRepository.findById(id).ifPresent(volunteerRequest -> {
            volunteerRequest.setExpired(parse(true));
            volunteerRequestRepository.save(volunteerRequest);
        });
    }

    @CacheEvict(value = {"volunteerRequestsByRsql", "volunteerRequestsWithCategories"}, allEntries = true)
    public void deleteVolunteerRequest(long id){ volunteerRequestRepository.deleteById(id);}

    public List<VolunteerRequestDTO> getVolunteerRequestBySolr(String text){
        if(text.length()>=3)
            return volunteerRequestSearchDao.searchVolunteerRequestByQuery(text);
        return null;
    }

    public Page<VolunteerRequestDTO> getVolunteerRequestBySolrPage(Pageable pageable, String text){
        List<Long> id = new ArrayList<>();
        if(text.length()>=3) {
            List<VolunteerRequestDTO> volunteerRequestDTOSBySolr = volunteerRequestSearchDao.searchVolunteerRequestByQuery(text);
            long start = pageable.getOffset();
            long end = (start + pageable.getPageSize()) > volunteerRequestDTOSBySolr.size() ? volunteerRequestDTOSBySolr.size() : (start + pageable.getPageSize());

            return new PageImpl<>(volunteerRequestDTOSBySolr.subList((int)start, (int)end), pageable, volunteerRequestDTOSBySolr.size());

        }
        return null;
    }

    @Scheduled(fixedRate = 60000, initialDelay = 20000)
    public void testForExpired() {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        long epochMillis = utc.toEpochSecond() * 1000;
        System.out.println("XXX executing task scheduled" + epochMillis);
        volunteerRequestRepository.findAllByExpiredIs((byte)0).forEach(volunteerRequest -> {
            if(volunteerRequest.getExpirationDate()<epochMillis){
                volunteerRequest.setExpired((byte)1);
                volunteerRequestRepository.save(volunteerRequest);
            }
        });

    }


}
