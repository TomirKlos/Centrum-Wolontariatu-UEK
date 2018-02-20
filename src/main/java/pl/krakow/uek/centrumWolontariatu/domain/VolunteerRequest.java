package pl.krakow.uek.centrumWolontariatu.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

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
}
