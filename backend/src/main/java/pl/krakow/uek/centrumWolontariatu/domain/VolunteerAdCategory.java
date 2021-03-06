package pl.krakow.uek.centrumWolontariatu.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "cw_volunteer_ad_category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VolunteerAdCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 70)
    @Id
    @Column(length = 70)
    private String name;
}
