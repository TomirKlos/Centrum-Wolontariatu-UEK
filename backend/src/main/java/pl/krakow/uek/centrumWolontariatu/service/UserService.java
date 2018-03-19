package pl.krakow.uek.centrumWolontariatu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.repository.UserRepository;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.security.SecurityUtils;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.EmailNotAllowedException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    MailDomainToAuthoritiesService mailDomainToAuthoritiesService;
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VolunteerRequestRepository volunteerRequestRepository;


    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, VolunteerRequestRepository volunteerRequestRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.volunteerRequestRepository = volunteerRequestRepository;
    }


    public User registerUser(String email, String password) {
        if(!mailDomainToAuthoritiesService.emailFromAllowedDomain(email)){
            throw new EmailNotAllowedException();
        }
        User newUser = new User();

        newUser.setEmail(email);
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encryptedPassword);

        newUser.setActivationKey(UUID.randomUUID().toString());
        userRepository.save(newUser);

        return newUser;

    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                user.setAuthorities(mailDomainToAuthoritiesService.awardAuthoritiesBasedEmail(user.getEmail()));

                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> getUserWithAuthorities() { // TODO change method name
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findByEmail);
    }

    public void changePassword(String password) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findByEmail)
            .ifPresent(user -> {
                String encryptedPassword = passwordEncoder.encode(password);
                user.setPassword(encryptedPassword);
                userRepository.save(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findByEmail(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(UUID.randomUUID().toString());
                user.setResetDate(Instant.now());
                userRepository.save(user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        return userRepository.findByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);

                userRepository.save(user);
                return user;
            });
    }

}
