package pl.krakow.uek.centrumWolontariatu.service.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krakow.uek.centrumWolontariatu.domain.UserProfile;
import pl.krakow.uek.centrumWolontariatu.domain.repository.UserProfileRepository;
import pl.krakow.uek.centrumWolontariatu.service.UserProfileService;

/**
 * Created by MSI DRAGON on 2017-12-11.
 */


@Service("userProfileService")
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    public UserProfile findById(int id) {
        return userProfileRepository.findById(id);
    }

    public UserProfile findByType(String type){
        return userProfileRepository.findByType(type);
    }

    public List<UserProfile> findAll() {return userProfileRepository.findAll();}
}
