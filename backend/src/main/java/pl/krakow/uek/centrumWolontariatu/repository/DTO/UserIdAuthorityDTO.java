package pl.krakow.uek.centrumWolontariatu.repository.DTO;

import pl.krakow.uek.centrumWolontariatu.domain.Authority;

import java.util.Set;

public interface UserIdAuthorityDTO {
    Long getId();

    Set<Authority> getAuthorities();
}
