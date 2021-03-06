package pl.krakow.uek.centrumWolontariatu.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.krakow.uek.centrumWolontariatu.configuration.constant.UserConstant;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.repository.UserRepository;
import pl.krakow.uek.centrumWolontariatu.service.MailService;
import pl.krakow.uek.centrumWolontariatu.service.UserService;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.EmailAlreadyUsedException;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.EmailNotFoundException;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.InternalServerErrorException;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.InvalidPasswordException;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountResource {
    private final UserRepository userRepository;
    private final UserService userService;
    private final MailService mailService;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * POST  /register : register the user.
     *
     * @param userVM the managed user View Model
     * @throws InvalidPasswordException  400 (Bad Request) if the password is incorrect
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody UserRegisterVM userVM) {

        if (!checkPasswordLength(userVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        userRepository.findByEmail(userVM.getEmail().toLowerCase()).ifPresent(u -> {
            throw new EmailAlreadyUsedException();
        });
        User user = userService.registerUser(userVM.getEmail(), userVM.getPassword(), userVM.getFirstName(), userVM.getLastName());

//        mailService.sendActivationEmail(user);
    }

    private boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= UserConstant.PASSWORD_MIN_LENGTH &&
            password.length() <= UserConstant.PASSWORD_MAX_LENGTH;
    }

    /**
     * GET  /activate : activate the registered user.
     *
     * @param activationKeyVM the activation key
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be activated
     */
    @PostMapping("/activate")
    public void activateAccount(@RequestBody ActivationKeyVM activationKeyVM) {
        Optional<User> user = userService.activateRegistration(activationKeyVM.getActivationKey());
        if (!user.isPresent()) {
            throw new BadRequestAlertException("No user was found for this reset key", "userManagement", "nouserfoundforresetkey");
        }
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the current user
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    public User getAccount() {
        return userService.getUserWithAuthorities()
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
    }

    /**
     * POST  /account/change-password : changes the current user's password
     *
     * @param passwordVM the new password
     * @throws InvalidPasswordException 400 (Bad Request) if the new password is incorrect
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordVM passwordVM) {
        if (!checkPasswordLength(passwordVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordVM.getPassword());
    }

    /**
     * POST   /account/reset-password/init : Send an email to reset the password of the user
     *
     * @param mailVM the mail of the user
     * @throws EmailNotFoundException 400 (Bad Request) if the email address is not registered
     */
    @PostMapping(path = "/reset-password/init") // TODO test it
    public void requestPasswordReset(@RequestBody MailVM mailVM) {
        mailService.sendPasswordResetMail(
            userService.requestPasswordReset(mailVM.getEmail())
                .orElseThrow(EmailNotFoundException::new)
        );
    }

    /**
     * POST   /account/reset-password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
     * @throws RuntimeException         500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping(path = "/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) { // TODO test if working
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user =
            userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this reset key");
        }
    }

}
