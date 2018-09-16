package pl.krakow.uek.centrumWolontariatu.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.krakow.uek.centrumWolontariatu.domain.CarouselBanner;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.repository.CarouselBannerRepository;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.CarouselBannerVM;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static pl.krakow.uek.centrumWolontariatu.configuration.constant.UserConstant.UPLOADED_FOLDER_CAROUSEL_BANNER;
import static pl.krakow.uek.centrumWolontariatu.configuration.constant.UserConstant.UPLOADED_FOLDER_REQUESTS;

@Service
public class CarouselBannerService {

    private final CarouselBannerRepository carouselBannerRepository;
    private final UserService userService;

    public CarouselBannerService(CarouselBannerRepository carouselBannerRepository, UserService userService) {
        this.carouselBannerRepository = carouselBannerRepository;
        this.userService = userService;
    }

    @Cacheable(value = "carouselBannerGetAll")
    public List<CarouselBanner> getAll(){
        return carouselBannerRepository.findAll();
    }
    @CacheEvict(value = "carouselBannerGetAll", allEntries = true)
    public void crateBanner(CarouselBannerVM carouselBannerVM){
        CarouselBanner carouselBanner = new CarouselBanner();
        carouselBanner.setDescription(carouselBannerVM.getDescription());
        carouselBanner.setReferenceToPicture(carouselBannerVM.getReferenceToPicture()[0]);
        carouselBanner.setTitle(carouselBannerVM.getTitle());
        carouselBanner.setUser(userService.getUserWithAuthorities().get());

        carouselBannerRepository.save(carouselBanner);
    }

    public Set<String> addPicturesToBanner(MultipartFile[] file) {
        for (MultipartFile multipartFile : file) {
            if (!multipartFile.getContentType().matches("^(image).*$")) {
                throw new BadRequestAlertException("Not allowed format file", "bannerManagement", "notallowedformatfile");
            }
        }
        User user = userService.getUserWithAuthorities().get();
        HashSet<String> hashPicturesWithReferences = new HashSet<>();
        for (MultipartFile multipartFile : file) {
            try {
                byte[] bytes = multipartFile.getBytes();

                String fileTypes[] = multipartFile.getOriginalFilename().split("\\.");
                String fileType = fileTypes[fileTypes.length - 1];

                MessageDigest salt = MessageDigest.getInstance("SHA-256");
                salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
                String hexString = DatatypeConverter.printHexBinary(salt.digest());

                Path path = Paths.get(UPLOADED_FOLDER_CAROUSEL_BANNER + hexString + "." + fileType);
                Files.write(path, bytes);

                hashPicturesWithReferences.add(hexString + "." + fileType);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return hashPicturesWithReferences;
    }
    @CacheEvict(value = "carouselBannerGetAll", allEntries = true)
    public void deleteBanner(Long id){
        carouselBannerRepository.deleteById(id);
    }
}
