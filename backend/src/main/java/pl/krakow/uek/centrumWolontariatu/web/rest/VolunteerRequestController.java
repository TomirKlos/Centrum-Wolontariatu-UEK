package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestType;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestPictureRepository;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.service.MailService;
import pl.krakow.uek.centrumWolontariatu.service.UserService;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerRequestService;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


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
    public void addVolunteerRequest(@RequestParam MultipartFile[] file, @RequestParam String description, @RequestParam String title, @RequestParam int numberVolunteers, @RequestParam boolean isForStudents, @RequestParam boolean isForTutors, @RequestParam Set<String> categories, @RequestParam Set<String> types) {
        volunteerRequestService.createVolunteerRequest(description, title, numberVolunteers, isForStudents, isForTutors, categories, types, file);
    }

    @GetMapping("/vrequest/getimage")
    public HashMap<String, String> getImagesByVolunteerId(@RequestParam long volunteerRequestId) {
        return volunteerRequestService.getImagesFromVolunteerRequest(volunteerRequestId);
    }

    @GetMapping("/vrequest/getVolunteerRequests")
    public List<VolunteerRequestDTO> getVolunteerRequests(@RequestParam int page, @RequestParam int numberOfResultsPerPage, @RequestParam boolean isDescending) {
        return volunteerRequestService.getVolunteerRequests(page, numberOfResultsPerPage, isDescending);
    }

    @PostMapping("/vrequest/category/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewCategory(@RequestParam String categoryName) {
        volunteerRequestService.createVolunteerRequestCategory(categoryName);
    }

    @GetMapping("/vrequest/category/get")
    public List<VolunteerRequestCategory> getAllVolunteerRequestCategory() {
        return volunteerRequestService.getAllCategories();
    }

    @PostMapping("/vrequest/type/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewType(@RequestParam String typeName) {
        volunteerRequestService.createVolunteerRequestType(typeName);
    }

    @GetMapping("/vrequest/type/get")
    public List<VolunteerRequestType> getAllVolunteerRequestType() {
        return volunteerRequestService.getAllTypes();
    }

}
