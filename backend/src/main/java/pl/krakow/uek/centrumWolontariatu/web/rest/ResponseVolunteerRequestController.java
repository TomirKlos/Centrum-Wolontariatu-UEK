package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.ResponseVolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.service.ResponseVolunteerRequestService;
import pl.krakow.uek.centrumWolontariatu.service.UserService;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.IdVM;

@RestController
@RequestMapping("/api/responseVr/")
public class ResponseVolunteerRequestController {

    private final UserService userService;
    private final ResponseVolunteerRequestService responseVolunteerRequestService;

    public ResponseVolunteerRequestController(UserService userService, ResponseVolunteerRequestService responseVolunteerRequestService) {
        this.userService = userService;
        this.responseVolunteerRequestService = responseVolunteerRequestService;
    }
    /*
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void apply(@RequestParam String description, @RequestParam long volunteerRequestId) {
        responseVolunteerRequestService.apply(description, volunteerRequestId);
    }

    @GetMapping
    public Page<ResponseVolunteerRequestDTO> getAllByVolunteerRequestId(@RequestParam long volunteerRequestId, Pageable pageable){
        return responseVolunteerRequestService.getAllByVolunteerRequestId(volunteerRequestId, pageable);
    }

    @PostMapping("accept")
    public void acceptVolunteerRequest(@RequestBody IdVM idVM) {
        responseVolunteerRequestService.acceptResponse(idVM.getId());
    }

    @PostMapping("confirm")
    public void confirmVolunteerRequest(@RequestBody IdVM idVM) {
        responseVolunteerRequestService.confirmResponse(idVM.getId());
    }*/

}
