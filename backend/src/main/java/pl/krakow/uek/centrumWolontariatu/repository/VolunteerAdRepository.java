package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAd;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerAdDTO;

import java.util.Optional;

public interface VolunteerAdRepository extends JpaRepository<VolunteerAd, Long>, JpaSpecificationExecutor<VolunteerAd> {

    Optional<VolunteerAd> findById(long id);

    Page<VolunteerAdDTO> findAllBy(Pageable pageable);
}
