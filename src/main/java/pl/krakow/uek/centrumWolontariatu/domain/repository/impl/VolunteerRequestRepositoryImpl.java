package pl.krakow.uek.centrumWolontariatu.domain.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.domain.repository.AbstractRepository;
import pl.krakow.uek.centrumWolontariatu.domain.repository.VolunteerRequestRepository;

import java.util.List;

@Repository("volunteerRequestRepository")
public class VolunteerRequestRepositoryImpl extends AbstractRepository<Integer, VolunteerRequest> implements VolunteerRequestRepository{

    static final Logger logger = LoggerFactory.getLogger(VolunteerRequestRepositoryImpl.class);

    @Override
    public VolunteerRequest findById(int id) {
        VolunteerRequest volunteerRequest = getByKey(id);
        return volunteerRequest;
    }

    @Override
    public void save(VolunteerRequest volunteerRequest) { persist(volunteerRequest); }

    @SuppressWarnings("unchecked")
    @Override
    public List<VolunteerRequest> findAllVolunteerRequests() {
        Criteria criteria = createEntityCriteria().addOrder(Order.asc("id"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
        List<VolunteerRequest> volunteerRequest = (List<VolunteerRequest>) criteria.list();
        return volunteerRequest;
    }
}
