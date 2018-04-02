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
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerRequestService;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.CategoryVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.IdVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.TypeVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.VolunteerRequestVM;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static pl.krakow.uek.centrumWolontariatu.web.rest.util.ParserRSQLUtil.parse;

@RestController
@RequestMapping("/api")
public class VolunteerRequestController {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final VolunteerRequestService volunteerRequestService;

    public VolunteerRequestController(VolunteerRequestService volunteerRequestService) {
        this.volunteerRequestService = volunteerRequestService;
    }

    @PostMapping(path = "/vrequest/")
    @ResponseStatus(HttpStatus.CREATED)
    public void addVolunteerRequest(@RequestBody VolunteerRequestVM volunteerRequestVM, @RequestBody String[] referenceToPictures) {
        volunteerRequestService.createVolunteerRequest(volunteerRequestVM,  referenceToPictures);
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
    public void acceptVolunteerRequest(@RequestBody IdVM idVM) {
        volunteerRequestService.acceptVolunteerRequest(idVM.getId());
    }

    @DeleteMapping("/vrequest/{id}")
    public void deleteVolunteerRequest(@PathVariable Integer id) {
        volunteerRequestService.deleteVolunteerRequest(id);
    }

    @GetMapping("/vrequest/solr/{text}")
    public List<VolunteerRequestDTO> getVolunteerRequestBySolr(@PathVariable String text) {
        return volunteerRequestService.getVolunteerRequestBySolr(text);
    }

    @PostMapping(path = "/vrequest/picture", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public Set<String> addPicture(@RequestBody MultipartFile[] file) {
        return volunteerRequestService.addPicturesToVolunteerRequest(file);
    }


}
