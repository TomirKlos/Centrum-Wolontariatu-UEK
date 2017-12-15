package pl.krakow.uek.centrumWolontariatu.domain.repository;

import pl.krakow.uek.centrumWolontariatu.domain.User;

import java.util.List;

/**
 * Created by MSI DRAGON on 2017-12-11.
 */

public interface UserRepository {

    User findById(int id);

    User findBySSO(String sso);

    void save(User user);

    void saveCustomerAccount(User user);

    void deleteBySSO(String sso);

    List<User> findAllUsers();

}
