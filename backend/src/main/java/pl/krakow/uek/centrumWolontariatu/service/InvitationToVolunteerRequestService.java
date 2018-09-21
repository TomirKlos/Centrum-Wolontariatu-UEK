package pl.krakow.uek.centrumWolontariatu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.*;
import pl.krakow.uek.centrumWolontariatu.repository.*;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.InvitationToVolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.ResponseVolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.web.rest.AuthenticationController;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.InvitationVolunteerResponsesPermissionException;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.ResponseAcceptException;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.VolunteerRequestResponsesPermissionException;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.InvitationToVolunteerRequestVM;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.ResponseVolunteerRequestVM;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.Predicate;

import static pl.krakow.uek.centrumWolontariatu.web.rest.util.ParserRSQLUtil.parse;

@Service
public class InvitationToVolunteerRequestService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final InvitationToVolunteerRequestRepository invitationToVolunteerRequestRepository;
    private final VolunteerRequestRepository volunteerRequestRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final VolunteerAdRepository volunteerAdRepository;
    private final ResponseVolunteerRequestService responseVolunteerRequestService;

    public InvitationToVolunteerRequestService(InvitationToVolunteerRequestRepository invitationToVolunteerRequestRepository, VolunteerRequestRepository volunteerRequestRepository, UserService userService, UserRepository userRepository, VolunteerAdRepository volunteerAdRepository, ResponseVolunteerRequestService responseVolunteerRequestService) {
        this.invitationToVolunteerRequestRepository = invitationToVolunteerRequestRepository;
        this.volunteerRequestRepository = volunteerRequestRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.volunteerAdRepository = volunteerAdRepository;
        this.responseVolunteerRequestService = responseVolunteerRequestService;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_LECTURER')")
    public void invite(InvitationToVolunteerRequestVM invitationToVolunteerRequestVM){
        if(userService.getUserWithAuthorities().get().getId()==volunteerRequestRepository.findById(invitationToVolunteerRequestVM.getVolunteerRequestId()).get().getUser().getId()
            && userService.getUserWithAuthorities().get().getId()!= userRepository.findOneById(volunteerAdRepository.findById(invitationToVolunteerRequestVM.getVolunteerAdId()).get().getUser().getId()).get().getId()) {
            if(!canInviteToVolunteerRequest(invitationToVolunteerRequestVM)){
                throw new BadRequestAlertException("You cannot invite user to Volunteer Request because user doesnt have required status", "volunteerRequestInvitation", "cannotinvitetovolunteerrequestduetostatus");
            }
            User user = userRepository.findOneById(volunteerAdRepository.findById(invitationToVolunteerRequestVM.getVolunteerAdId()).get().getUser().getId()).get();

            InvitationToVolunteerRequest invitationToVolunteerRequest = new InvitationToVolunteerRequest();
            invitationToVolunteerRequest.setDescription(invitationToVolunteerRequestVM.getDescription());
            invitationToVolunteerRequest.setUserInvited(user);
            invitationToVolunteerRequest.setVolunteerRequest(volunteerRequestRepository.getOne(invitationToVolunteerRequestVM.getVolunteerRequestId()));
            invitationToVolunteerRequest.setVolunteerAd(volunteerAdRepository.getOne(invitationToVolunteerRequestVM.getVolunteerAdId()));

            ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
            long epochMillis = utc.toEpochSecond() * 1000;
            invitationToVolunteerRequest.setTimestamp(epochMillis);
            invitationToVolunteerRequestRepository.save(invitationToVolunteerRequest);

            log.debug("User id={} invited to Volunteer Request id={}", user.getId(), invitationToVolunteerRequest.getVolunteerRequest().getId());
        } else throw new BadRequestAlertException("You cannot invite yourself for your own Volunteer Request", "volunteerRequestManagement", "cannotinviteyourselforyouownvolunteerrequest");
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

    public Page<InvitationToVolunteerRequestDTO> getAllByInvitationId(long adId, Pageable pageable){
        Page<InvitationToVolunteerRequestDTO> page;
        long userId=volunteerAdRepository.findById(adId).get().getUser().getId();
        if(userService.getUserWithAuthorities().get().getId()==userId){
            page = invitationToVolunteerRequestRepository.findAllByVolunteerAdId(pageable, adId);
            setSeenFlag(page);
            return page;
        }
        else throw new BadRequestAlertException("You cannot see not your own invitations", "invitationVolunteerRequestManagement", "cannotseenotowninvitations");
    }

    public long getAllUnseen(long adId){
        if(isUserOwnerOfVolunteerAd(adId) ){

            return invitationToVolunteerRequestRepository.countByVolunteerAdIdAndSeen(adId, (byte)0);
        }

        else throw new InvitationVolunteerResponsesPermissionException();
    }


    private void setSeenFlag(Page<InvitationToVolunteerRequestDTO> pageInvitation){
        for(InvitationToVolunteerRequestDTO invitationToVolunteerRequestDTO: pageInvitation.getContent()){
            invitationToVolunteerRequestRepository.findById(invitationToVolunteerRequestDTO.getId()).map(invitationToVolunteerRequest -> {
                invitationToVolunteerRequest.setSeen((byte)1);
                return invitationToVolunteerRequestRepository.save(invitationToVolunteerRequest);
            });
        }
    }

    private boolean isUserOwnerOfVolunteerAd(long adId){
        return volunteerAdRepository.findById(adId).map(invitationToVolunteerRequest -> {
            return invitationToVolunteerRequest.getUser().getId()==userService.getUserId();
        }).orElseThrow(InvitationVolunteerResponsesPermissionException::new);
    }


    public void acceptInvitation(long invitationId){
        if(isUserOwnerOfVolunteerAdByInvitation(invitationId))
            invitationToVolunteerRequestRepository.findById(invitationId).ifPresent(response -> {
                response.setAccepted(parse(true));
                invitationToVolunteerRequestRepository.save(response);

                volunteerRequestRepository.findById(response.getVolunteerRequest().getId()).map(volunteerRequest -> {
                    if(volunteerRequest.getVolunteersAmount()>0){
                        volunteerRequest.setVolunteersAmount(volunteerRequest.getVolunteersAmount()-1);
                        return volunteerRequestRepository.save(volunteerRequest);
                    }else return null;
                });
                log.debug("User id={} accepted Volunteer invitation id={} user={}", userService.getUserId(), response.getId(), response.getId());
                resendInvitationAsApplication(response.getVolunteerRequest(), response.getDescription());
            });
        else throw new ResponseAcceptException();
    }

    public void disableAcceptedInvitation(long responseId){
        if(isUserOwnerOfVolunteerAdByInvitation(responseId))
            invitationToVolunteerRequestRepository.findById(responseId).ifPresent(response -> {
                response.setAccepted(parse(false));
                invitationToVolunteerRequestRepository.save(response);

                volunteerRequestRepository.findById(response.getVolunteerRequest().getId()).map(volunteerRequest -> {
                    volunteerRequest.setVolunteersAmount(volunteerRequest.getVolunteersAmount()+1);
                    return volunteerRequestRepository.save(volunteerRequest);

                });
                log.debug("User id={} disabled accepted Volunteer Invitation id={} user={}", userService.getUserId(), response.getId(), response.getId());
            });
        else throw new ResponseAcceptException();
    }

    private boolean isUserOwnerOfVolunteerAdByInvitation(long invitationId){
        return invitationToVolunteerRequestRepository.findById(invitationId).map(response -> {
            return response.getUserInvited().getId()==userService.getUserId();
        }).orElseThrow(ResponseAcceptException::new);
    }

    private void resendInvitationAsApplication(VolunteerRequest volunteerRequest, String description){
        ResponseVolunteerRequestVM responseVolunteerRequestVM = new ResponseVolunteerRequestVM();
        responseVolunteerRequestVM.setVolunteerRequestId(volunteerRequest.getId());
        responseVolunteerRequestVM.setDescription("Potwierdzam chęć udzialu w wolontariacie! | " + description);

        responseVolunteerRequestService.apply(responseVolunteerRequestVM);
    }

    private boolean canInviteToVolunteerRequest(InvitationToVolunteerRequestVM invitationToVolunteerRequestVM){
        Set<Authority> authoritySet = volunteerAdRepository.findById(invitationToVolunteerRequestVM.getVolunteerAdId()).get().getUser().getAuthorities();
        return doHaveRequiredAuthorities(authoritySet, invitationToVolunteerRequestVM.getVolunteerRequestId());
    }

    private boolean doHaveRequiredAuthorities(Set<Authority> authoritySet, Long id){
        Predicate<Authority> userPredicate = e -> e.getName().equals("ROLE_USER");
        Predicate<Authority> lecturerPredicate = e -> e.getName().equals("ROLE_LECTURER");

        VolunteerRequest volunteerRequest = volunteerRequestRepository.findById(id).get();
        if(authoritySet.stream().anyMatch(userPredicate) && volunteerRequest.getIsForStudents()==1)
            return true;
        if(authoritySet.stream().anyMatch(lecturerPredicate) && volunteerRequest.getIsForTutors()==1)
            return true;

        return false;
    }




}
