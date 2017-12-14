package pl.krakow.uek.centrumWolontariatu.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.krakow.uek.centrumWolontariatu.domain.UserOLD;

/**
 * Created by MSI DRAGON on 2017-12-10.
 */


@Repository("userRepositoryOLD")
public interface UserRepositoryOLD extends CrudRepository<UserOLD, Long> {
    UserOLD findByEmail(String email);
    UserOLD findByConfirmationToken(String confirmationToken);
}