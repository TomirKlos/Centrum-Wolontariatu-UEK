package pl.krakow.uek.centrumWolontariatu.repository.DTO;

public interface InvitationToVolunteerRequestDTO {
    Long getId();

    UserIdDTO getUserInvited();

    String getDescription();

    long getTimestamp();

    byte getAccepted();

    VolunteerRequestDTOMin getVolunteerRequest();
}
