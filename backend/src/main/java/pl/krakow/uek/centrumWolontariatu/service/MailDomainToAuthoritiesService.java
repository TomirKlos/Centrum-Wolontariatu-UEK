package pl.krakow.uek.centrumWolontariatu.service;

import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.Authority;

import java.util.HashSet;
import java.util.Set;

@Service
public class MailDomainToAuthoritiesService {

    //TODO upewnic sie co do mailow wykladowcow i studentow.

    public Set<Authority> awardAuthoritiesBasedEmail(String email) {
        Set<Authority> authoritySet = new HashSet<>();
        Authority authority = new Authority();
        if (email.matches("^([^0-9]{5,7}).*(uek.krakow.pl)$")) {
            authority.setName("ROLE_LECTURER");
            authoritySet.add(authority);
        } else {
            authority.setName("ROLE_USER");
            authoritySet.add(authority);
        }
        return authoritySet;
    }

    public boolean emailFromAllowedDomain(String email) {
        return (email.matches("^.*(uek.krakow.pl)$"));
    }

}

