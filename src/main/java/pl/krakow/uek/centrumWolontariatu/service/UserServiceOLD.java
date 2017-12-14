package pl.krakow.uek.centrumWolontariatu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.UserOLD;
import pl.krakow.uek.centrumWolontariatu.domain.repository.UserRepositoryOLD;

/**
 * Created by MSI DRAGON on 2017-12-10.
 */

@Service("userServiceOLD")
public class UserServiceOLD {

    @Autowired
    @Qualifier("userRepositoryOLD")
    private UserRepositoryOLD userRepositoryOLD;

    public UserOLD findByEmail(String email) {
        return userRepositoryOLD.findByEmail(email);
    }

    public UserOLD findByConfirmationToken(String confirmationToken) {
        return userRepositoryOLD.findByConfirmationToken(confirmationToken);
    }


    public void saveUser(UserOLD userOLD) {
        userRepositoryOLD.save(userOLD);
    }

}