package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;

import java.util.Optional;

public interface VolunteerRequestRepository extends JpaRepository<VolunteerRequest, Long>, JpaSpecificationExecutor<VolunteerRequest> {

    Page<VolunteerRequest> findAll(Pageable pageable);

    Page<VolunteerRequest> findAllByCreatedBy(Pageable pageable, User user);

    Page<VolunteerRequestDTO> findAllBy(Pageable pageable);

   // Page<VolunteerRequestDTO> findAllByUserId(Pageable pageable, long id);

    Optional<VolunteerRequest> getById(long id);
}
