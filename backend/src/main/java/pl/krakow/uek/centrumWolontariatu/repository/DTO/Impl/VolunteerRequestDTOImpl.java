package pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl;

import lombok.Getter;
import lombok.Setter;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestType;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.UserIdDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;

import java.util.Set;

@Setter
@Getter
public class VolunteerRequestDTOImpl implements VolunteerRequestDTO {
    Long id;

    String description;

    String title;

    int volunteersAmount;

    long timestamp;

    long expirationDate;

    byte expired;

    byte isForStudents;

    byte isForTutors;

    UserIdDTO user;

    Set<VolunteerRequestCategory> categories;

    Set<VolunteerRequestType> volunteerRequestTypes;

}
