package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krakow.uek.centrumWolontariatu.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
