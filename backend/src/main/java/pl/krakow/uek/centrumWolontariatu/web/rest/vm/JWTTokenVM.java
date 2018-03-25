package pl.krakow.uek.centrumWolontariatu.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTTokenVM {
    private String idToken;

    public JWTTokenVM(String idToken) {
        this.idToken = idToken;
    }

    @JsonProperty("jwtToken")
    String getIdToken() {
        return idToken;
    }

    void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
