package pl.krakow.uek.centrumWolontariatu.converter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerAd;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl.UserIdAuthorityDTOImpl;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl.UserIdDTOImpl;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl.VolunteerAdDTOImpl;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.UserIdAuthorityDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.UserIdDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerAdDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class VolunteerAdConverter {

    static List<VolunteerAdDTO> mapEntitiesIntoDTOList(Iterable<VolunteerAd> entities) {
        List<VolunteerAdDTO> dtoList = new ArrayList<>();

        entities.forEach(e -> dtoList.add(mapEntityIntoDTO(e)));

        return dtoList;
    }

    static VolunteerAdDTOImpl mapEntityIntoDTO(VolunteerAd entity) {
        VolunteerAdDTOImpl dto = new VolunteerAdDTOImpl();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCategories(entity.getCategories());
        dto.setUser(mapEntityIntoUserDTO(entity.getUser()));
        dto.setTimestamp(entity.getTimestamp());
        dto.setExpired(entity.getExpired());
        dto.setExpirationDate(entity.getExpirationDate());
        dto.setAccepted(entity.getAccepted());
        dto.setPictures(entity.getPictures());
        return dto;
    }

    static UserIdAuthorityDTO mapEntityIntoUserDTO(User entity) {
        UserIdAuthorityDTOImpl dto = new UserIdAuthorityDTOImpl();
        dto.setId(entity.getId());
        dto.setAuthorities(entity.getAuthorities());
        return dto;
    }


    public static Page<VolunteerAdDTO> mapEntityPageIntoDTOPage(Pageable pageRequest, Page<VolunteerAd> source) {
        List<VolunteerAdDTO> dtoList = mapEntitiesIntoDTOList(source.getContent());
        return new PageImpl<>(dtoList, pageRequest, source.getTotalElements());
    }

    public static List<VolunteerAdDTO> mapEntityListIntoDTOList(List<VolunteerAd> source) {
        return mapEntitiesIntoDTOList(source);
    }

}
