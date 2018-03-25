package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.repository.UserRepository;
import pl.krakow.uek.centrumWolontariatu.service.MailDomainToAuthoritiesService;
import pl.krakow.uek.centrumWolontariatu.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    final private UserService userService;
    private final UserRepository userRepository;
    private final MailDomainToAuthoritiesService mailDomainToAuthoritiesService;

    public UserResource(UserService userService, UserRepository userRepository, MailDomainToAuthoritiesService mailDomainToAuthoritiesService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailDomainToAuthoritiesService = mailDomainToAuthoritiesService;
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAll(
        @RequestParam(value = "activated", required = false) Boolean activated,
        Pageable pageable) {

        Page<User> page = userService.findAll(pageable, activated);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping(path = "/activate")
    @ResponseStatus(HttpStatus.OK)
    public void activateUser(@RequestParam long id){
        userService.activateUser(id);
    }

}
