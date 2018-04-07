package pl.krakow.uek.centrumWolontariatu.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cw_volunteer_requests")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Indexed
public class VolunteerRequest extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    @Lob
    @Field(termVector = TermVector.YES)
    private String description;

    @Column(name = "title")
    @Field(termVector = TermVector.YES)
    private String title;

    @Column(name = "volunteers_amount")
    private int volunteersAmount;

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

    @OneToMany(mappedBy = "volunteerRequest", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<VolunteerRequestPicture> pictures;

}
