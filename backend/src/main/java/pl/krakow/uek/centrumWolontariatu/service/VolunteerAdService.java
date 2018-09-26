package pl.krakow.uek.centrumWolontariatu.service;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.converter.VolunteerAdConverter;
import pl.krakow.uek.centrumWolontariatu.converter.VolunteerRequestConverter;
import pl.krakow.uek.centrumWolontariatu.domain.*;
import pl.krakow.uek.centrumWolontariatu.repository.*;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerAdDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.repository.solr.VolunteerAdSearchDao;
import pl.krakow.uek.centrumWolontariatu.util.rsql.CustomRsqlVisitor;
import pl.krakow.uek.centrumWolontariatu.web.rest.AuthenticationController;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.VolunteerAdVM;

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

import static pl.krakow.uek.centrumWolontariatu.configuration.constant.UserConstant.UPLOADED_FOLDER_AD;
import static pl.krakow.uek.centrumWolontariatu.configuration.constant.UserConstant.UPLOADED_FOLDER_REQUESTS;
import static pl.krakow.uek.centrumWolontariatu.web.rest.util.ParserRSQLUtil.parse;

@Service
public class VolunteerAdService {
    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;
    private final MailService mailService;
    private final VolunteerAdRepository volunteerAdRepository;
    private final VolunteerAdPictureRepository volunteerAdPictureRepository;
    private final VolunteerAdCategoryRepository volunteerAdCategoryRepository;
    private final VolunteerAdTypeRepository volunteerAdTypeRepository;
    private final VolunteerAdSearchDao volunteerAdSearchDao;
    private final PictureService<VolunteerAd> pictureService;

    public VolunteerAdService(UserService userService, MailService mailService, VolunteerAdRepository volunteerAdRepository, VolunteerAdPictureRepository volunteerAdPictureRepository, VolunteerAdCategoryRepository volunteerAdCategoryRepository, VolunteerAdTypeRepository volunteerAdTypeRepository, VolunteerAdSearchDao volunteerAdSearchDao, PictureService<VolunteerAd> pictureService) {
        this.userService = userService;
        this.mailService = mailService;
        this.volunteerAdRepository = volunteerAdRepository;
        this.volunteerAdPictureRepository = volunteerAdPictureRepository;
        this.volunteerAdCategoryRepository = volunteerAdCategoryRepository;
        this.volunteerAdTypeRepository = volunteerAdTypeRepository;
        this.volunteerAdSearchDao = volunteerAdSearchDao;
        this.pictureService = pictureService;
    }

    public long GenerateVolunteerAd() {
        VolunteerAd volunteerAd = new VolunteerAd();
        User user = userService.getUserWithAuthorities().get();
        volunteerAd.setUser(user);

        volunteerAdRepository.save(volunteerAd);
        return volunteerAd.getId();
    }

    public VolunteerAd createVolunteerAd(VolunteerAdVM volunteerAdVm) {
        long id = GenerateVolunteerAd();
        try {
            return volunteerAdRepository.findById(id)
                .map(volunteerAd -> {
                    ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
                    long epochMillis = utc.toEpochSecond() * 1000;
                    volunteerAd.setTimestamp(epochMillis);
                    volunteerAd.setExpirationDate(volunteerAdVm.getExpirationDate());
                    volunteerAd.setDescription(volunteerAdVm.getDescription());
                    volunteerAd.setTitle(volunteerAdVm.getTitle());

                    User user = userService.getUserWithAuthorities().get();

                    if (volunteerAdVm.getCategories() != null) { volunteerAd.setCategories(getCategoriesFromAd(volunteerAdVm.getCategories())); }
                    volunteerAd.setTypes(getTypesFromAd(volunteerAdVm.getTypes()));

                    volunteerAdRepository.save(volunteerAd);
                    log.debug("User id={} created new volunteer Ad id={}", user.getId(), volunteerAd.getId());

                    volunteerAd.setPictures((Set<VolunteerAdPicture>) pictureService.addPicturesToDatabase(volunteerAdVm.getImages(), volunteerAd));

                    return volunteerAd;
                }).get();
        } catch (JpaObjectRetrievalFailureException e) {
            volunteerAdRepository.deleteById(id);
            throw new BadRequestAlertException("Unable to find category/type of volunteerAd in database", "volunteerAdManagement", "nocategoryortypelinkstodatabase");
        }
    }

