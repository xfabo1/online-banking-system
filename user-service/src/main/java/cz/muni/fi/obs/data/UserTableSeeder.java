package cz.muni.fi.obs.data;

import cz.muni.fi.obs.data.dbo.User;
import cz.muni.fi.obs.data.enums.Nationality;
import cz.muni.fi.obs.data.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@ConditionalOnExpression("${data.initialize:false}")
@Slf4j
public class UserTableSeeder {

    private final UserRepository userRepository;

    @Autowired
    public UserTableSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initializeData() {
        log.info("Initializing user data.");

        long cntUsers = userRepository.count();
        if (cntUsers > 0) {
            log.info("User data already initialized.");
            return;
        }

        userRepository.saveAll(getUserSeedData());
        log.info("Initialized user data.");
    }

    private List<User> getUserSeedData() {
        User user1 = new User("John",
                              "Doe",
                              "+420102306985",
                              "JohnDoe@gmail.com",
                              LocalDate.of(1995, 7, 17),
                              Nationality.CZ,
                              "950717/5073",
                              true
        );
        user1.setId(UUID.fromString("4121add0-f5d7-4128-9c8f-e81fa93237c5"));
        User user2 = new User("Petr",
                              "Hrach",
                              "+420556991123",
                              "PeteruvMail@gmail.com",
                              LocalDate.of(1993, 5, 12),
                              Nationality.CZ,
                              "930512/4477",
                              true
        );
        user2.setId(UUID.fromString("4121add0-f5d7-4128-9c8f-e81fa93237c6"));
        User user3 = new User("Iva",
                              "Dolava",
                              "+421752694158",
                              "IvaDol@gmail.com",
                              LocalDate.of(1991, 8, 5),
                              Nationality.SK,
                              "915805/9999",
                              true
        );
        user3.setId(UUID.fromString("4121add0-f5d7-4128-9c8f-e81fa93237c7"));
        User user4 = new User("Pavel",
                              "Horel",
                              "+421346952176",
                              "PavelHore@email.sk",
                              LocalDate.of(1998, 6, 4),
                              Nationality.SK,
                              "980604/2268",
                              false
        );
        user4.setId(UUID.fromString("4121add0-f5d7-4128-9c8f-e81fa93237c8"));

        return List.of(user1, user2, user3, user4);
    }
}