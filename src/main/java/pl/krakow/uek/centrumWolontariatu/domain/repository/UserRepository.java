package pl.krakow.uek.centrumWolontariatu.domain.repository;

/**
 * Created by MSI DRAGON on 2017-12-10.
 */

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.krakow.uek.centrumWolontariatu.domain.User;


@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
    User findByConfirmationToken(String confirmationToken);
}