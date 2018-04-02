package pl.krakow.uek.centrumWolontariatu.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "cw_volunteer_request_pictures")
@EqualsAndHashCode
public class VolunteerRequestPicture implements Serializable {
    @Getter
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne
    @JoinColumn(name="volunteer_request_id", nullable=false)
    @Getter
    @Setter
    @JsonBackReference
    private VolunteerRequest volunteerRequest;

    @Column(name = "reference_to_picture")
    @Lob
    @Getter
    @Setter
    private String referenceToPicture;


}
