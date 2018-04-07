package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.krakow.uek.centrumWolontariatu.configuration.constant.AuthoritiesConstants;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerRequestService;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.CategoryVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.IdVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.TypeVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.VolunteerRequestVM;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vrequest")
public class VolunteerRequestController {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final VolunteerRequestService volunteerRequestService;

    public VolunteerRequestController(VolunteerRequestService volunteerRequestService) {
        this.volunteerRequestService = volunteerRequestService;
    }

    /*
     * VOLUNTEER REQUEST
     */

    @GetMapping()
    public Page<VolunteerRequest> findAllByRsq(@RequestParam(value = "search") Optional<String> search, Pageable pageable) {
        if (search.isPresent()) {
            return volunteerRequestService.findAllByRsql(pageable, search.get());
        } else {
            return volunteerRequestService.getAll(pageable);
        }

    }

    @Secured(AuthoritiesConstants.LECTURER)
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void addVolunteerRequest(@Valid @RequestBody VolunteerRequestVM volunteerRequestVM) {
        volunteerRequestService.create(volunteerRequestVM);
    }

    @Secured(AuthoritiesConstants.LECTURER)
    @DeleteMapping("/{id}")
    public void deleteVolunteerRequest(@PathVariable Integer id) {
        volunteerRequestService.delete(id);
    }

    @Secured(AuthoritiesConstants.ADMIN)
    @PostMapping("/accept")
    public void acceptVolunteerRequest(@RequestBody IdVM idVM) {
        volunteerRequestService.changeAccepted(idVM.getId());
    }


    /*
     * CATEGORIES
     */

    @GetMapping("/categories")
    public List<String> getAllVolunteerRequestCategory() {
        return volunteerRequestService.getAllCategories();
    }

    @Secured(AuthoritiesConstants.LECTURER)
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewCategory(@Valid @RequestBody CategoryVM categoryVM) {
        volunteerRequestService.createCategory(categoryVM.getCategoryName());
    }

    @Secured(AuthoritiesConstants.ADMIN)
    @DeleteMapping("/categories/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable String name) {
        volunteerRequestService.deleteCategory(name);
    }

    /*
     * TYPES
     */

    @GetMapping("/types")
    public List<String> getAllVolunteerRequestType() {
        return volunteerRequestService.getAllTypes();
    }

    @Secured(AuthoritiesConstants.LECTURER)
    @PostMapping("/types")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewType(@RequestBody TypeVM typeVM) {
        volunteerRequestService.createType(typeVM.getType());
    }

    @Secured(AuthoritiesConstants.ADMIN)
    @DeleteMapping("/types/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteType(@PathVariable String name) {
        volunteerRequestService.deleteType(name);
    }


//

//
//    @GetMapping("/vrequest/solr/{text}")
//    public List<VolunteerRequestDTO> getVolunteerRequestBySolr(@PathVariable String text) {
//        return volunteerRequestService.getVolunteerRequestBySolr(text);
//    }
//
//    @PostMapping(path = "/vrequest/picture", consumes = "multipart/form-data")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Set<String> addPicture(@RequestBody MultipartFile[] file) {
//        return volunteerRequestService.addPicturesToVolunteerRequest(file);
//    }
//

}
