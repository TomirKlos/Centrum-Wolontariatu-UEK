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

import java.nio.charset.StandardCharsets;
import java.util.List;

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

    public List<VolunteerCertificate> findAllUncertified() {
        return volunteerCertificateRepository.findAllByCertifiedIsFalse();
    }

    public VolunteerCertificate sendVolunteerToCertification(ResponseVolunteerRequest responseVolunteerRequest){
        VolunteerCertificate volunteerCertificate = new VolunteerCertificate();
        volunteerCertificate.setFeedback(responseVolunteerRequest.getFeedback());
        volunteerCertificate.setUser(responseVolunteerRequest.getUser());
        volunteerCertificate.setVolunteerRequest(responseVolunteerRequest.getVolunteerRequest());

        return volunteerCertificateRepository.save(volunteerCertificate);
    }

    public byte[] generateCSV(List<VolunteerCertificate> volunteerCertificateList){
        StringBuilder stringBuilder = new StringBuilder();
        prepareCsv(stringBuilder);
        volunteerCertificateList.forEach(volunteerCertificate -> {
            setVolunteerCertificated(volunteerCertificate);
            addRowToCsv(stringBuilder, volunteerCertificate);
        });

        return stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] generateSingleCsv(long id){
        VolunteerCertificate volunteerCertificate = volunteerCertificateRepository.getOne(id);

        StringBuilder stringBuilder = new StringBuilder();
        prepareCsv(stringBuilder);

        setVolunteerCertificated(volunteerCertificate);

        addRowToCsv(stringBuilder, volunteerCertificate);

        return stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void setVolunteerCertificated(VolunteerCertificate volunteerCertificated){
        volunteerCertificated.setCertified(true);
        volunteerCertificateRepository.save(volunteerCertificated);
    }

    private StringBuilder prepareCsv(StringBuilder stringBuilder){
        String DELIMITER = ", ";

        stringBuilder.append("email wolontariusza");
        stringBuilder.append(DELIMITER);
        stringBuilder.append("imie wolontariusza");
        stringBuilder.append(DELIMITER);
        stringBuilder.append("nazwisko wolontariusza");
        stringBuilder.append(DELIMITER);
        stringBuilder.append("id wolontariatu");
        stringBuilder.append(DELIMITER);
        stringBuilder.append("email tworcy wolontariatu");
        stringBuilder.append(DELIMITER);
        stringBuilder.append("imie tworcy wolontariatu");
        stringBuilder.append(DELIMITER);
        stringBuilder.append("nazwisko tworcy wolontariatu");
        stringBuilder.append(DELIMITER);
        stringBuilder.append("opinia o wolontariuszu");
        stringBuilder.append(System.getProperty("line.separator"));

        return stringBuilder;
    }

    private StringBuilder addRowToCsv(StringBuilder stringBuilder, VolunteerCertificate volunteerCertificate){
        String DELIMITER = ", ";

        stringBuilder.append(volunteerCertificate.getUser().getEmail());
        stringBuilder.append(DELIMITER);
        stringBuilder.append(volunteerCertificate.getUser().getFirstName());
        stringBuilder.append(DELIMITER);
        stringBuilder.append(volunteerCertificate.getUser().getLastName());
        stringBuilder.append(DELIMITER);
        stringBuilder.append(volunteerCertificate.getVolunteerRequest().getId());
        stringBuilder.append(DELIMITER);
        stringBuilder.append(volunteerCertificate.getVolunteerRequest().getUser().getEmail());
        stringBuilder.append(DELIMITER);
        stringBuilder.append(volunteerCertificate.getVolunteerRequest().getUser().getFirstName());
        stringBuilder.append(DELIMITER);
        stringBuilder.append(volunteerCertificate.getVolunteerRequest().getUser().getLastName());
        stringBuilder.append(DELIMITER);
        stringBuilder.append(volunteerCertificate.getFeedback());
        stringBuilder.append(System.getProperty("line.separator"));

        return stringBuilder;
    }




}
