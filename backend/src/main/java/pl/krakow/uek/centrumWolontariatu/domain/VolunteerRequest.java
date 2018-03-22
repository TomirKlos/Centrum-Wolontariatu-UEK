package pl.krakow.uek.centrumWolontariatu.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cw_volunteer_requests")
@Getter
@Setter
public class VolunteerRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "volunteers_amount")
    private int volunteersAmount;

    @Column(name= "timestamp")
    private long timestamp;

    @Column(name = "accepted")
    private boolean accepted;

    @Column(name = "expired")
    private boolean expired;

    @Column(name = "is_for_students")
    private boolean isForStudents;

    @Column(name = "is_for_tutors")
    private boolean isForTutors;

    @ManyToMany
    @JoinTable(
        name = "cw_volunteer_requests_categories",
        joinColumns = {@JoinColumn(name = "volunteerRequest_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "category_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    private Set<VolunteerRequestCategory> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "cw_volunteer_requests_types",
        joinColumns = {@JoinColumn(name = "volunteerRequest_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "type_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    private Set<VolunteerRequestType> volunteerRequestTypes = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolunteerRequest that = (VolunteerRequest) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, user);
    }
}
