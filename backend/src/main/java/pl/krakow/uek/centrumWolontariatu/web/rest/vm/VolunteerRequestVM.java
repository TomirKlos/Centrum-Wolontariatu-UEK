package pl.krakow.uek.centrumWolontariatu.web.rest.vm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@ToString
public class VolunteerRequestVM {

    private String description;

    private String title;

    private int volunteersAmount;

    private boolean isForStudents;

    private boolean isForTutors;

    private Set<String> categories;

    private Set<String> types;

    private long expirationDate;

    private String[] images;


}
