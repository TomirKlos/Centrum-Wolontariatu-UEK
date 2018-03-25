package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestType;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl.VolunteerRequestDTOImpl;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestPictureRepository;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.service.MailService;
import pl.krakow.uek.centrumWolontariatu.service.UserService;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerRequestService;
import static pl.krakow.uek.centrumWolontariatu.web.rest.util.ParserRSQLUtil.*;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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

    @PostMapping(path = "/vrequest/", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public void addVolunteerRequest(@RequestParam MultipartFile[] file, @RequestParam String description, @RequestParam String title, @RequestParam int numberVolunteers, @RequestParam boolean isForStudents, @RequestParam boolean isForTutors, @RequestParam Set<String> categories, @RequestParam Set<String> types, @RequestParam long expirationDate) {
        volunteerRequestService.createVolunteerRequest(description, title, numberVolunteers, parse(isForStudents), parse(isForTutors), categories, types,expirationDate,  file);
    }

    @GetMapping("/vrequest/image")
    public HashMap<String, String> getImagesByVolunteerId(@RequestParam long volunteerRequestId) {
        return volunteerRequestService.getImagesFromVolunteerRequest(volunteerRequestId);
    }


    @PostMapping("/vrequest/category/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewCategory(@RequestParam String categoryName) {
        volunteerRequestService.createVolunteerRequestCategory(categoryName);
    }

    @GetMapping("/vrequest/category/")
    public List<VolunteerRequestCategory> getAllVolunteerRequestCategory() {
        return volunteerRequestService.getAllCategories();
    }

    @DeleteMapping("/vrequest/category")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@RequestParam String name) {
        volunteerRequestService.deleteCategory(name);
    }

    @PostMapping("/vrequest/type")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewType(@RequestParam String typeName) {
        volunteerRequestService.createVolunteerRequestType(typeName);
    }

    @DeleteMapping("/vrequest/type")
    @ResponseStatus(HttpStatus.OK)
    public void deleteType(@RequestParam String name) {
        volunteerRequestService.deleteType(name);
    }

    @GetMapping("/vrequest/type")
    public List<VolunteerRequestType> getAllVolunteerRequestType() {
        return volunteerRequestService.getAllTypes();
    }

    @GetMapping("/vrequest/")
    @ResponseBody
    public ResponseEntity<Page<VolunteerRequestDTO>> findAllByRsq(@RequestParam(value = "search") Optional<String> search, Pageable pageable) {
        Page<VolunteerRequestDTO> volunteerRequests = volunteerRequestService.findAllByRsql(pageable, parse(search));
        return new ResponseEntity<>(volunteerRequests, HttpStatus.OK);
    }

    @PostMapping("/vrequest/accept")
    public void acceptVolunteerRequest(@RequestParam(value = "id") long id){
        volunteerRequestRepository.findById(id).ifPresent(volunteerRequest -> {
            volunteerRequest.setAccepted(parse(true));
            volunteerRequestRepository.save(volunteerRequest);
        });
    }



}
