package pl.krakow.uek.centrumWolontariatu.domain;

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
@Table(name = "cw_volunteer_ads")
@Getter
@Setter
@Indexed
public class VolunteerAd implements Serializable {

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

    @ManyToMany
    @JoinTable(
        name = "cw_volunteer_ads_categories",
        joinColumns = {@JoinColumn(name = "volunteerAd_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "category_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    private Set<VolunteerAdCategory> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "cw_volunteer_ads_types",
        joinColumns = {@JoinColumn(name = "volunteerAd_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "type_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    private Set<VolunteerAdType> types = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolunteerAd that = (VolunteerAd) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, user);
    }
}
