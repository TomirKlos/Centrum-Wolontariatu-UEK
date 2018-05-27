package pl.krakow.uek.centrumWolontariatu.web.rest.vm;

public class ResponseVolunteerRequestVM {

    private String description;
    private long volunteerRequestId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getVolunteerRequestId() {
        return volunteerRequestId;
    }

    public void setVolunteerRequestId(long volunteerRequestId) {
        this.volunteerRequestId = volunteerRequestId;
    }
}
