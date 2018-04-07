package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;

public interface VolunteerRequestRepository extends JpaRepository<VolunteerRequest, Long>, JpaSpecificationExecutor<VolunteerRequest> {
    Page<VolunteerRequest> findAll(Pageable pageable);

    Page<VolunteerRequest> findAllBy(Pageable pageable);
}
