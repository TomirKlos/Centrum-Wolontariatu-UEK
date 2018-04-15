package pl.krakow.uek.centrumWolontariatu.repository.DTO;

public interface ResponseVolunteerRequestDTO {
    Long getId();

    UserIdDTO getUser();

    String getDescription();

    long getTimestamp();

    byte getAccepted();

    byte getConfirmation();

    VolunteerRequestDTOMin getVolunteerRequest();

}

