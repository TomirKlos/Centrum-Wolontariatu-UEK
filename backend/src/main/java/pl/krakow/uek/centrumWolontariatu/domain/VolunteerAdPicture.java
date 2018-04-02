package pl.krakow.uek.centrumWolontariatu.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;

@Entity
@Table(name = "cw_volunteer_ad_pictures")
@Getter
@Setter
@EqualsAndHashCode
public class VolunteerAdPicture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne
    @JoinColumn(name="volunteer_ad_id", nullable=false)
    @Getter
    @Setter
    @JsonBackReference
    private VolunteerAd volunteerAd;

    @Column(name = "reference_to_picture")
    @Lob
    @Getter
    @Setter
    private String referenceToPicture;
}
