package pl.krakow.uek.centrumWolontariatu.domain.repository.impl;

import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pl.krakow.uek.centrumWolontariatu.domain.UserProfile;
import pl.krakow.uek.centrumWolontariatu.domain.repository.AbstractRepository;
import pl.krakow.uek.centrumWolontariatu.domain.repository.UserProfileRepository;

/**
 * Created by MSI DRAGON on 2017-12-11.
 */


@Repository("userProfileDao")
public class UserProfileRepositoryImpl extends AbstractRepository<Integer, UserProfile> implements UserProfileRepository {

    public UserProfile findById(int id) {
        return getByKey(id);
    }

    public UserProfile findByType(String type) {
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("type", type));
        return (UserProfile) crit.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<UserProfile> findAll(){
        Criteria crit = createEntityCriteria();
        crit.addOrder(Order.asc("type"));
        return (List<UserProfile>)crit.list();
    }

}
