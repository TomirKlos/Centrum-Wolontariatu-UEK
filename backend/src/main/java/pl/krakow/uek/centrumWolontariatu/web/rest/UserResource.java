package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    final private UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAll(
        @RequestParam(value = "activated", required = false) Boolean activated,
        Pageable pageable) {

        Page<User> page = userService.findAll(pageable, activated);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

}
