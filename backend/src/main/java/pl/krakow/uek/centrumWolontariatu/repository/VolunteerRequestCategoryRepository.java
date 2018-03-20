package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestPicture;

import java.util.Optional;
import java.util.Set;

public interface VolunteerRequestCategoryRepository extends JpaRepository<VolunteerRequestCategory, String> {

}
