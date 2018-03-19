package pl.krakow.uek.centrumWolontariatu.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cw_volunteer_requests")
@Getter @Setter
public class VolunteerRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "volunteers_amount")
    private int volunteersAmount;


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
