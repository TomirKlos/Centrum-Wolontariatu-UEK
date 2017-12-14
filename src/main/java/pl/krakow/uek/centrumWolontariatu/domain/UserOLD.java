package pl.krakow.uek.centrumWolontariatu.domain;

/**
 * Created by MSI DRAGON on 2017-12-10.
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

@Entity
@Table(name = "userOLD")
public class UserOLD {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Proszę podać poprawny adres e-mail")
    @NotEmpty(message = "Proszę podać adres e-mail")
    private String email;

    @Column(name = "password")
    @Transient
    private String password;

    @Column(name = "first_name")
    @NotEmpty(message = "Proszę podać swoje imię")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "Proszę podać swoje nazwisko")
    private String lastName;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean value) {
        this.enabled = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserOLD)) return false;

        UserOLD userOLD = (UserOLD) o;

        return getId() == userOLD.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
