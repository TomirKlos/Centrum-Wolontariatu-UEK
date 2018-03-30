package pl.krakow.uek.centrumWolontariatu.web.rest;

import jdk.jfr.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestType;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl.VolunteerRequestDTOImpl;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestPictureRepository;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.repository.solr.VolunteerRequestSearchDao;
import pl.krakow.uek.centrumWolontariatu.service.MailService;
import pl.krakow.uek.centrumWolontariatu.service.UserService;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerRequestService;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.CategoryVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.IdVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.TypeVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.VolunteerRequestVM;

import javax.validation.Valid;

import static pl.krakow.uek.centrumWolontariatu.web.rest.util.ParserRSQLUtil.*;


import java.io.IOException;
import java.util.*;


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

    @Autowired
    VolunteerRequestSearchDao volunteerRequestSearchDao;


    @PostMapping(path = "/vrequest/", consumes="multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public void addVolunteerRequest(@RequestPart VolunteerRequestVM volunteerRequestVM,
                                    @RequestPart  MultipartFile[] file) {
        volunteerRequestService.createVolunteerRequest(volunteerRequestVM.getDescription(), volunteerRequestVM.getTitle(), volunteerRequestVM.getVolunteersAmount(), parse(volunteerRequestVM.isForStudents()), parse(volunteerRequestVM.isForTutors()), volunteerRequestVM.getCategories(), volunteerRequestVM.getTypes(), volunteerRequestVM.getExpirationDate(),  file);
    }


    @GetMapping("/vrequest/image")
    public HashMap<String, String> getImagesByVolunteerId(@RequestParam long volunteerRequestId) {
        return volunteerRequestService.getImagesFromVolunteerRequest(volunteerRequestId);
    }


    @PostMapping("/vrequest/category/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewCategory(@RequestBody CategoryVM categoryVM) {
        volunteerRequestService.createVolunteerRequestCategory(categoryVM.getCategoryName());
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
    public void createNewType(@RequestBody TypeVM typeVM) {
        volunteerRequestService.createVolunteerRequestType(typeVM.getType());
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
    public void acceptVolunteerRequest(@RequestBody IdVM idVM){
        volunteerRequestService.acceptVolunteerRequest(idVM.getId());
    }

    @DeleteMapping("/vrequest/{id}")
    public void deleteVolunteerRequest(@PathVariable Integer id){
        volunteerRequestService.deleteVolunteerRequest(id);
    }

    @GetMapping("/vrequest/solr/{text}")
    public List<VolunteerRequestDTO> getVolunteerRequestBySolr(@PathVariable String text) {
        return volunteerRequestService.getVolunteerRequestBySolr(text);
    }



}
