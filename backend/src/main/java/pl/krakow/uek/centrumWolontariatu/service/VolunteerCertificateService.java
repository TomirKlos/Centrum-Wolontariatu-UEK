package pl.krakow.uek.centrumWolontariatu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.ResponseVolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerCertificate;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerCertificateRepository;
import pl.krakow.uek.centrumWolontariatu.web.rest.AuthenticationController;

@Service
public class VolunteerCertificateService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;
    private final VolunteerCertificateRepository volunteerCertificateRepository;

    public VolunteerCertificateService(UserService userService, VolunteerCertificateRepository volunteerCertificateRepository) {
        this.userService = userService;
        this.volunteerCertificateRepository = volunteerCertificateRepository;
    }

    public Page<VolunteerCertificate> findAll(Pageable pageable) {
        return volunteerCertificateRepository.findAll(pageable);
    }

    public Page<VolunteerCertificate> findAllUncertified(Pageable pageable) {
        return volunteerCertificateRepository.findAllByCertifiedIsFalse(pageable);
    }

    public Page<VolunteerCertificate> findAllCertified(Pageable pageable) {
        return volunteerCertificateRepository.findAllByCertifiedIsTrue(pageable);
    }

    public VolunteerCertificate sendVolunteerToCertification(ResponseVolunteerRequest responseVolunteerRequest){
        VolunteerCertificate volunteerCertificate = new VolunteerCertificate();
        volunteerCertificate.setFeedback(responseVolunteerRequest.getFeedback());
        volunteerCertificate.setUser(responseVolunteerRequest.getUser());
        volunteerCertificate.setVolunteerRequest(responseVolunteerRequest.getVolunteerRequest());

        return volunteerCertificateRepository.save(volunteerCertificate);
    }




}
