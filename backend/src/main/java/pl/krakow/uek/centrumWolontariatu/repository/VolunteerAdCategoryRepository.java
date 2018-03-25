package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdCategory;

public interface VolunteerAdCategoryRepository extends JpaRepository<VolunteerAdCategory, String> {
}
