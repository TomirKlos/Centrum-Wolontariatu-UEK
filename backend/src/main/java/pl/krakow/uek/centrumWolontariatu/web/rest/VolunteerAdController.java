package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAd;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdType;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestCategory;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerAdDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerAdPictureRepository;
import pl.krakow.uek.centrumWolontariatu.service.MailService;
import pl.krakow.uek.centrumWolontariatu.service.UserService;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerAdService;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.CategoryVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.IdVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.TypeVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.VolunteerAdVM;

import java.util.*;

import static pl.krakow.uek.centrumWolontariatu.web.rest.util.ParserRSQLUtil.parseGuavaOptional;

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


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addVolunteerAd(@RequestBody VolunteerAdVM volunteerAdVM) {
        volunteerAdService.createVolunteerAd(volunteerAdVM);
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
        Page<VolunteerAdDTO> volunteerAds = volunteerAdService.findAllByRsql(pageable, parseGuavaOptional(search));
        return new ResponseEntity<>(volunteerAds, HttpStatus.OK);
    }

    @PostMapping("accept")
    public void acceptVolunteerAd(@RequestBody IdVM idVM){
        volunteerAdService.acceptVolunteerAd(idVM.getId());
    }

    @PostMapping("expire")
    public void expireVolunteerAd(@RequestBody IdVM idVM){
        volunteerAdService.setExpired(idVM.getId());
    }

    @DeleteMapping("{id}")
    public void deleteVolunteerAd(@PathVariable Integer id) {
        volunteerAdService.deleteVolunteerAd(id);
    }

    @GetMapping("solr/{text}")
    public List<VolunteerAdDTO> getVolunteerAdBySolr(@PathVariable String text) {
        return volunteerAdService.getVolunteerAdBySolr(text);
    }

    @GetMapping("solrPage/{text}")
    public Page<VolunteerAdDTO> getVolunteerRequestBySolrPage(@PathVariable String text, Pageable pageable) {
        return volunteerAdService.getVolunteerAdBySolrPage(pageable, text);
    }

    @PostMapping(path = "/picture", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public Set<String> addPicture(@RequestBody MultipartFile[] file) {
        return volunteerAdService.addPicturesToVolunteerAd(file);
    }

    @GetMapping("mine")
    @ResponseBody
    public ResponseEntity<Page<VolunteerAdDTO>> findAllMine(Pageable pageable) {
        Page<VolunteerAdDTO> volunteerAds = volunteerAdService.findAllByUserId(pageable);
        return new ResponseEntity<>(volunteerAds, HttpStatus.OK);
    }

    @GetMapping("mineId")
    @ResponseBody
    public ResponseEntity<List<Long>> findAllMineIds(Pageable pageable) {
        List<Long> volunteerAdsIds = volunteerAdService.findAllMineIdsByUserId(pageable);
        return new ResponseEntity<>(volunteerAdsIds, HttpStatus.OK);
    }

    @GetMapping("v2/")
    @ResponseBody
    public ResponseEntity<Page<VolunteerAdDTO>> findAllByRsqWithCategories(Pageable pageable, String[] categories, boolean isByStudents, boolean isByLecturers) {
        Page<VolunteerAdDTO> volunteerAdDTOS = volunteerAdService.findAllWithCategoriesAndAuthorities(pageable, categories, isByStudents, isByLecturers);
        return new ResponseEntity<>(volunteerAdDTOS, HttpStatus.OK);
    }


}
