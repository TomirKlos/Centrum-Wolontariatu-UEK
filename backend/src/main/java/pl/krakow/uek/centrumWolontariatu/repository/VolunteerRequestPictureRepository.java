package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestPicture;

import java.util.Optional;

public interface VolunteerRequestPictureRepository extends JpaRepository<VolunteerRequestPicture, Long> {
    Optional<VolunteerRequestPicture> findByVolunteerRequestId(long id);

}
