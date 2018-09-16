package pl.krakow.uek.centrumWolontariatu.web.rest.vm;

import javax.validation.constraints.NotNull;

/**
 * View Model object for storing a user's credentials.
 */
public class UserRegisterVM {
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "UserVM{" +
            "email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
