package pl.krakow.uek.centrumWolontariatu.domain;

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
    private Long id;

    @Column(name = "reference_to_picture")
    @Lob
    private HashMap<String, String> referenceToPicture;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "volunteerAd_id")
    private VolunteerAd volunteerAd;
}
