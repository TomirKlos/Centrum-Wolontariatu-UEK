package pl.krakow.uek.centrumWolontariatu.repository.DTO;

public interface VolunteerRequestDTO {
    Long getId();
    String getDescription();
    String getTitle();
    String getVolunteersAmount();
    UserIdDTO getUser();
}


