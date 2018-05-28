package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.InvitationToVolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.ResponseVolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.service.InvitationToVolunteerRequestService;
import pl.krakow.uek.centrumWolontariatu.service.UserService;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.InvitationToVolunteerRequestVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.ResponseVolunteerRequestVM;

@RestController
@RequestMapping("/api/vrequest/invite")
public class InvitationToVolunteerRequestController {

    private final UserService userService;
    private final InvitationToVolunteerRequestService invitationToVolunteerRequestService;

    public InvitationToVolunteerRequestController(UserService userService, InvitationToVolunteerRequestService invitationToVolunteerRequestService) {
        this.userService = userService;
        this.invitationToVolunteerRequestService = invitationToVolunteerRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void invite(@RequestBody InvitationToVolunteerRequestVM invitationToVolunteerRequestVM) {
        invitationToVolunteerRequestService.invite(invitationToVolunteerRequestVM);
    }

    @GetMapping
    public Page<InvitationToVolunteerRequestDTO> getAllInvitation(@RequestParam long userId, Pageable pageable){
        return invitationToVolunteerRequestService.getAllByUserId(userId, pageable);
    }
}
