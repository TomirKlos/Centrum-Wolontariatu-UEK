package pl.krakow.uek.centrumWolontariatu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.service.UserService;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerRequestService;

import java.util.List;


/*
https://www.concretepage.com/spring-boot/spring-boot-security-rest-jpa-hibernate-mysql-crud-example!!!!!!!!!!!!!!!
SUPER|!!!!!!!!!!!!!!!
*/

@RestController
@SessionAttributes("roles")
public class VolunteerRequestRestController {

    @Autowired
    VolunteerRequestService volunteerRequestService;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationTrustResolver authenticationTrustResolver;

    @RequestMapping(value = { "/rest/volunteer/list" }, method = RequestMethod.GET)
    public List<VolunteerRequest> listVolunteerRequests(ModelMap model) {
        return volunteerRequestService.findAllVolunteerRequests();
    }

    @RequestMapping(value = { "/rest/25" }, method = RequestMethod.GET)
    public VolunteerRequest get(ModelMap model) {
       // String name = getPrincipal().getFirstName();
     //   System.out.println(name);
        return volunteerRequestService.findById(1);
    }

    @RequestMapping(value = { "/dupa" }, method = RequestMethod.GET)
    public User getddd(ModelMap model) {
        return getPrincipal();
    }


    @RequestMapping(value = { "/api/v1/xd" }, method = RequestMethod.POST)
    public ResponseEntity getNewVolunteer(@RequestBody MultiValueMap<String,String> formData) {


        VolunteerRequest volunteerRequest = new VolunteerRequest(
                formData.get("description").get(0).toString(),
                Integer.valueOf(formData.get("number_of_requested_volunteers").get(0)),
                getPrincipal()
        );

        volunteerRequestService.save(volunteerRequest);

        return ResponseEntity.ok("");
    }

    @RequestMapping(value = { "/api/v1/xd2" }, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity getNewVolunteer2() {


        return ResponseEntity.ok("");
    }






    private User getPrincipal(){

            String userName = null;
            User user = null;

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof UserDetails) {
                userName = ((UserDetails) principal).getUsername();
                user=userService.findBySSO(userName);
            } else {
                userName = principal.toString();
            }

            return user;
    }
/*
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Cart create(@RequestBody Cart cart) {
        return cartService.create(cart);
    }
    */


}
