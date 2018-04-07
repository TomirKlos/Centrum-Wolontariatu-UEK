package pl.krakow.uek.centrumWolontariatu.web.rest.vm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@ToString
public class VolunteerRequestVM {

    @NotBlank
    private String description;

    @NotBlank
    private String title;

    private int volunteersAmount;

    @NotNull
    private boolean isForStudents;

    @NotNull
    private boolean isForTutors;

    private Set<String> categories;

    private Set<String> types;

    @NotNull
    private long expirationDate;

    private String[] images;
}
