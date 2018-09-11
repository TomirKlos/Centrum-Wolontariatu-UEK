package pl.krakow.uek.centrumWolontariatu.web.rest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerCertificate;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerCertificateService;

@RestController
@RequestMapping("/api/certificate")
public class VolunteerCertificateController {

    private final VolunteerCertificateService volunteerCertificateService;

    public VolunteerCertificateController(VolunteerCertificateService volunteerCertificateService) {
        this.volunteerCertificateService = volunteerCertificateService;
    }

    @GetMapping
    public Page<VolunteerCertificate> getAllCertificates(Pageable pageable){
        return volunteerCertificateService.findAll(pageable);
    }

    @GetMapping("/uncertified")
    public Page<VolunteerCertificate> getAllUncertified(Pageable pageable){
        return volunteerCertificateService.findAllUncertified(pageable);
    }

    @GetMapping("/certified")
    public Page<VolunteerCertificate> getAllCertified(Pageable pageable){
        return volunteerCertificateService.findAllCertified(pageable);
    }

}
