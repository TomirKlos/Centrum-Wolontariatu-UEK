package pl.krakow.uek.centrumWolontariatu.service.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.UserProfile;
import pl.krakow.uek.centrumWolontariatu.domain.repository.UserRepository;
import pl.krakow.uek.centrumWolontariatu.service.UserService;

/**
 * Created by MSI DRAGON on 2017-12-11.
 */


@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findById(int id) {
        return userRepository.findById(id);
    }

    public User findBySSO(String sso) {
        User user = userRepository.findBySSO(sso);
        return user;
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void saveCustomerAccount(User user) {
        Set profile = new LinkedHashSet<UserProfile>();
        UserProfile up = new UserProfile();
        up.setId(1);
        up.setType("USER");
        profile.add(up);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserProfiles(profile);
        userRepository.save(user);
    }


    public void updateUser(User user) {
        User entity = userRepository.findById(user.getId());
        if(entity!=null){
            entity.setSsoId(user.getSsoId());
            if(!user.getPassword().equals(entity.getPassword())){
                entity.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            entity.setFirstName(user.getFirstName());
            entity.setLastName(user.getLastName());
            entity.setEmail(user.getEmail());
            entity.setUserProfiles(user.getUserProfiles());
        }
    }


    public void deleteUserBySSO(String sso) {
        userRepository.deleteBySSO(sso);
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public boolean isUserSSOUnique(Integer id, String sso) {
        User user = findBySSO(sso);
        return ( user == null || ((id != null) && (user.getId() == id)));
    }

}
