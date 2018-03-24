package pl.krakow.uek.centrumWolontariatu.service;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.repository.UserRepository;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.security.SecurityUtils;
import pl.krakow.uek.centrumWolontariatu.util.rsql.CustomRsqlVisitor;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.EmailNotAllowedException;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VolunteerRequestRepository volunteerRequestRepository;
    @Autowired
    MailDomainToAuthoritiesService mailDomainToAuthoritiesService;


    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, VolunteerRequestRepository volunteerRequestRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.volunteerRequestRepository = volunteerRequestRepository;
    }


    public User registerUser(String email, String password) {
        if (!mailDomainToAuthoritiesService.emailFromAllowedDomain(email)) {
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

    @Transactional
    public Page<User> findAllByRsql(int page, int numberOfResultsPerPage, Optional<String> sortBy, Optional<String> search, Optional<Boolean> descending) {
        Sort.Direction sort = Sort.Direction.DESC;
        if(descending.isPresent() && !descending.get()) sort = Sort.Direction.ASC;
        String sortByField = "id";
        if(sortBy.isPresent()) sortByField=sortBy.get();

        Page<User> result;
        if(search.isPresent()) {
            final Node rootNode = new RSQLParser().parse(search.get());
            Specification<User> spec = rootNode.accept(new CustomRsqlVisitor<User>());
            result = userRepository.findAll(spec, new PageRequest(page, numberOfResultsPerPage, sort, sortByField));
        } else {
            result = userRepository.findAll(new PageRequest(page, numberOfResultsPerPage, sort, sortByField));
        }
        return result;
    }

}
