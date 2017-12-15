package pl.krakow.uek.centrumWolontariatu.domain.repository;

import pl.krakow.uek.centrumWolontariatu.domain.UserProfile;

import java.util.List;

/**
 * Created by MSI DRAGON on 2017-12-11.
 */


public interface UserProfileRepository {

    List<UserProfile> findAll();

    UserProfile findByType(String type);

    UserProfile findById(int id);
}
