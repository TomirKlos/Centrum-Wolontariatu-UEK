package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.CarouselBanner;

import java.util.List;

public interface CarouselBannerRepository extends JpaRepository<CarouselBanner, Long> {

    CarouselBanner findFirstByIdGreaterThan(Long id);
    CarouselBanner findFirstByOrderByIdDesc();

    List<CarouselBanner> findAllByOrderByIdDesc();

}
