package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdType;

public interface VolunteerAdTypeRepository extends JpaRepository<VolunteerAdType, String> {
}
