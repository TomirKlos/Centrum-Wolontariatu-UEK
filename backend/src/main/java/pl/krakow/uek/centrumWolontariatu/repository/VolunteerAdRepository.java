package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAd;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestCategory;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerAdDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;

import java.util.Optional;
import java.util.Set;

public interface VolunteerAdRepository extends JpaRepository<VolunteerAd, Long>, JpaSpecificationExecutor<VolunteerAd> {

    Optional<VolunteerAd> findById(long id);

    Page<VolunteerAdDTO> findAllBy(Pageable pageable);

    Page<VolunteerAdDTO> findAllByUserId(Pageable pageable, long id);

    Page<VolunteerAd> findAllByAcceptedIs(byte accepted, Pageable pageable);

    Page<VolunteerAd> findAllByAcceptedIsAndCategoriesIn(Pageable pageable, byte accepted, Set<VolunteerAdCategory> categories);

}
