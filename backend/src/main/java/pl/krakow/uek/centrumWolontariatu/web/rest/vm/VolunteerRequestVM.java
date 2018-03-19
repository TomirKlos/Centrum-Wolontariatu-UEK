package pl.krakow.uek.centrumWolontariatu.web.rest.vm;

public class VolunteerRequestVM {

    private String description;

    private String title;

    private int volunteersAmount;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVolunteersAmount() {
        return volunteersAmount;
    }

    public void setVolunteersAmount(int volunteersAmount) {
        this.volunteersAmount = volunteersAmount;
    }

    @Override
    public String toString() {
        return "VolunteerRequestVM{" +
            "description='" + description + '\'' +
            ", title='" + title + '\'' +
            ", volunteersAmount=" + volunteersAmount +
            '}';
    }
}
