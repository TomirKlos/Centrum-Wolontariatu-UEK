package pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl;

import lombok.Getter;
import lombok.Setter;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdPicture;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdType;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.UserIdDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerAdDTO;

import java.util.Set;

@Getter
@Setter
public class VolunteerAdDTOImpl implements VolunteerAdDTO {
    Long id;

    String description;

    String title;

    long timestamp;

    long expirationDate;

    byte expired;

    byte accepted;

    UserIdDTO user;

    Set<VolunteerAdCategory> categories;

    Set<VolunteerAdType> types;

    Set<VolunteerAdPicture> pictures;
}
