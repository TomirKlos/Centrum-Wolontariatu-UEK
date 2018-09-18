package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestType;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerRequestService;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.CategoryVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.IdVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.TypeVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.VolunteerRequestVM;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import static pl.krakow.uek.centrumWolontariatu.web.rest.util.ParserRSQLUtil.parseGuavaOptional;

@RestController
@RequestMapping("/api")
public class VolunteerRequestController {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final VolunteerRequestService volunteerRequestService;

    public VolunteerRequestController(VolunteerRequestService volunteerRequestService) {
        this.volunteerRequestService = volunteerRequestService;
    }

    @PostMapping(path = "/vrequest")
    @ResponseStatus(HttpStatus.CREATED)
    public void addVolunteerRequest(@RequestBody VolunteerRequestVM volunteerRequestVM) {
        volunteerRequestService.createVolunteerRequest(volunteerRequestVM);
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

    @GetMapping("/vrequest")
    @ResponseBody
    public ResponseEntity<Page<VolunteerRequestDTO>> findAllByRsq(@RequestParam(value = "search") Optional<String> search, Pageable pageable) {
        Page<VolunteerRequestDTO> volunteerRequests = volunteerRequestService.findAllByRsql(pageable, parseGuavaOptional(search));
        return new ResponseEntity<>(volunteerRequests, HttpStatus.OK);
    }

    @PostMapping("/vrequest/accept")
    public void acceptVolunteerRequest(@RequestBody IdVM idVM) {
        volunteerRequestService.acceptVolunteerRequest(idVM.getId());
    }

    @PostMapping("/vrequest/expire")
    public void expireVolunteerRequest(@RequestBody IdVM idVM) {
        volunteerRequestService.setExpired(idVM.getId());
    }

    @DeleteMapping("/vrequest/{id}")
    public void deleteVolunteerRequest(@PathVariable Integer id) {
        volunteerRequestService.deleteVolunteerRequest(id);
    }

    @GetMapping("/vrequest/solr/{text}")
    public List<VolunteerRequestDTO> getVolunteerRequestBySolr(@PathVariable String text) {
        return volunteerRequestService.getVolunteerRequestBySolr(text);
    }

    @GetMapping("/vrequest/solrPage/{text}")
    public Page<VolunteerRequestDTO> getVolunteerRequestBySolrPage(@PathVariable String text, Pageable pageable) {
        return volunteerRequestService.getVolunteerRequestBySolrPage(pageable, text);
    }

    @PostMapping(path = "/vrequest/picture", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public Set<String> addPicture(@RequestBody MultipartFile[] file) {
        return volunteerRequestService.addPicturesToVolunteerRequest(file);
    }

    @GetMapping("/vrequest/mine")
    @ResponseBody
    public ResponseEntity<Page<VolunteerRequestDTO>> findAllMine(Pageable pageable) {
        Page<VolunteerRequestDTO> volunteerAds = volunteerRequestService.findAllByUserId(pageable);
        return new ResponseEntity<>(volunteerAds, HttpStatus.OK);
    }

    @GetMapping("/vrequest/mineId")
    @ResponseBody
    public ResponseEntity<List<Long>> findAllMineIds(Pageable pageable) {
        List<Long> volunteerAdsIds = volunteerRequestService.findAllMineIdsByUserId(pageable);
        return new ResponseEntity<>(volunteerAdsIds, HttpStatus.OK);
    }

    @GetMapping("/vrequest/v2/")
    @ResponseBody
    public ResponseEntity<Page<VolunteerRequestDTO>> findAllByRsqWithCategories(Pageable pageable, String[] categories) {
        Page<VolunteerRequestDTO> volunteerRequests = volunteerRequestService.findAllByRsqlWithCategories(pageable, categories);
        return new ResponseEntity<>(volunteerRequests, HttpStatus.OK);
    }


}
