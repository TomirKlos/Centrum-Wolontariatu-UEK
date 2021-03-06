package pl.krakow.uek.centrumWolontariatu.converter;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl.UserIdAuthorityDTOImpl;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl.UserIdDTOImpl;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.Impl.VolunteerRequestDTOImpl;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.UserIdAuthorityDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.UserIdDTO;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class VolunteerRequestConverter {

    static List<VolunteerRequestDTO> mapEntitiesIntoDTOList(Iterable<VolunteerRequest> entities) {
        List<VolunteerRequestDTO> dtoList = new ArrayList<>();

        entities.forEach(e -> dtoList.add(mapEntityIntoDTO(e)));

        return dtoList;
    }

    static List<VolunteerRequestDTO> mapListIntoDTOList(List<VolunteerRequest> entities) {
        List<VolunteerRequestDTO> dtoList = new ArrayList<>();

        entities.forEach(e -> dtoList.add(mapEntityIntoDTO(e)));

        return dtoList;
    }

    static VolunteerRequestDTOImpl mapEntityIntoDTO(VolunteerRequest entity) {
        VolunteerRequestDTOImpl dto = new VolunteerRequestDTOImpl();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setVolunteersAmount(entity.getVolunteersAmount());
        dto.setCategories(entity.getCategories());
        dto.setVolunteerRequestTypes(entity.getVolunteerRequestTypes());
        dto.setUser(mapEntityIntoUserDTO(entity.getUser()));
        dto.setTimestamp(entity.getTimestamp());
        dto.setIsForStudents(entity.getIsForStudents());
        dto.setIsForTutors(entity.getIsForTutors());
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


    public static Page<VolunteerRequestDTO> mapEntityPageIntoDTOPage(Pageable pageRequest, Page<VolunteerRequest> source) {
        List<VolunteerRequestDTO> dtoList = mapEntitiesIntoDTOList(source.getContent());
        return new PageImpl<>(dtoList, pageRequest, source.getTotalElements());
    }

    public static List<VolunteerRequestDTO> mapEntityListIntoDTOList(List<VolunteerRequest> source) {
        return mapEntitiesIntoDTOList(source);
    }

}