    public Set<String> addPicturesToVolunteerAd(MultipartFile[] file) {
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
        Path pathThumbnail = Paths.get(UPLOADED_FOLDER_AD + hexString + "_thumbnail." + fileType);
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createVolunteerAdCategory(String name) {
        if(volunteerAdCategoryRepository.findById(name).isPresent()){
            throw new BadRequestAlertException("category already exist in database", "categoryManagement", "categoryexistindatabase");
        }
        VolunteerAdCategory volunteerAdCategory = new VolunteerAdCategory();
        volunteerAdCategory.setName(name);
        volunteerAdCategoryRepository.save(volunteerAdCategory);
    }

    public List<VolunteerAdCategory> getAllCategories() {
        return volunteerAdCategoryRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createVolunteerAdType(String name) {
        if(volunteerAdTypeRepository.findById(name).isPresent()){
            throw new BadRequestAlertException("type already exist in database", "categoryManagement", "typeexistindatabase");
        }
        VolunteerAdType volunteerAdType = new VolunteerAdType();
        volunteerAdType.setName(name);
        volunteerAdTypeRepository.save(volunteerAdType);
    }

    public List<VolunteerAdType> getAllTypes() {
        return volunteerAdTypeRepository.findAll();
    }

    private Set<VolunteerAdCategory> getCategoriesFromAd(Set<String> categories) {
        HashSet<VolunteerAdCategory> volunteerAdCategories = new HashSet<>();
        for (String string : categories) {
            volunteerAdCategories.add(new VolunteerAdCategory(string));
        }
        return volunteerAdCategories;
    }

    private Set<VolunteerAdType> getTypesFromAd(Set<String> types) {
        HashSet<VolunteerAdType> volunteerAdTypes = new HashSet<>();
        if(types!=null)
            for (String string : types) {
                volunteerAdTypes.add(new VolunteerAdType(string));
            }
        return volunteerAdTypes;
    }

    @Cacheable(value = "volunteerAdsByRsql")
    @Transactional
    public Page<VolunteerAdDTO> findAllByRsql(Pageable pageable, com.google.common.base.Optional<String> search) {
        if(search.isPresent()) {
            final Node rootNode = new RSQLParser().parse(search.get());
            Specification<VolunteerAd> spec = rootNode.accept(new CustomRsqlVisitor<VolunteerAd>());

            Page<VolunteerAd> volunteerAds = volunteerAdRepository.findAll(spec, pageable);
            return VolunteerAdConverter.mapEntityPageIntoDTOPage(pageable, volunteerAds);
        } else return volunteerAdRepository.findAllBy(pageable);
    }

    @Cacheable(value = "volunteerAdsWithCategories")
    @Transactional
    public Page<VolunteerAdDTO> findAllWithCategoriesAndAuthorities(Pageable pageable, String[] categories, boolean isByStudent, boolean isByLecturer) {
        Set<Authority> authoritySet = new HashSet<>();
        Set<VolunteerAdCategory> categorySet = new HashSet<>();
        if(categories!=null) {
            for (String cat : categories) {
                categorySet.add(new VolunteerAdCategory(cat));
            }
            if(isByStudent == true && isByLecturer == false){
                authoritySet.add(new Authority("ROLE_USER"));
                return VolunteerAdConverter.mapEntityPageIntoDTOPage(pageable, volunteerAdRepository.findDistinctByAcceptedIsAndExpiredIsAndCategoriesInAndUserAuthoritiesIn(pageable, (byte) 1, (byte) 0, categorySet, authoritySet));
            }
            if(isByStudent == false && isByLecturer == true){
                authoritySet.add(new Authority("ROLE_LECTURER"));
                return VolunteerAdConverter.mapEntityPageIntoDTOPage(pageable, volunteerAdRepository.findDistinctByAcceptedIsAndExpiredIsAndCategoriesInAndUserAuthoritiesIn(pageable, (byte) 1, (byte) 0, categorySet, authoritySet));
            }
            return VolunteerAdConverter.mapEntityPageIntoDTOPage(pageable, volunteerAdRepository.findAllByAcceptedIsAndExpiredIsAndCategoriesIn(pageable, (byte) 1, (byte) 0, categorySet));

        } else {
            if(isByStudent == true && isByLecturer == false){
                authoritySet.add(new Authority("ROLE_USER"));
                return VolunteerAdConverter.mapEntityPageIntoDTOPage(pageable, volunteerAdRepository.findDistinctByAcceptedIsAndExpiredIsAndUserAuthoritiesIn(pageable, (byte) 1, (byte) 0, authoritySet));
            }
            if(isByStudent == false && isByLecturer == true){
                authoritySet.add(new Authority("ROLE_LECTURER"));
                return VolunteerAdConverter.mapEntityPageIntoDTOPage(pageable, volunteerAdRepository.findDistinctByAcceptedIsAndExpiredIsAndUserAuthoritiesIn(pageable, (byte) 1, (byte) 0, authoritySet));
            }
            return VolunteerAdConverter.mapEntityPageIntoDTOPage(pageable, volunteerAdRepository.findAllByAcceptedIsAndExpiredIs(pageable, (byte)1, (byte)0));
        }
    }

    @Transactional
    public Page<VolunteerAdDTO> findAllByUserId(Pageable pageable) {
         return volunteerAdRepository.findAllByUserId(pageable, userService.getUserWithAuthorities().get().getId());
    }

    @Transactional
    public List<Long> findAllMineIdsByUserId(Pageable pageable) {

        List<Long> idList = new ArrayList<>();
        for(VolunteerAdDTO volunteerAdDTO: volunteerAdRepository.findAllByUserId(pageable, userService.getUserWithAuthorities().get().getId())){
            idList.add(volunteerAdDTO.getId());
        }
        return idList;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteType(String name){
        volunteerAdTypeRepository.deleteById(name);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(String name){
        volunteerAdCategoryRepository.deleteById(name);
    }

    @CacheEvict(value = {"volunteerAdsByRsql", "volunteerAdsWithCategories"}, allEntries = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void acceptVolunteerAd(long id){
        volunteerAdRepository.findById(id).map(volunteerAd -> {
            volunteerAd.setAccepted(parse(true));
            return volunteerAdRepository.save(volunteerAd);
        }).orElseThrow(() -> new BadRequestAlertException("volunteer ad not found", "volunteerAdManagement", "volunteeradnotfound"));
    }

    @CacheEvict(value = {"volunteerAdsByRsql", "volunteerAdsWithCategories"}, allEntries = true)
    public void setExpired(long id){
        volunteerAdRepository.findById(id)
            .map(volunteerAd -> userService.getUserWithAuthorities()
                .filter(user -> user.getId() == volunteerAd.getUser().getId())
                .map(user -> {
                    volunteerAd.setExpired(parse(true));
                    return volunteerAdRepository.save(volunteerAd);
                }).orElseThrow(() -> new BadRequestAlertException("cannot set expired not your own volunteer ad", "volunteerAdManagement", "cannotsetexpirednotyourownvolunteerad")))
            .orElseThrow(() -> new BadRequestAlertException("volunteer ad not found", "volunteerAdManagement", "volunteeradnotfound"));
    }

    @CacheEvict(value = {"volunteerAdsByRsql", "volunteerAdsWithCategories"}, allEntries = true)
    public void deleteVolunteerAd(long id){
         volunteerAdRepository.findById(id).map(volunteerAd1 -> {
            return userService.getUserWithAuthorities().filter(user -> (user.getId() == volunteerAd1.getUser().getId()) || user.getAuthorities().stream().anyMatch(e -> e.getName().equals("ROLE_ADMIN"))).map(user -> {
                  volunteerAdRepository.deleteById(id);
                  return id;
             }).orElseThrow(() -> new BadRequestAlertException("cannot delete not your own volunteer ad","volunteerAdManagement", "cannotdeletenotyourownvolunteerad"));
        }).orElseThrow(() -> new BadRequestAlertException("volunteer ad not found", "volunteerAdManagement", "volunteeradnotfound"));
    }

    public List<VolunteerAdDTO> getVolunteerAdBySolr(String text){
        if(text.length()>=3)
            return volunteerAdSearchDao.searchVolunteerAdByQuery(text);
        return null;
    }

    public Page<VolunteerAdDTO> getVolunteerAdBySolrPage(Pageable pageable, String text){
        List<Long> id = new ArrayList<>();
        if(text.length()>=3) {
            List<VolunteerAdDTO> volunteerAdDTOSBySolr = volunteerAdSearchDao.searchVolunteerAdByQuery(text);
            long start = pageable.getOffset();
            long end = (start + pageable.getPageSize()) > volunteerAdDTOSBySolr.size() ? volunteerAdDTOSBySolr.size() : (start + pageable.getPageSize());

            return new PageImpl<>(volunteerAdDTOSBySolr.subList((int)start, (int)end), pageable, volunteerAdDTOSBySolr.size());

        }
        return null;
    }

    @Scheduled(fixedRate = 60000, initialDelay = 30000)
    public void testForExpired() {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        long epochMillis = utc.toEpochSecond() * 1000;
        volunteerAdRepository.findAllByExpiredIs(parse(false)).stream().filter(volunteerAd -> volunteerAd.getExpirationDate() < epochMillis).forEach(volunteerAd -> {
                volunteerAd.setExpired(parse(true));
                volunteerAdRepository.save(volunteerAd);
        });
    }
}
