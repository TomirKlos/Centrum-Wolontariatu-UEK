package pl.krakow.uek.centrumWolontariatu.main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

/**
 * Created by MSI DRAGON on 2017-12-03.
 */
@SpringBootApplication(scanBasePackages={"pl.krakow.uek"})
@ComponentScan({"pl.krakow.uek"})
@EntityScan("pl.krakow.uek.centrumWolontariatu.domain")
@EnableJpaRepositories("pl.krakow.uek.centrumWolontariatu.domain.repository")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }
    @Bean(name = "bcryptPasswordEncoder")
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }
}
