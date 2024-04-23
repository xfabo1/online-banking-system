package cz.muni.fi.obs.repository;

import cz.muni.fi.obs.data.dbo.User;
import cz.muni.fi.obs.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@Sql(value = {"/initialize_db.sql"}, executionPhase = BEFORE_TEST_CLASS)
@Sql(value = {"/drop_all.sql"}, executionPhase = AFTER_TEST_CLASS)
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByIdOrThrow_UserFound_ReturnsUser() {
        User user = userRepository.findByIdOrThrow(UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bec2"));

        assertThat(user)
                .returns("5e4b3326-38b5-4484-8034-33d81f34bec2", User::getId)
                .returns("John", User::getFirstName)
                .returns("Doe", User::getLastName)
                .returns("9707178239", User::getPhoneNumber)
                .returns("example1@domain.com", User::getEmail)
                .returns("1990-01-01", u -> u.getBirthDate().toString())
                .returns("CZ", User::getNationality)
                .returns("900101/1234", User::getBirthNumber)
                .returns(true, User::isActive);
    }
}
