package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.krakow.uek.centrumWolontariatu.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String login);

    Optional<User> findByActivationKey(String activationKey);

    Optional<User> findByResetKey(String resetKey);
}
