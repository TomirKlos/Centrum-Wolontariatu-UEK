package pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl;

import lombok.Data;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.UserIdDTO;

import java.io.Serializable;

@Data
public class UserIdDTOImpl implements UserIdDTO, Serializable {
    Long id;
}
