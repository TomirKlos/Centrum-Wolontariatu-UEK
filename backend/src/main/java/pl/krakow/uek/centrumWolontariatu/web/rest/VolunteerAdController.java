package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdType;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerAdDTO;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerAdPictureRepository;
import pl.krakow.uek.centrumWolontariatu.service.MailService;
import pl.krakow.uek.centrumWolontariatu.service.UserService;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerAdService;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.CategoryVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.TypeVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.VolunteerAdVM;

import java.util.*;

import static pl.krakow.uek.centrumWolontariatu.web.rest.util.ParserRSQLUtil.parse;

@RestController
@RequestMapping("/api/vAd/")
public class VolunteerAdController {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;
    private final MailService mailService;
    private final VolunteerAdService volunteerAdService;
    private final VolunteerAdPictureRepository volunteerAdPictureRepository;

    public VolunteerAdController(UserService userService, MailService mailService, VolunteerAdPictureRepository volunteerAdPictureRepository, VolunteerAdService volunteerAdService) {
        this.userService = userService;
        this.mailService = mailService;
        this.volunteerAdService = volunteerAdService;
        this.volunteerAdPictureRepository = volunteerAdPictureRepository;
    }


    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public void addVolunteerAd(@RequestPart MultipartFile[] file,
                               @RequestPart VolunteerAdVM volunteerAdVM) {
        volunteerAdService.createVolunteerAd(volunteerAdVM.getDescription(), volunteerAdVM.getTitle(), volunteerAdVM.getCategories(), volunteerAdVM.getTypes(), volunteerAdVM.getExpirationDate(), file);
    }

    @GetMapping("image")
    public HashMap<String, String> getImagesByVolunteerId(@RequestParam long volunteerAdId) {
        return volunteerAdService.getImagesFromVolunteerAd(volunteerAdId);
    }


    @PostMapping("category/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewCategory(@RequestBody CategoryVM categoryVM) {
        volunteerAdService.createVolunteerAdCategory(categoryVM.getCategoryName());
    }

    @GetMapping("category/")
    public List<VolunteerAdCategory> getAllVolunteerAdCategory() {
        return volunteerAdService.getAllCategories();
    }

    @DeleteMapping("category")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@RequestParam String name) {
        volunteerAdService.deleteCategory(name);
    }

    @PostMapping("type")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewType(@RequestBody TypeVM typeVM) {
        volunteerAdService.createVolunteerAdType(typeVM.getType());
    }

    @DeleteMapping("type")
    @ResponseStatus(HttpStatus.OK)
    public void deleteType(@RequestParam String name) {
        volunteerAdService.deleteType(name);
    }

    @GetMapping("type")
    public List<VolunteerAdType> getAllVolunteerAdType() {
        return volunteerAdService.getAllTypes();
    }

    @GetMapping()
    @ResponseBody
    public ResponseEntity<Page<VolunteerAdDTO>> findAllByRsq(@RequestParam(value = "search") Optional<String> search, Pageable pageable) {
        Page<VolunteerAdDTO> volunteerAds = volunteerAdService.findAllByRsql(pageable, parse(search));
        return new ResponseEntity<>(volunteerAds, HttpStatus.OK);
    }

    @PostMapping("accept")
    public void acceptVolunteerAd(@RequestParam(value = "id") long id){
        volunteerAdService.acceptVolunteerAd(id);
    }

    @GetMapping("solr/{text}")
    public List<VolunteerAdDTO> getVolunteerAdBySolr(@PathVariable String text) {
        return volunteerAdService.getVolunteerAdBySolr(text);
    }


}
