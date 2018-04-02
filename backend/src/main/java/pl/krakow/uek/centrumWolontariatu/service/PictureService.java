package pl.krakow.uek.centrumWolontariatu.service;

import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAd;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAdPicture;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestPicture;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerAdPictureRepository;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestPictureRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class PictureService<T> {

    private final VolunteerRequestPictureRepository volunteerRequestPictureRepository;
    private final VolunteerAdPictureRepository volunteerAdPictureRepository;

    public PictureService(VolunteerRequestPictureRepository volunteerRequestPictureRepository, VolunteerAdPictureRepository volunteerAdPictureRepository){
        this.volunteerRequestPictureRepository = volunteerRequestPictureRepository;
        this.volunteerAdPictureRepository = volunteerAdPictureRepository;
    }

    public Set<? extends Object> addPicturesToDatabase(String[] references, T t){
        if(t instanceof VolunteerRequest) {
            Set<VolunteerRequestPicture> set = new HashSet<>();
            if (references.length>0) {
                for(String reference: references){
                    VolunteerRequestPicture volunteerRequestPicture = new VolunteerRequestPicture();
                    volunteerRequestPicture.setReferenceToPicture(reference);
                    volunteerRequestPicture.setVolunteerRequest((VolunteerRequest) t);
                    volunteerRequestPictureRepository.save(volunteerRequestPicture);
                    set.add(volunteerRequestPicture);
                }
            }
            return set;
        }else if(t instanceof VolunteerAd){
            Set<VolunteerAdPicture> set = new HashSet<>();
            if (references.length>0) {
                for(String reference: references){
                    VolunteerAdPicture volunteerAdPicture = new VolunteerAdPicture();
                    volunteerAdPicture.setReferenceToPicture(reference);
                    volunteerAdPicture.setVolunteerAd((VolunteerAd) t);
                    volunteerAdPictureRepository.save(volunteerAdPicture);
                    set.add(volunteerAdPicture);
                }
            }
            return set;
        }else return null;
    }

}
