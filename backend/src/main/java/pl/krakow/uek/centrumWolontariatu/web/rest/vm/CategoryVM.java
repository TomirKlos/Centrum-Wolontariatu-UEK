package pl.krakow.uek.centrumWolontariatu.web.rest.vm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class CategoryVM {
    @NotBlank
    String categoryName;
}
