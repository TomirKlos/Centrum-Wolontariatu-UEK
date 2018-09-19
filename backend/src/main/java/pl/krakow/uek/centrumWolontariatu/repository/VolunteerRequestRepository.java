package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestCategory;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl.VolunteerRequestDTOImpl;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VolunteerRequestRepository extends JpaRepository<VolunteerRequest, Long>, JpaSpecificationExecutor<VolunteerRequest> {

    Optional<VolunteerRequest> findByUser(User user);

    List<VolunteerRequest> findAllByExpiredIs(byte expired);

    Optional<VolunteerRequest> findById(long id);

    Page<VolunteerRequestDTO> findAllBy(Pageable pageable);

    Page<VolunteerRequestDTO> findAllByUserId(Pageable pageable, long id);

    Page<VolunteerRequest> findAllByAcceptedIsAndCategoriesIn(Pageable pageable, byte accepted, Set<VolunteerRequestCategory> categories);

    Page<VolunteerRequest> findAllByAcceptedIsAndExpiredIsAndVolunteersAmountGreaterThan(byte accepted, byte expired, Integer volunteersAmount, Pageable pageable);

    Page<VolunteerRequest> findAllByAcceptedIsAndExpiredIsAndIsForStudentsIsAndVolunteersAmountGreaterThan(Pageable pageable, byte accepted, byte expired, byte isForStudents, Integer volunteersAmount);

    Page<VolunteerRequest> findAllByAcceptedIsAndExpiredIsAndIsForTutorsIsAndVolunteersAmountGreaterThan(Pageable pageable, byte accepted, byte expired, byte isForTutors, Integer volunteersAmount);

    Page<VolunteerRequest> findDistinctByAcceptedIsAndExpiredIsAndVolunteersAmountGreaterThanAndCategoriesIn(Pageable pageable, byte accepted, byte expired,  Integer volunteersAmount, Set<VolunteerRequestCategory> categories);

    Page<VolunteerRequest> findDistinctByAcceptedIsAndExpiredIsAndIsForStudentsIsAndVolunteersAmountGreaterThanAndCategoriesIn(Pageable pageable, byte accepted, byte expired, byte isForStudents, Integer volunteersAmount, Set<VolunteerRequestCategory> categories);

    Page<VolunteerRequest> findDistinctByAcceptedIsAndExpiredIsAndIsForTutorsIsAndVolunteersAmountGreaterThanAndCategoriesIn(Pageable pageable, byte accepted, byte expired, byte isForTutors, Integer volunteersAmount, Set<VolunteerRequestCategory> categories);

    Page<VolunteerRequest> findAllByAcceptedIsAndExpiredIsAndIsForStudentsOrIsForTutorsAndVolunteersAmountGreaterThanAndCategoriesIn(Pageable pageable, byte accepted, byte expired, byte isForStudents, byte isForTutors, Integer volunteersAmount, Set<VolunteerRequestCategory> categories);


}
