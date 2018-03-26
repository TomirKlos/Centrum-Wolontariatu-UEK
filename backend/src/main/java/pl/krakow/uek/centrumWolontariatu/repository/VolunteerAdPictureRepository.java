package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdPicture;

import java.util.Optional;

public interface VolunteerAdPictureRepository extends JpaRepository<VolunteerAdPicture, String> {
    Optional<VolunteerAdPicture> findByVolunteerAdId(long id);
}
