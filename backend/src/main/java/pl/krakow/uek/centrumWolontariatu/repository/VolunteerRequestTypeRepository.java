package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestType;

public interface VolunteerRequestTypeRepository extends JpaRepository<VolunteerRequestType, String> {
}
