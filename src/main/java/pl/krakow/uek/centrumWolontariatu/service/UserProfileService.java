package pl.krakow.uek.centrumWolontariatu.service;

import pl.krakow.uek.centrumWolontariatu.domain.UserProfile;

import java.util.List;

/**
 * Created by MSI DRAGON on 2017-12-11.
 */


public interface UserProfileService {

    UserProfile findById(int id);

    UserProfile findByType(String type);

    List<UserProfile> findAll();

}