package pl.krakow.uek.centrumWolontariatu.web.rest.vm;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CarouselBannerVM {

    private String title;

    private String description;

    private String[] referenceToPicture;
}
