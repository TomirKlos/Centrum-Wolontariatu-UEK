package pl.krakow.uek.centrumWolontariatu.service.dataFetcher;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.repository.UserRepository;

import java.util.List;

@Component
public class UserIsActivatedDataFetcher implements DataFetcher<List<User>> {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> get(DataFetchingEnvironment dataFetchingEnvironment) {
        boolean isActivated = dataFetchingEnvironment.getArgument("activated");
        return userRepository.findByActivated(isActivated);
    }
}
