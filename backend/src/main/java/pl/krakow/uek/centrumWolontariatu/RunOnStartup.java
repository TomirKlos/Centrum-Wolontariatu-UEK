package pl.krakow.uek.centrumWolontariatu;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.krakow.uek.centrumWolontariatu.domain.Authority;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.repository.AuthorityRepository;
import pl.krakow.uek.centrumWolontariatu.repository.UserRepository;
import pl.krakow.uek.centrumWolontariatu.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RunOnStartup {

    private final AuthorityRepository authorityRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public RunOnStartup(AuthorityRepository authorityRepository, UserService userService, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        addRolesToDB();
        addAdminAccountIfNotExist();
    }

    private void addRolesToDB() {
        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_ADMIN");
        authorities.add("ROLE_LECTURER");
        authorities.add("ROLE_USER");

        for (String string : authorities) {
            Authority authority = new Authority();
            authority.setName(string);

            authorityRepository.save(authority);
        }
    }

    private void addAdminAccountIfNotExist() {
        if (userRepository.findByEmail("admin@uek.krakow.pl").isPresent()) {
            return;
        }

        User admin = userService.registerUser("admin@uek.krakow.pl", "admin@uek.krakow.pl");
        userService.activateRegistration(admin.getActivationKey()).ifPresent(user -> {

            Set<Authority> authorities = new HashSet<>();
            Authority roleAdmin = new Authority();
            roleAdmin.setName("ROLE_ADMIN");
            authorities.add(roleAdmin);

            Authority temp = new Authority();
            temp.setName("ROLE_LECTURER");
            authorities.add(temp);

            user.setAuthorities(authorities);

            userRepository.save(user);
        });
    }
}
