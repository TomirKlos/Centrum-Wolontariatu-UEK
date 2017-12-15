package pl.krakow.uek.centrumWolontariatu.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.krakow.uek.centrumWolontariatu.domain.UserProfile;
import pl.krakow.uek.centrumWolontariatu.service.UserProfileService;

/**
 * Created by MSI DRAGON on 2017-12-11.
 */


@Component
public class RoleToUserProfileConverter implements Converter<Object, UserProfile>{

    static final Logger logger = LoggerFactory.getLogger(RoleToUserProfileConverter.class);

    @Autowired
    UserProfileService userProfileService;

    @Override
    public UserProfile convert(Object element) {
        Integer id = Integer.parseInt((String)element);
        UserProfile profile= userProfileService.findById(id);
        logger.info("Profile : {}",profile);
        return profile;
    }

}

