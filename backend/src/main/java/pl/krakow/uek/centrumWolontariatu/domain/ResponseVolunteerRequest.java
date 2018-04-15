package pl.krakow.uek.centrumWolontariatu.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cw_response_volunteer_request")
@Getter
@Setter
public class ResponseVolunteerRequest implements Serializable {
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

    @Column(name= "timestamp")
    private long timestamp;

    @Column(name = "accepted")
    private byte accepted = 0;

    @Column(name = "confirmation")
    private byte confirmation = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "volunteer_request_id", nullable = false)
    private VolunteerRequest volunteerRequest;
}
