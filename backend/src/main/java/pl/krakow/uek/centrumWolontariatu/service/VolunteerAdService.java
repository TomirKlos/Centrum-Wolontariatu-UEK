package pl.krakow.uek.centrumWolontariatu.service;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.converter.VolunteerAdConverter;
import pl.krakow.uek.centrumWolontariatu.domain.*;
import pl.krakow.uek.centrumWolontariatu.repository.*;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerAdDTO;
import pl.krakow.uek.centrumWolontariatu.repository.solr.VolunteerAdSearchDao;
import pl.krakow.uek.centrumWolontariatu.util.rsql.CustomRsqlVisitor;
import pl.krakow.uek.centrumWolontariatu.web.rest.AuthenticationController;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;

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
    @Autowired
    UserRepository userRepository;

    public VolunteerAdService(UserService userService, MailService mailService, VolunteerAdRepository volunteerAdRepository, VolunteerAdPictureRepository volunteerAdPictureRepository, VolunteerAdCategoryRepository volunteerAdCategoryRepository, VolunteerAdTypeRepository volunteerAdTypeRepository, VolunteerAdSearchDao volunteerAdSearchDao) {
        this.userService = userService;
        this.mailService = mailService;
        this.volunteerAdRepository = volunteerAdRepository;
        this.volunteerAdPictureRepository = volunteerAdPictureRepository;
        this.volunteerAdCategoryRepository = volunteerAdCategoryRepository;
        this.volunteerAdTypeRepository = volunteerAdTypeRepository;
        this.volunteerAdSearchDao = volunteerAdSearchDao;
    }

    public long GenerateVolunteerAd() {
        VolunteerAd volunteerAd = new VolunteerAd();
        User user = userService.getUserWithAuthorities().get();
        volunteerAd.setUser(user);

        volunteerAdRepository.save(volunteerAd);
        return volunteerAd.getId();
    }

    public VolunteerAd createVolunteerAd(String description, String title, Set<String> categories, Set<String> types, long expirationDate, MultipartFile[] file) {
        boolean isImageUploaded = file.length > 0;
        long id = GenerateVolunteerAd();
        try {
            return volunteerAdRepository.findById(id)
                .map(volunteerAd -> {
                    ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
                    Date date = Date.from(utc.toInstant());
                    long epochMillis = utc.toEpochSecond() * 1000;
                    volunteerAd.setTimestamp(epochMillis);
                    volunteerAd.setExpirationDate(expirationDate);
                    volunteerAd.setDescription(description);
                    volunteerAd.setTitle(title);

                    User user = userService.getUserWithAuthorities().get();

                    volunteerAd.setCategories(getCategoriesFromAd(categories));
                    volunteerAd.setTypes(getTypesFromAd(types));

                    volunteerAdRepository.save(volunteerAd);
                    log.debug("User id={} created new volunteer Ad id={}", user.getId(), volunteerAd.getId());

                    if (isImageUploaded) {
                        VolunteerAdPicture volunteerAdPicture = addPicturesToVolunteerAd(file, user, volunteerAd);
                        volunteerAdPictureRepository.save(volunteerAdPicture);
                    }
                    return volunteerAd;
                }).get();
        } catch (JpaObjectRetrievalFailureException e) {
            volunteerAdRepository.deleteById(id);
            throw new BadRequestAlertException("Unable to find category/type of volunteerAd in database", "volunteerAdManagement", "nocategoryortypelinkstodatabase");
        }
    }

    private VolunteerAdPicture addPicturesToVolunteerAd(MultipartFile[] file, User user, VolunteerAd volunteerAd) {
        for (MultipartFile multipartFile : file) {
            if (!multipartFile.getContentType().matches("^(image).*$")) {
                throw new BadRequestAlertException("Not allowed format file", "volunteerManagement", "notallowedformatfile");
            }
        }
        VolunteerAdPicture volunteerAdPicture = new VolunteerAdPicture();
        HashMap<String, String> hashPicturesWithReferences = new HashMap<>();
        for (MultipartFile multipartFile : file) {
            try {
                byte[] bytes = multipartFile.getBytes();

                String fileTypes[] = multipartFile.getOriginalFilename().split("\\.");
                String fileType = fileTypes[fileTypes.length - 1];

                MessageDigest salt = MessageDigest.getInstance("SHA-256");
                salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
                String hexString = DatatypeConverter.printHexBinary(salt.digest());

                Path path = Paths.get(UPLOADED_FOLDER_AD + hexString + "." + fileType);
                Files.write(path, bytes);
                log.debug("User id={} uploaded picture: {}", user.getId(), hexString + "." + fileType);

                hashPicturesWithReferences.put(hexString + "." + fileType, multipartFile.getOriginalFilename());

                createThumbnailFromPicture(bytes, hexString, fileType);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        volunteerAdPicture.setReferenceToPicture(hashPicturesWithReferences);
        volunteerAdPicture.setVolunteerAd(volunteerAd);
        return volunteerAdPicture;
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

    public HashMap<String, String> getImagesFromVolunteerAd(long volunteerAdId) {
        VolunteerAdPicture volunteerAdPicture;
        try {
            volunteerAdPicture =
                volunteerAdPictureRepository.findByVolunteerAdId(volunteerAdId)
                    .get();
        } catch (NoSuchElementException e) {
            throw new BadRequestAlertException("No image found for this volunteer request", "volunteerManagement", "noimagefoundforvolunteerid");

        }
        return volunteerAdPicture.getReferenceToPicture();
    }

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
        for (String string : types) {
            volunteerAdTypes.add(new VolunteerAdType(string));
        }
        return volunteerAdTypes;
    }

    @Transactional
    public Page<VolunteerAdDTO> findAllByRsql(Pageable pageable, Optional<String> search) {
        if(search.isPresent()) {
            final Node rootNode = new RSQLParser().parse(search.get());
            Specification<VolunteerAd> spec = rootNode.accept(new CustomRsqlVisitor<VolunteerAd>());

            Page<VolunteerAd> volunteerAds = volunteerAdRepository.findAll(spec, pageable);
            return VolunteerAdConverter.mapEntityPageIntoDTOPage(pageable, volunteerAds);
        } else return volunteerAdRepository.findAllBy(pageable);
    }

    public void deleteType(String name){
        volunteerAdTypeRepository.deleteById(name);
    }

    public void deleteCategory(String name){
        volunteerAdCategoryRepository.deleteById(name);
    }

    public void acceptVolunteerAd(long id){
        volunteerAdRepository.findById(id).ifPresent(volunteerAd -> {
            volunteerAd.setAccepted(parse(true));
            volunteerAdRepository.save(volunteerAd);
        });
    }

    public List<VolunteerAdDTO> getVolunteerAdBySolr(String text){
        if(text.length()>=3)
            return volunteerAdSearchDao.searchVolunteerAdByQuery(text);
        return null;
    }
}
