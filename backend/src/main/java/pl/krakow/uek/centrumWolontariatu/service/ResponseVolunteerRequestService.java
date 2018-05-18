package pl.krakow.uek.centrumWolontariatu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.ResponseVolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.ResponseVolunteerRequestDTO;
import pl.krakow.uek.centrumWolontariatu.repository.ResponseVolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.web.rest.AuthenticationController;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.ResponseAcceptException;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.VolunteerRequestResponsesPermissionException;

import static pl.krakow.uek.centrumWolontariatu.web.rest.util.ParserRSQLUtil.parse;

@Service
public class ResponseVolunteerRequestService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final ResponseVolunteerRequestRepository responseVolunteerRequestRepository;
    private final VolunteerRequestRepository volunteerRequestRepository;
    private final UserService userService;

    public ResponseVolunteerRequestService(ResponseVolunteerRequestRepository responseVolunteerRequestRepository, VolunteerRequestRepository volunteerRequestRepository, UserService userService) {
        this.responseVolunteerRequestRepository = responseVolunteerRequestRepository;
        this.volunteerRequestRepository = volunteerRequestRepository;
        this.userService = userService;
    }
    /*
    public void apply(String description, long volunteerRequestId){
        if(userService.getUserWithAuthorities().get().getId()!=volunteerRequestRepository.findById(volunteerRequestId).get().getUser().getId() ) {
            User user = userService.getUserWithAuthorities().get();
            ResponseVolunteerRequest responseVolunteerRequest = new ResponseVolunteerRequest();
            responseVolunteerRequest.setDescription(description);
            responseVolunteerRequest.setUser(user);
            responseVolunteerRequest.setVolunteerRequest(volunteerRequestRepository.getOne(volunteerRequestId));
            responseVolunteerRequestRepository.save(responseVolunteerRequest);
            log.debug("User id={} applied for Volunteer Request id={}", user.getId(), responseVolunteerRequest.getVolunteerRequest().getId());
        } else throw new BadRequestAlertException("You cannot apply for your own Volunteer Request", "volunteerRequestManagement", "cannotaplyforownvolunteerrequest");

    }

    public Page<ResponseVolunteerRequestDTO> getAllByVolunteerRequestId(long volunteerRequestId, Pageable pageable){
        if(isUserOwnerOfVolunteerRequest(volunteerRequestId) )
             return responseVolunteerRequestRepository.findAllByVolunteerRequestId(pageable, volunteerRequestId);
        else throw new VolunteerRequestResponsesPermissionException();
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

    public void acceptResponse(long responseId){
        if(isUserOwnerOfVolunteerRequestByResponse(responseId))
            responseVolunteerRequestRepository.findById(responseId).ifPresent(response -> {
                response.setAccepted(parse(true));
                responseVolunteerRequestRepository.save(response);
                log.debug("User id={} accepted Volunteer Response id={} user={}", userService.getUserId(), response.getId(), response.getId());
            });
        else throw new ResponseAcceptException();
    }

    public void confirmResponse(long responseId){
        if(isUserOwnerOfVolunteerRequestByResponse(responseId))
            responseVolunteerRequestRepository.findById(responseId).ifPresent(response -> {
                response.setConfirmation(parse(true));
                responseVolunteerRequestRepository.save(response);
                log.debug("User id={} confirmed Volunteer Response id={} and user={}", userService.getUserId(), response.getId(), response.getUser().getId());
            });
        else throw new ResponseAcceptException();
    }
    */

}
