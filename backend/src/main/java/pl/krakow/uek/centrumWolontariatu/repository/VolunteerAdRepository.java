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

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VolunteerAdRepository extends JpaRepository<VolunteerAd, Long>, JpaSpecificationExecutor<VolunteerAd> {

    Optional<VolunteerAd> findById(long id);

    Page<VolunteerAdDTO> findAllBy(Pageable pageable);

    List<VolunteerAd> findAllByExpiredIs(byte expired);

    Page<VolunteerAdDTO> findAllByUserId(Pageable pageable, long id);

    Page<VolunteerAd> findAllByAcceptedIsAndExpiredIs(byte accepted, byte expired, Pageable pageable);

    Page<VolunteerAd> findAllByAcceptedIsAndExpiredIsAndCategoriesIn(Pageable pageable, byte accepted, byte expired, Set<VolunteerAdCategory> categories);

}
