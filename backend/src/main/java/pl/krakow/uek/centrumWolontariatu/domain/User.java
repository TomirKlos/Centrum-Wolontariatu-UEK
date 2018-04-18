package pl.krakow.uek.centrumWolontariatu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import pl.krakow.uek.centrumWolontariatu.configuration.constant.UserConstant;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cw_users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Size(
        min = UserConstant.EMAIL_MIN_SIZE,
        max = UserConstant.EMAIL_MAX_SIZE
    )
    @Column(length = UserConstant.EMAIL_MAX_SIZE, unique = true)
    private String email;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60)
    private String password;

    @Size(max = UserConstant.FIRST_NAME_MAX_SIZE)
    @Column(name = "first_name", length = UserConstant.FIRST_NAME_MAX_SIZE)
    private String firstName;

    @Size(max = UserConstant.LAST_NAME_MAX_SIZE)
    @Column(name = "last_name", length = UserConstant.LAST_NAME_MAX_SIZE)
    private String lastName;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @Size(max = 36)
    @Column(name = "activation_key", length = 36)
    @JsonIgnore
    private String activationKey;

    @Size(max = 36)
    @Column(name = "reset_key", length = 36)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    @JsonIgnore
    private Instant resetDate = null;

    //    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "cw_user_authorities",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
            Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, email);
    }
}
