package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;

import java.util.Optional;

public interface VolunteerRequestRepository extends JpaRepository<VolunteerRequest, Long> {

    Optional<VolunteerRequest> findByUser(User user);
}
