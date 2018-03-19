package pl.krakow.uek.centrumWolontariatu.service;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestPicture;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestPictureRepository;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.web.rest.AuthenticationController;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;

import static pl.krakow.uek.centrumWolontariatu.configuration.constant.UserConstant.UPLOADED_FOLDER;

@Service
public class VolunteerRequestService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;
    private final MailService mailService;
    private final VolunteerRequestRepository volunteerRequestRepository;
    private final VolunteerRequestPictureRepository volunteerRequestPictureRepository;

    public VolunteerRequestService(UserService userService, MailService mailService, VolunteerRequestRepository volunteerRequestRepository, VolunteerRequestPictureRepository volunteerRequestPictureRepository) {
        this.userService = userService;
        this.mailService = mailService;
        this.volunteerRequestRepository = volunteerRequestRepository;
        this.volunteerRequestPictureRepository = volunteerRequestPictureRepository;
    }

    public VolunteerRequest createVolunteerRequest(String description, String title, int numberVolunteers, MultipartFile[] file){
        User user = userService.getUserWithAuthorities().get();
        VolunteerRequest volunteerRequest = new VolunteerRequest();
        volunteerRequest.setDescription(description);
        volunteerRequest.setTitle(title);
        volunteerRequest.setVolunteersAmount(numberVolunteers);
        volunteerRequest.setUser(user);

       VolunteerRequestPicture volunteerRequestPicture = addPicturesToVolunteerRequest(file, user, volunteerRequest);
        volunteerRequestRepository.save(volunteerRequest);
        log.debug("User id={} created new volunteer request id={}", user.getId(), volunteerRequest.getId());

        volunteerRequestPictureRepository.save(volunteerRequestPicture);
        return volunteerRequest;
    }

    private VolunteerRequestPicture addPicturesToVolunteerRequest(MultipartFile[] file, User user, VolunteerRequest volunteerRequest){
        for(MultipartFile multipartFile: file){
            if(!multipartFile.getContentType().matches("^(image).*$")){
                throw new BadRequestAlertException("Not allowed format file", "volunteerManagement", "notallowedformatfile");
            }
        }
        VolunteerRequestPicture volunteerRequestPicture = new VolunteerRequestPicture();
        HashMap<String, String> hashPicturesWithReferences = new HashMap<>();
        for(MultipartFile multipartFile: file){
            try {
                byte[] bytes = multipartFile.getBytes();

                String fileTypes[] = multipartFile.getOriginalFilename().split("\\.");
                String fileType = fileTypes[fileTypes.length-1];

                MessageDigest salt = MessageDigest.getInstance("SHA-256");
                salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
                String hexString = DatatypeConverter.printHexBinary(salt.digest());

                Path path = Paths.get(UPLOADED_FOLDER + hexString + "." + fileType);
                Files.write(path, bytes);
                log.debug("User id={} uploaded picture: {}", user.getId(), hexString + "." + fileType);

                hashPicturesWithReferences.put(hexString, multipartFile.getOriginalFilename());

                createThumbnailFromPicture(bytes, hexString, fileType);
                
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e){
                e.printStackTrace();
            }
        }
        volunteerRequestPicture.setReferenceToPicture(hashPicturesWithReferences);
        volunteerRequestPicture.setVolunteerRequest(volunteerRequest);
        return volunteerRequestPicture;
    }

    private void createThumbnailFromPicture(byte[] bytes, String hexString, String fileType){
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Path pathThumbnail = Paths.get(UPLOADED_FOLDER + hexString + "_thumbnail." + fileType);
        try {
            BufferedImage img = Thumbnails.of(bis)
                .size(400, 400)
                .asBufferedImage();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, fileType, baos);
            Files.write(pathThumbnail, baos.toByteArray());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
