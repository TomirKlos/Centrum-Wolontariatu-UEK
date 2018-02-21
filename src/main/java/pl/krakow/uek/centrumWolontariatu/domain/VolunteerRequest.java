package pl.krakow.uek.centrumWolontariatu.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by MSI DRAGON on 2017-12-19.
 */
@Entity
public class VolunteerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String description;

    private int numberOfRequestedVolunteers;

    private User user;

    public VolunteerRequest() {
    }

    public VolunteerRequest(String description, int numberOfRequestedVolunteers, User user) {
        this.description = description;
        this.numberOfRequestedVolunteers = numberOfRequestedVolunteers;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getNumberOfRequestedVolunteers() {
        return numberOfRequestedVolunteers;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolunteerRequest that = (VolunteerRequest) o;
        return id == that.id &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, user);
    }
}
