package pl.krakow.uek.centrumWolontariatu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by MSI DRAGON on 2017-12-15.
 */
@ControllerAdvice
public class GlobalControllerAdvice {
    /*

    @Autowired
    AuthenticationTrustResolver authenticationTrustResolver;

    @ModelAttribute
    public void globalAttributes(Model model) {
        model.addAttribute("loggedinuser", getPrincipal());
        model.addAttribute("isAnonymous", isCurrentAuthenticationAnonymous());
    }

    private String getPrincipal(){
        String userName = null;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    private boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }
    */

}
