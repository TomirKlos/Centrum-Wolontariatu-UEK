package pl.krakow.uek.centrumWolontariatu.web.rest.vm;

import javax.validation.constraints.NotNull;

/**
 * View Model object for storing a user's credentials.
 */
public class LoginVM {
    @NotNull
    private String email;

    @NotNull
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "LoginVM{" +
            "email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
