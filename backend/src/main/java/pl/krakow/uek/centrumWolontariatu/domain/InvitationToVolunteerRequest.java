package pl.krakow.uek.centrumWolontariatu.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cw_invitation_to_volunteer_request")
@Getter
@Setter
public class InvitationToVolunteerRequest {
    private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id", nullable = false)
        private User userInvited;

        @Column(name = "description")
        @Lob
        private String description;

        @Column(name= "timestamp")
        private long timestamp;

        @Column(name = "accepted")
        private byte accepted = 0;

        @Column(name = "seen")
        private byte seen = 0;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "volunteer_request_id", nullable = false)
        private VolunteerRequest volunteerRequest;

}
