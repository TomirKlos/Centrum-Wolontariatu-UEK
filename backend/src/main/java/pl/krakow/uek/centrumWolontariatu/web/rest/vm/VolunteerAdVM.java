package pl.krakow.uek.centrumWolontariatu.web.rest.vm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class VolunteerAdVM {

    private String description;

    private String title;

    private Set<String> categories;

    private Set<String> types;

    private long expirationDate;
}
