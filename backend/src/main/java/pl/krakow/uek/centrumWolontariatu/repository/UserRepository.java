package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.krakow.uek.centrumWolontariatu.domain.Authority;
import pl.krakow.uek.centrumWolontariatu.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String login);

    Optional<User> findByActivationKey(String activationKey);

    Optional<User> findByResetKey(String resetKey);

    User findOneById(long id);

    List<User> findAll(Specification specification);

    List<User> findByActivated(boolean isActivated);

}
