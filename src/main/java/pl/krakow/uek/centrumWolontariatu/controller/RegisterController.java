package pl.krakow.uek.centrumWolontariatu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.UserOLD;
import pl.krakow.uek.centrumWolontariatu.domain.UserProfile;
import pl.krakow.uek.centrumWolontariatu.domain.UserProfileType;
import pl.krakow.uek.centrumWolontariatu.service.EmailService;
import pl.krakow.uek.centrumWolontariatu.service.UserService;
import pl.krakow.uek.centrumWolontariatu.service.UserServiceOLD;

/**
 * Created by MSI DRAGON on 2017-12-10.
 */

@Controller
public class RegisterController {

    @Autowired
    @Qualifier("bcryptPasswordEncoder")
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserServiceOLD userServiceOLD;
    @Autowired
    private EmailService emailService;


    @RequestMapping(value="/register", method = RequestMethod.GET)
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, UserOLD userOLD){
        modelAndView.addObject("userOLD", userOLD);
        modelAndView.setViewName("thymeleaf/register");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid UserOLD userOLD, BindingResult bindingResult, HttpServletRequest request) {

        UserOLD userOLDExists = userServiceOLD.findByEmail(userOLD.getEmail());

        if(userOLDExists != null) {
            modelAndView.addObject("errorMessage", "Konto o podanym adresie e-mail już istnieje. " + userOLD.getEmail());
            modelAndView.setViewName("thymeleaf/register");
            bindingResult.reject("email");
            return modelAndView;
        }
        //wyłapuje e-maile które nie naleza do domeny wizard.uek.krakow.pl
        /*
        if(!userOLD.getEmail().matches("^(.*[@]wizard.uek.krakow.pl)$")) {
            modelAndView.addObject("errorMessage", "Konto w systemie można zakładać jedynie za pomocą poczty uczelnianej: [nazwaUzytkownika]@wizard.uek.krakow.pl ");
            modelAndView.setViewName("thymeleaf/register");
            bindingResult.reject("email");
            return modelAndView;
        }*/

        /*
        if(userOLD.getEmail().matches("^([sS][0-9]{5,7}[@]wizard.uek.krakow.pl)$")){
            System.out.println("KONTO STUDENTA");
        }else if(userOLD.getEmail().matches("^(.*[@]wizard.uek.krakow.pl)$")){
            System.out.println("Konto wykladowcy");
        } */
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("thymeleaf/register");
        } else { //Jeśli nie ma błędów, przechodzi do sekcji tworzenia uzytkownika

            //Użytkownik nie jest aktywny do momentu kliknięcia w link aktywacyjny oraz ustawienia swojego hasła.
            userOLD.setEnabled(false);

            // Generuje losowy 36 znakowy string używany do wygenerowania linku aktywacyjnego
            userOLD.setConfirmationToken(UUID.randomUUID().toString());

            userServiceOLD.saveUser(userOLD);

            String applicationUrl = request.getScheme() + "://" + request.getServerName();

            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(userOLD.getEmail());
            registrationEmail.setSubject("Centrum Wolontariatu UEK - potwierdzenie rejestracji");
            registrationEmail.setText("Aby potwierdzić swoj adres e-mail, kliknij w link poniżej:\n"
                    + applicationUrl + "/confirm?token=" + userOLD.getConfirmationToken());
            registrationEmail.setFrom("noreply.uek.dev.text@gmail.com");

            emailService.sendEmail(registrationEmail);

            modelAndView.addObject("confirmationMessage", "E-mail z linkiem potwierdzającym rejestracje został wysłany na adres: " + userOLD.getEmail());
            modelAndView.setViewName("thymeleaf/register");
        }
        return modelAndView;
    }

    // Potwierdzanie adresu e-mail za pomocą wygenerowanego tokenu.
    @RequestMapping(value="/confirm", method = RequestMethod.GET)
    public ModelAndView showConfirmationPage(ModelAndView modelAndView, @RequestParam("token") String token) {

        UserOLD userOLD = userServiceOLD.findByConfirmationToken(token);

        if (userOLD == null) { // Token nie został znaleziony
            modelAndView.addObject("invalidToken", "Niepoprawny link aktywacyjny.");
        } else { // Token znaleziony
            modelAndView.addObject("confirmationToken", userOLD.getConfirmationToken());
        }
        modelAndView.setViewName("thymeleaf/confirm");
        return modelAndView;
    }

    //Potwierdzenie adresu e-mail za pomocą wygenerowanego tokenu
    @RequestMapping(value="/confirm", method = RequestMethod.POST)
    public ModelAndView processConfirmationForm(ModelAndView modelAndView, BindingResult bindingResult, @RequestParam Map requestParams, RedirectAttributes redirectAttributes) {

        modelAndView.setViewName("thymeleaf/confirm");

        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(String.valueOf(requestParams.get("password")));

        if (strength.getScore() < 2) {
            bindingResult.reject("password");

            redirectAttributes.addFlashAttribute("errorMessage", "Hasło jest zbyt słabe.");

            modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
            return modelAndView;
        }

        UserOLD userOLD = userServiceOLD.findByConfirmationToken(String.valueOf(requestParams.get("token")));
        userOLD.setPassword(bCryptPasswordEncoder.encode((CharSequence) requestParams.get("password")));
        userOLD.setEnabled(true);
        userServiceOLD.saveUser(userOLD);




        modelAndView.addObject("successMessage", "Hasło zostało ustanowione.");
        return modelAndView;
    }

}