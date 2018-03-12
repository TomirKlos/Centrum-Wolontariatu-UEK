package pl.krakow.uek.centrumWolontariatu.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Validated
public class AppProperties {
    private final Jwt jwt = new Jwt();
    private final CorsConfiguration cors = new CorsConfiguration();

    public Jwt getJwt() {
        return jwt;
    }

    public CorsConfiguration getCors() {
        return cors;
    }

    public static class Jwt {
        @NotEmpty
        @Size(min = 20)
        private String secret;
        @NotNull
        private int tokenValidityInSeconds;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public int getTokenValidityInSeconds() {
            return tokenValidityInSeconds;
        }

        public void setTokenValidityInSeconds(int tokenValidityInSeconds) {
            this.tokenValidityInSeconds = tokenValidityInSeconds;
        }

    }
}
