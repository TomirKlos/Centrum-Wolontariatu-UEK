package pl.krakow.uek.centrumWolontariatu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.InvitationToVolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.repository.*;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.InvitationToVolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.ResponseVolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.web.rest.AuthenticationController;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.VolunteerRequestResponsesPermissionException;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.InvitationToVolunteerRequestVM;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class InvitationToVolunteerRequestService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final InvitationToVolunteerRequestRepository invitationToVolunteerRequestRepository;
    private final VolunteerRequestRepository volunteerRequestRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final VolunteerAdRepository volunteerAdRepository;

    public InvitationToVolunteerRequestService(InvitationToVolunteerRequestRepository invitationToVolunteerRequestRepository, VolunteerRequestRepository volunteerRequestRepository, UserService userService, UserRepository userRepository, VolunteerAdRepository volunteerAdRepository) {
        this.invitationToVolunteerRequestRepository = invitationToVolunteerRequestRepository;
        this.volunteerRequestRepository = volunteerRequestRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.volunteerAdRepository = volunteerAdRepository;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_LECTURER')")
    public void invite(InvitationToVolunteerRequestVM invitationToVolunteerRequestVM){
        if(userService.getUserWithAuthorities().get().getId()==volunteerRequestRepository.findById(invitationToVolunteerRequestVM.getVolunteerRequestId()).get().getUser().getId()
            && userService.getUserWithAuthorities().get().getId()!= userRepository.findOneById(volunteerAdRepository.findById(invitationToVolunteerRequestVM.getVolunteerAdId()).get().getUser().getId()).get().getId()) {
            User user = userRepository.findOneById(volunteerAdRepository.findById(invitationToVolunteerRequestVM.getVolunteerAdId()).get().getUser().getId()).get();

            InvitationToVolunteerRequest invitationToVolunteerRequest = new InvitationToVolunteerRequest();
            invitationToVolunteerRequest.setDescription(invitationToVolunteerRequestVM.getDescription());
            invitationToVolunteerRequest.setUserInvited(user);
            invitationToVolunteerRequest.setVolunteerRequest(volunteerRequestRepository.getOne(invitationToVolunteerRequestVM.getVolunteerRequestId()));

            ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
            long epochMillis = utc.toEpochSecond() * 1000;
            invitationToVolunteerRequest.setTimestamp(epochMillis);
            invitationToVolunteerRequestRepository.save(invitationToVolunteerRequest);

            log.debug("User id={} invited to Volunteer Request id={}", user.getId(), invitationToVolunteerRequest.getVolunteerRequest().getId());
        } else throw new BadRequestAlertException("You cannot invite yourself for your own Volunteer Request", "volunteerRequestManagement", "cannotaplyforownvolunteerrequest");
    }

    public Page<InvitationToVolunteerRequestDTO> getAllByUserId(long userId, Pageable pageable){
        Page<InvitationToVolunteerRequestDTO> page;
        if(userService.getUserWithAuthorities().get().getId()==userId){
            page = invitationToVolunteerRequestRepository.findAllByUserInvitedId(pageable, userId);
            //setSeenFlag(page);
            return page;
        }
        else throw new BadRequestAlertException("You cannot see not your own invitations", "invitationVolunteerRequestManagement", "cannotseenotowninvitations");
    }


}
