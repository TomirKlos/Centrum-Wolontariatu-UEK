package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerCertificate;

import java.util.List;

public interface VolunteerCertificateRepository extends JpaRepository<VolunteerCertificate, Long> {

    List<VolunteerCertificate> findAllByCertifiedIsFalse();

}
