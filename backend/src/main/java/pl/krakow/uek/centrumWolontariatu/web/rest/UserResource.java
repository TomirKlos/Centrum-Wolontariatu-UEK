package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.service.UserService;
import pl.krakow.uek.centrumWolontariatu.web.rest.util.HeaderUtil;

@RestController
@RequestMapping("/api/users")
@Secured({"ROLE_ADMIN"})
public class UserResource {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        log.debug("REST request to delete User: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", id.toString())).build();
    }

    @PostMapping(path = "/activate")
    @ResponseStatus(HttpStatus.OK)
    public void activateUser(@RequestParam long id) {
        userService.activateUser(id);
    }

}
