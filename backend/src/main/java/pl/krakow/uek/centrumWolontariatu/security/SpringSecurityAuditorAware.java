package pl.krakow.uek.centrumWolontariatu.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.service.UserService;

import javax.transaction.Transactional;
import java.util.Optional;


@Component
public class SpringSecurityAuditorAware implements AuditorAware<Long> {

    private final UserService userService;

    public SpringSecurityAuditorAware(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public Optional<Long> getCurrentAuditor() {
        User user = userService.getUserWithAuthorities().get();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return Optional.of(Long.valueOf(1));
    }
}
