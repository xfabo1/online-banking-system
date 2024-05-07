package cz.muni.fi.obs.data.dbo;

import cz.muni.fi.obs.data.enums.Nationality;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "us_user")
public class User extends Dbo {

    @Column(nullable = false, unique = true)
    private String oauthId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Nationality nationality;

    @Column(nullable = false, unique = true, length = 20)
    private String birthNumber;

    @Column(nullable = false)
    private boolean active;
}
