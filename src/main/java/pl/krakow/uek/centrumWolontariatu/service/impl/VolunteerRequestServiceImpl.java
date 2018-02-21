package pl.krakow.uek.centrumWolontariatu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.domain.repository.VolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.service.VolunteerRequestService;

import java.util.List;

@Service("volunteerRequestService")
@Transactional
public class VolunteerRequestServiceImpl implements VolunteerRequestService {

    @Autowired
    private VolunteerRequestRepository volunteerRequestRepository;

    @Override
    public VolunteerRequest findById(int id) {
        return volunteerRequestRepository.findById(id);
    }

    @Override
    public void save(VolunteerRequest volunteerRequest) {
        volunteerRequestRepository.save(volunteerRequest);
    }

    @Override
    public List<VolunteerRequest> findAllVolunteerRequests() {
        return volunteerRequestRepository.findAllVolunteerRequests();
    }
}
