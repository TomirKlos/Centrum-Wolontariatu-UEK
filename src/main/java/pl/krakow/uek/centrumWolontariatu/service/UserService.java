package pl.krakow.uek.centrumWolontariatu.service;

import pl.krakow.uek.centrumWolontariatu.domain.User;

import java.util.List;

/**
 * Created by MSI DRAGON on 2017-12-11.
 */


public interface UserService {

    User findById(int id);

    User findBySSO(String sso);

    void saveUser(User user);

    void saveCustomerAccount(User user);

    void updateUser(User user);

    void deleteUserBySSO(String sso);

    List<User> findAllUsers();

    boolean isUserSSOUnique(Integer id, String sso);

}
