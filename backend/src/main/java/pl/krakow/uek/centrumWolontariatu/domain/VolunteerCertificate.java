package pl.krakow.uek.centrumWolontariatu.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cw_volunteer_certificate")
@Getter
@Setter
public class VolunteerCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean certified = false;

    private String feedback;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="volunteer_request_id", nullable=false)
    private VolunteerRequest volunteerRequest;
}
