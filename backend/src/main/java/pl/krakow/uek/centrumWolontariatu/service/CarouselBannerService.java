package pl.krakow.uek.centrumWolontariatu.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CacheEvict(value = "carouselBannerGetAll", allEntries = true)
    public void crateBanner(CarouselBannerVM carouselBannerVM){
        CarouselBanner carouselBanner = new CarouselBanner();
        carouselBanner.setDescription(carouselBannerVM.getDescription());
        carouselBanner.setReferenceToPicture(carouselBannerVM.getReferenceToPicture()[0]);
        carouselBanner.setTitle(carouselBannerVM.getTitle());
        carouselBanner.setUser(userService.getUserWithAuthorities().get());

        carouselBannerRepository.save(carouselBanner);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void saveNewBannersTransaction(CarouselBanner carouselBanner1, CarouselBanner carouselBanner2){
        this.carouselBannerRepository.save(carouselBanner1);
        this.carouselBannerRepository.save(carouselBanner2);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CacheEvict(value = "carouselBannerGetAll", allEntries = true)
    public void deleteBanner(Long id){
        carouselBannerRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CacheEvict(value = "carouselBannerGetAll", allEntries = true)
    public void makeUpInList(Long id){
        List<CarouselBanner> carouselBannerList = carouselBannerRepository.findAll();

        CarouselBanner lastIterationBanner = carouselBannerRepository.findFirstByIdGreaterThan(0L);
        for(CarouselBanner carouselBanner: carouselBannerList){
            if(lastIterationBanner==carouselBanner) continue;
            if(carouselBanner.getId()==id){
                exchangePlace(carouselBanner, lastIterationBanner);
            }
            lastIterationBanner = carouselBanner;
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CacheEvict(value = "carouselBannerGetAll", allEntries = true)
    public void makeDownInList(Long id){
        List<CarouselBanner> carouselBannerList = carouselBannerRepository.findAllByOrderByIdDesc();

        CarouselBanner lastIterationBanner = carouselBannerRepository.findFirstByOrderByIdDesc();
        for(CarouselBanner carouselBanner: carouselBannerList){
            if(lastIterationBanner==carouselBanner) continue;
            if(carouselBanner.getId()==id){
                exchangePlace(carouselBanner, lastIterationBanner);
            }
            lastIterationBanner = carouselBanner;
        }
    }

    private void exchangePlace(CarouselBanner carouselBannerOld, CarouselBanner carouselBannerNew){
        CarouselBanner carouselBannerTemp = createTempCarousel(carouselBannerNew);
        carouselBannerNew = exchangeVariablesInCarousel(carouselBannerNew, carouselBannerOld);
        carouselBannerOld = exchangeVariablesInCarousel(carouselBannerOld, carouselBannerTemp);
        this.saveNewBannersTransaction(carouselBannerNew, carouselBannerOld);

    }

    private CarouselBanner exchangeVariablesInCarousel(CarouselBanner carouselBannerNew, CarouselBanner carouselBannerOld){
        carouselBannerNew.setUser(carouselBannerOld.getUser());
        carouselBannerNew.setTitle(carouselBannerOld.getTitle());
        carouselBannerNew.setReferenceToPicture(carouselBannerOld.getReferenceToPicture());
        carouselBannerNew.setDescription(carouselBannerOld.getDescription());
        return carouselBannerNew;
    }

    private CarouselBanner createTempCarousel(CarouselBanner carouselBanner){
        CarouselBanner carouselBannerTemp = new CarouselBanner();
        carouselBannerTemp.setUser(carouselBanner.getUser());
        carouselBannerTemp.setTitle(carouselBanner.getTitle());
        carouselBannerTemp.setReferenceToPicture(carouselBanner.getReferenceToPicture());
        carouselBannerTemp.setDescription(carouselBanner.getDescription());
        return carouselBannerTemp;
    }
}
