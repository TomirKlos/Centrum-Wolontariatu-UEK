package pl.krakow.uek.centrumWolontariatu.repository.solr;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.krakow.uek.centrumWolontariatu.converter.VolunteerRequestConverter;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.repository.DTO.VolunteerRequestDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class VolunteerRequestSearchDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    VolunteerRequestConverter volunteerRequestConverter;

    public List<VolunteerRequestDTO> searchVolunteerRequestByQuery(String text) {

        Query combinedQuery = getQueryBuilder()
            .bool()
            .should(getQueryBuilder()
                .keyword()
                .onFields("title", "description")
                .matching(text)
                .createQuery())
            .should(getQueryBuilder().phrase()
                .onField("description").sentence(text)
                .createQuery())
            .should(getQueryBuilder()
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(2)
                .withPrefixLength(0)
                .onField("description")
                .matching(text)
                .createQuery())
            .createQuery();

        Query filterQuery = getQueryBuilder().keyword()
            .onField("accepted").matching(1)
            .createQuery();

        Query finalQuery = getQueryBuilder()
            .bool()
            .must(combinedQuery)
            .must(filterQuery)
            .createQuery();

        List<VolunteerRequestDTO> results = VolunteerRequestConverter.mapEntityListIntoDTOList(getJpaQuery(finalQuery).setProjection().getResultList());

        return results;

    }


    private FullTextQuery getJpaQuery(org.apache.lucene.search.Query luceneQuery) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        return fullTextEntityManager.createFullTextQuery(luceneQuery, VolunteerRequest.class);
    }

    private QueryBuilder getQueryBuilder() {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        return fullTextEntityManager.getSearchFactory()
            .buildQueryBuilder()
            .forEntity(VolunteerRequest.class)
            .get();
    }
}
