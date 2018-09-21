package pl.krakow.uek.centrumWolontariatu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.Authority;
import pl.krakow.uek.centrumWolontariatu.domain.ResponseVolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.ResponseVolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.repository.ResponseVolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.web.rest.AuthenticationController;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.ResponseAcceptException;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.VolunteerRequestResponsesPermissionException;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.ResponseVolunteerRequestVM;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static pl.krakow.uek.centrumWolontariatu.web.rest.util.ParserRSQLUtil.parse;

@Service
public class ResponseVolunteerRequestService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final ResponseVolunteerRequestRepository responseVolunteerRequestRepository;
    private final VolunteerRequestRepository volunteerRequestRepository;
    private final VolunteerCertificateService volunteerCertificateService;
    private final UserService userService;

    public ResponseVolunteerRequestService(ResponseVolunteerRequestRepository responseVolunteerRequestRepository, VolunteerRequestRepository volunteerRequestRepository, VolunteerCertificateService volunteerCertificateService, UserService userService) {
        this.responseVolunteerRequestRepository = responseVolunteerRequestRepository;
        this.volunteerRequestRepository = volunteerRequestRepository;
        this.volunteerCertificateService = volunteerCertificateService;
        this.userService = userService;
    }

    // @PreAuthorize("#responseVolunteerRequestVm.name == principal.name")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_LECTURER')")
    public void apply(ResponseVolunteerRequestVM responseVolunteerRequestVM){
        if(userService.getUserWithAuthorities().get().getId()!=volunteerRequestRepository.findById(responseVolunteerRequestVM.getVolunteerRequestId()).get().getUser().getId() ) {
           if(!canApplyForVolunteerRequest(userService.getUserWithAuthorities().get().getAuthorities(), responseVolunteerRequestVM.getVolunteerRequestId())){
               throw new BadRequestAlertException("You cannot apply for Volunteer Request because dont have status wanted by Volunteer Request", "volunteerRequestManagement", "cannotaplyforvolunteerrequestbecauseofstatus");
           }
            User user = userService.getUserWithAuthorities().get();
            ResponseVolunteerRequest responseVolunteerRequest = new ResponseVolunteerRequest();
            responseVolunteerRequest.setDescription(responseVolunteerRequestVM.getDescription());
            responseVolunteerRequest.setUser(user);
            responseVolunteerRequest.setVolunteerRequest(volunteerRequestRepository.getOne(responseVolunteerRequestVM.getVolunteerRequestId()));

            ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
            long epochMillis = utc.toEpochSecond() * 1000;
            responseVolunteerRequest.setTimestamp(epochMillis);

            responseVolunteerRequestRepository.save(responseVolunteerRequest);
            log.debug("User id={} applied for Volunteer Request id={}", user.getId(), responseVolunteerRequest.getVolunteerRequest().getId());
        } else throw new BadRequestAlertException("You cannot apply for your own Volunteer Request", "volunteerRequestManagement", "cannotaplyforownvolunteerrequest");
    }

    public Page<ResponseVolunteerRequestDTO> getAllByVolunteerRequestId(long volunteerRequestId, Pageable pageable){
        Page<ResponseVolunteerRequestDTO> page;
        if(isUserOwnerOfVolunteerRequest(volunteerRequestId) ){
            page = responseVolunteerRequestRepository.findAllByVolunteerRequestId(pageable, volunteerRequestId);
            setSeenFlag(page);
            return page;
        }

        else throw new VolunteerRequestResponsesPermissionException();
    }

    public long getAllUnseen(long volunteerRequestId){
        if(isUserOwnerOfVolunteerRequest(volunteerRequestId) ){

            return responseVolunteerRequestRepository.countByVolunteerRequestIdAndSeen(volunteerRequestId, (byte)0);
        }

        else throw new VolunteerRequestResponsesPermissionException();
    }


    private void setSeenFlag(Page<ResponseVolunteerRequestDTO> pageResponseVr){
        for(ResponseVolunteerRequestDTO responseVolunteerRequestDTO: pageResponseVr.getContent()){
            responseVolunteerRequestRepository.findById(responseVolunteerRequestDTO.getId()).map(responseVolunteerRequest -> {
                responseVolunteerRequest.setSeen((byte)1);
                return responseVolunteerRequestRepository.save(responseVolunteerRequest);
            });
        }
    }

    private boolean isUserOwnerOfVolunteerRequest(long volunteerRequestId){
            return volunteerRequestRepository.findById(volunteerRequestId).map(volunteerRequest -> {
              return volunteerRequest.getUser().getId()==userService.getUserId();
          }).orElseThrow(VolunteerRequestResponsesPermissionException::new);
    }

    private boolean isUserOwnerOfVolunteerRequestByResponse(long responseId){
       return responseVolunteerRequestRepository.findById(responseId).map(response -> {
            return response.getVolunteerRequest().getUser().getId()==userService.getUserId();
        }).orElseThrow(ResponseAcceptException::new);
    }

    //should be CacheEvict cuz it changes
    @CacheEvict(value = "volunteerRequestsWithCategories", allEntries = true)
    public void acceptResponse(long responseId){
        if(isUserOwnerOfVolunteerRequestByResponse(responseId))
            responseVolunteerRequestRepository.findById(responseId).ifPresent(response -> {
                response.setAccepted(parse(true));
                responseVolunteerRequestRepository.save(response);

                volunteerRequestRepository.findById(response.getVolunteerRequest().getId()).map(volunteerRequest -> {
                    if(volunteerRequest.getVolunteersAmount()>0){
                        volunteerRequest.setVolunteersAmount(volunteerRequest.getVolunteersAmount()-1);
                        return volunteerRequestRepository.save(volunteerRequest);
                    }else return null;
                });
                log.debug("User id={} accepted Volunteer Response id={} user={}", userService.getUserId(), response.getId(), response.getId());
            });
        else throw new ResponseAcceptException();
    }

    @CacheEvict(value = "volunteerRequestsWithCategories", allEntries = true)
    public void disableAcceptedResponse(long responseId){
        if(isUserOwnerOfVolunteerRequestByResponse(responseId))
            responseVolunteerRequestRepository.findById(responseId).ifPresent(response -> {
                response.setAccepted(parse(false));
                responseVolunteerRequestRepository.save(response);

                volunteerRequestRepository.findById(response.getVolunteerRequest().getId()).map(volunteerRequest -> {
                        volunteerRequest.setVolunteersAmount(volunteerRequest.getVolunteersAmount()+1);
                        return volunteerRequestRepository.save(volunteerRequest);

                });
                log.debug("User id={} disabled accepted Volunteer Response id={} user={}", userService.getUserId(), response.getId(), response.getId());
            });
        else throw new ResponseAcceptException();
    }


    public void confirmResponse(long responseId, String feedback){
        if(isUserOwnerOfVolunteerRequestByResponse(responseId))
            responseVolunteerRequestRepository.findById(responseId).ifPresent(response -> {
                response.setConfirmation(parse(true));
                response.setFeedback(feedback);
                responseVolunteerRequestRepository.save(response);

                volunteerCertificateService.sendVolunteerToCertification(response);
                log.debug("User id={} confirmed and send to certification Volunteer Response id={} and user={}", userService.getUserId(), response.getId(), response.getUser().getId());
            });
        else throw new ResponseAcceptException();
    }

    private boolean canApplyForVolunteerRequest(Set<Authority> authoritySet, Long id){
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
