package pl.krakow.uek.centrumWolontariatu.service;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.repository.UserRepository;
import pl.krakow.uek.centrumWolontariatu.service.dataFetcher.AllUsersDataFetcher;
import pl.krakow.uek.centrumWolontariatu.service.dataFetcher.UserDataFetcher;
import pl.krakow.uek.centrumWolontariatu.service.dataFetcher.UserIsActivatedDataFetcher;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class GraphQLService {

    @Value("classpath:users.graphql")
    Resource resource;

    private GraphQL graphQL;
    @Autowired
    private AllUsersDataFetcher allUsersDataFetcher;
    @Autowired
    private UserDataFetcher userDataFetcher;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserIsActivatedDataFetcher userIsActivatedDataFetcher;

    @PostConstruct
    public void loadSchema() throws IOException {
        //get the schema
        File schemaFile = resource.getFile();
        //parse schema
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring runtimeWiring = buildRuntimeWiring();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
            .type("Query", typeWiring -> typeWiring
                  .dataFetcher("allUsers", allUsersDataFetcher)
                  .dataFetcher("user", userDataFetcher)
                  .dataFetcher("isActivated", userIsActivatedDataFetcher))
            .build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
