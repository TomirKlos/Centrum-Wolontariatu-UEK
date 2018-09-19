package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.krakow.uek.centrumWolontariatu.domain.*;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerAdDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VolunteerAdRepository extends JpaRepository<VolunteerAd, Long>, JpaSpecificationExecutor<VolunteerAd> {

    Optional<VolunteerAd> findById(long id);

    Page<VolunteerAdDTO> findAllBy(Pageable pageable);

    List<VolunteerAd> findAllByExpiredIs(byte expired);

    List<VolunteerAd> findAllByUserAuthoritiesIn(Set<Authority> authorities);

    Page<VolunteerAdDTO> findAllByUserId(Pageable pageable, long id);

    Page<VolunteerAd> findAllByAcceptedIsAndExpiredIs(Pageable pageable, byte accepted, byte expired);

    Page<VolunteerAd> findDistinctByAcceptedIsAndExpiredIsAndUserAuthoritiesIn( Pageable pageable, byte accepted, byte expired, Set<Authority> authorities);

    Page<VolunteerAd> findAllByAcceptedIsAndExpiredIsAndCategoriesIn(Pageable pageable, byte accepted, byte expired, Set<VolunteerAdCategory> categories);

    Page<VolunteerAd> findDistinctByAcceptedIsAndExpiredIsAndCategoriesInAndUserAuthoritiesIn(Pageable pageable, byte accepted, byte expired, Set<VolunteerAdCategory> categories, Set<Authority> authorities);

}
