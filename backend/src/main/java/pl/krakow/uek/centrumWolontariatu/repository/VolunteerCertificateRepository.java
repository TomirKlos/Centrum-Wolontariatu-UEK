package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerCertificate;

public interface VolunteerCertificateRepository extends JpaRepository<VolunteerCertificate, String> {

    Page<VolunteerCertificate> findAllByCertifiedIsFalse(Pageable pageable);
    Page<VolunteerCertificate> findAllByCertifiedIsTrue(Pageable pageable);

}
