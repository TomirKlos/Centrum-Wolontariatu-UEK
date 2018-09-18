package pl.krakow.uek.centrumWolontariatu.repository.DTO;

import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdPicture;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdType;

import java.util.Set;

public interface VolunteerAdDTO {

    Long getId();

    String getDescription();

    String getTitle();

    long getTimestamp();

    long getExpirationDate();

    byte getExpired();

    byte getAccepted();

    UserIdAuthorityDTO getUser();

    Set<VolunteerAdCategory> getCategories();

    Set<VolunteerAdType> getTypes();

    Set<VolunteerAdPicture> getPictures();
}
