package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.krakow.uek.centrumWolontariatu.domain.ResponseVolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.ResponseVolunteerRequestDTO;

public interface ResponseVolunteerRequestRepository extends JpaRepository<ResponseVolunteerRequest, Long>, JpaSpecificationExecutor<ResponseVolunteerRequest> {
    Page<ResponseVolunteerRequestDTO> findAllByVolunteerRequestId(Pageable pageable, long id);
}
