package pl.krakow.uek.centrumWolontariatu.service;

/**
 * Created by MSI DRAGON on 2017-12-10.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.repository.UserRepository;


@Service("userService")
public class UserService {

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByConfirmationToken(String confirmationToken) {
        return userRepository.findByConfirmationToken(confirmationToken);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

}