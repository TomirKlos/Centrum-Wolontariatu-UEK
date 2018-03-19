package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;

import java.util.List;
import java.util.Optional;

public interface VolunteerRequestRepository extends JpaRepository<VolunteerRequest, Long> {

    Optional<VolunteerRequest> findByUser(User user);
    List<VolunteerRequest> findFirst2ByOrderByIdDesc();
    Page<VolunteerRequest> findAll(Pageable pageable);
}
