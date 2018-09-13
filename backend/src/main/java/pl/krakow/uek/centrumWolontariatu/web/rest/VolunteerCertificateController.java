package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerCertificate;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerCertificateService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    @GetMapping("/uncertified/download")
    public HttpEntity<byte[]> createCSV() {

        byte[] documentBody = volunteerCertificateService.generateCSV(volunteerCertificateService.findAllUncertified());

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = localDateTime.format(formatter);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.set(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=" + formatDateTime + "WolontariuszeDoCertyfikacji.csv");
        header.setContentLength(documentBody.length);

        return new HttpEntity<>(documentBody, header);
    }

    @GetMapping("/uncertified/download/{id}")
    public HttpEntity<byte[]> createSingleCSV(@PathVariable long id) {

        byte[] documentBody = volunteerCertificateService.generateSingleCsv(id);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.set(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=" + "WolontariuszeDoCertyfikacji.csv");
        header.setContentLength(documentBody.length);

        return new HttpEntity<>(documentBody, header);
    }

}
