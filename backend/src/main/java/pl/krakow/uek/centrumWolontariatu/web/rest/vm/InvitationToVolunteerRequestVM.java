package pl.krakow.uek.centrumWolontariatu.web.rest.vm;

import pl.krakow.uek.centrumWolontariatu.domain.User;

public class InvitationToVolunteerRequestVM {

    private String description;
    private Long volunteerRequestId;
    private Long volunteerAdId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getVolunteerRequestId() {
        return volunteerRequestId;
    }

    public void setVolunteerRequestId(Long volunteerRequestId) {
        this.volunteerRequestId = volunteerRequestId;
    }

    public Long getVolunteerAdId() {
        return volunteerAdId;
    }

    public void setVolunteerAdId(Long volunteerAdId) {
        this.volunteerAdId = volunteerAdId;
    }
}
