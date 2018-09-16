package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.domain.CarouselBanner;
import pl.krakow.uek.centrumWolontariatu.service.CarouselBannerService;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.CarouselBannerVM;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/carouselbanner/")
public class CarouselBannerController {

    private final CarouselBannerService carouselBannerService;

    public CarouselBannerController(CarouselBannerService carouselBannerService) {
        this.carouselBannerService = carouselBannerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBanner(@RequestBody CarouselBannerVM carouselBannerVM) {
        carouselBannerService.crateBanner(carouselBannerVM);
    }

    @PostMapping(path = "picture", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public Set<String> addPicture(@RequestBody MultipartFile[] file) {
        return carouselBannerService.addPicturesToBanner(file);
    }

    @GetMapping()
    @ResponseBody
    public List<CarouselBanner> getAll() {
        return carouselBannerService.getAll();
    }

    @DeleteMapping("{id}")
    public void deleteBanner(@PathVariable Long id){
        carouselBannerService.deleteBanner(id);
    }
}
