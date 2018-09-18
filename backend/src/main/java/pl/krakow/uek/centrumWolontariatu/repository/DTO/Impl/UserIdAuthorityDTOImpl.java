package pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl;

import lombok.Data;
import pl.krakow.uek.centrumWolontariatu.domain.Authority;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.UserIdAuthorityDTO;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserIdAuthorityDTOImpl implements UserIdAuthorityDTO, Serializable {
    Long id;
    Set<Authority> authorities = new HashSet<>();
}
