package pl.krakow.uek.centrumWolontariatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.krakow.uek.centrumWolontariatu.domain.InvitationToVolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.InvitationToVolunteerRequestDTO;

public interface InvitationToVolunteerRequestRepository extends JpaRepository<InvitationToVolunteerRequest, Long>, JpaSpecificationExecutor<InvitationToVolunteerRequest> {
    Page<InvitationToVolunteerRequestDTO> findAllByVolunteerRequestId(Pageable pageable, Long id);
    Page<InvitationToVolunteerRequestDTO> findAllByUserInvitedId(Pageable pageable, Long id);
    Page<InvitationToVolunteerRequestDTO> findAllByVolunteerAdId(Pageable pageable, Long id);
    Long countByVolunteerAdIdAndSeen(Long id, byte seen);
}
