package pl.krakow.uek.centrumWolontariatu.repository.DTO;

import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestType;

import java.util.Set;

public interface VolunteerRequestDTO {
    Long getId();

    String getDescription();

    String getTitle();

    String getVolunteersAmount();

    UserIdDTO getUser();

    Set<VolunteerRequestCategory> getCategories();

    Set<VolunteerRequestType> getVolunteerRequestTypes();

}


