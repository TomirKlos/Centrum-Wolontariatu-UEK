package pl.krakow.uek.centrumWolontariatu.service;

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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

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

                String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex("hash" + user.getId() + System.currentTimeMillis() + multipartFile.getOriginalFilename());
                Path path = Paths.get(UPLOADED_FOLDER + md5 + "." + fileType);
                Files.write(path, bytes);
                log.debug("User id={} uploaded picture: {}", user.getId(), md5 + "." + fileType);

                hashPicturesWithReferences.put(md5, multipartFile.getOriginalFilename());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        volunteerRequestPicture.setReferenceToPicture(hashPicturesWithReferences);
        volunteerRequestPicture.setVolunteerRequest(volunteerRequest);
        return volunteerRequestPicture;
    }

}
