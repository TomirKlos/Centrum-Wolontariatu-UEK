package pl.krakow.uek.centrumWolontariatu.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cw_volunteer_requests")
@Getter
@Setter
@Indexed
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
    @Field(termVector = TermVector.YES)
    private String description;

    @Column(name = "title")
    @Field(termVector = TermVector.YES)
    private String title;

    @Column(name = "volunteers_amount")
    private int volunteersAmount;

    @Column(name= "timestamp")
    private long timestamp;

    //accepted set to byte to provide query search in RSQL which not support boolean type.
    @Column(name = "accepted")
    @Field(termVector = TermVector.YES)
    private byte accepted;

    @Column(name = "expired")
    private byte expired;

    @Column(name = "expiration_date")
    private long expirationDate;

    @Column(name = "is_for_students")
    private byte isForStudents;

    @Column(name = "is_for_tutors")
    private byte isForTutors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "cw_volunteer_requests_categories",
        joinColumns = {@JoinColumn(name = "volunteerRequest_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "category_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    private Set<VolunteerRequestCategory> categories = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "cw_volunteer_requests_types",
        joinColumns = {@JoinColumn(name = "volunteerRequest_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "type_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    private Set<VolunteerRequestType> volunteerRequestTypes = new HashSet<>();

    @OneToMany(mappedBy="volunteerRequest",fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<VolunteerRequestPicture> pictures;


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
