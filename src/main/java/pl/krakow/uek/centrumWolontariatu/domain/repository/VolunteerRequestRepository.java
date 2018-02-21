package pl.krakow.uek.centrumWolontariatu.domain.repository;

import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;

import java.util.List;

public interface VolunteerRequestRepository {

    VolunteerRequest findById(int id);

    void save(VolunteerRequest volunteerRequest);

    List<VolunteerRequest> findAllVolunteerRequests();

}
