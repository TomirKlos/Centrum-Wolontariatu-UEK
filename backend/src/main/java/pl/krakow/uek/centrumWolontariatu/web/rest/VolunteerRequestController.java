package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestPictureRepository;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.service.MailService;
import pl.krakow.uek.centrumWolontariatu.service.UserService;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerRequestService;


@RestController
@RequestMapping("/api")
public class VolunteerRequestController {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;
    private final MailService mailService;
    private final VolunteerRequestService volunteerRequestService;
    private final VolunteerRequestRepository volunteerRequestRepository;
    private final VolunteerRequestPictureRepository volunteerRequestPictureRepository;

    public VolunteerRequestController(UserService userService, MailService mailService, VolunteerRequestRepository volunteerRequestRepository, VolunteerRequestPictureRepository volunteerRequestPictureRepository, VolunteerRequestService volunteerRequestService) {
        this.userService = userService;
        this.mailService = mailService;
        this.volunteerRequestService = volunteerRequestService;
        this.volunteerRequestRepository = volunteerRequestRepository;
        this.volunteerRequestPictureRepository = volunteerRequestPictureRepository;
    }

//TODO Odbierac VolunteerRequestVM jako @RequestPart zamiast parametrami, gdyz normalna metoda @RequestBody + @RequestParam nie przechodzi.

    @PostMapping(path = "/vrequest/create", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public void addVolunteerRequest(@RequestParam MultipartFile[] file, @RequestParam String description, @RequestParam String title, @RequestParam int numberVolunteers) {

        volunteerRequestService.createVolunteerRequest(description, title, numberVolunteers, file);


    }
}
