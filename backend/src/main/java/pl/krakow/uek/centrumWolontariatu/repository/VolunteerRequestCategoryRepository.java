package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestCategory;

public interface VolunteerRequestCategoryRepository extends JpaRepository<VolunteerRequestCategory, String> {

}
