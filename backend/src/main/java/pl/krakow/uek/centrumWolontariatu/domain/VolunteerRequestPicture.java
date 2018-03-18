package pl.krakow.uek.centrumWolontariatu.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cw_volunteer_request_pictures")
public class VolunteerRequestPicture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="reference_to_picture")
    private String[] referenceToPicture;

    @Column(name="original_picture_name")
    private String[] originalpicturename;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId
    private VolunteerRequest volunteerRequest;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String[] getReferenceToPicture() {
        return referenceToPicture;
    }

    public void setReferenceToPicture(String[] referenceToPicture) {
        this.referenceToPicture = referenceToPicture;
    }

    public String[] getOriginalpicturename() {
        return originalpicturename;
    }

    public void setOriginalpicturename(String[] originalpicturename) {
        this.originalpicturename = originalpicturename;
    }

    public VolunteerRequest getVolunteerRequest() {
        return volunteerRequest;
    }

    public void setVolunteerRequest(VolunteerRequest volunteerRequest) {
        this.volunteerRequest = volunteerRequest;
    }
}
