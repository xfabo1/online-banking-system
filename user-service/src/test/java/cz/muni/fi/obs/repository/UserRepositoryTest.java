package cz.muni.fi.obs.repository;

import cz.muni.fi.obs.data.dbo.User;
import cz.muni.fi.obs.data.enums.Nationality;
import cz.muni.fi.obs.data.repository.UserRepository;
import cz.muni.fi.obs.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Sql(value = {"/seed_db.sql"}, executionPhase = BEFORE_TEST_METHOD)
@Sql(value = {"/clean_db.sql"}, executionPhase = AFTER_TEST_METHOD)
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByIdOrThrow_UserFound_ReturnsUser() {
        User user = userRepository.findByIdOrThrow(UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bec2"));

        assertThat(user)
                .returns(UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bec2"), User::getId)
                .returns("John", User::getFirstName)
                .returns("Doe", User::getLastName)
                .returns("9707178239", User::getPhoneNumber)
                .returns("example1@domain.com", User::getEmail)
                .returns("1990-01-01", u -> u.getBirthDate().toString())
                .returns(Nationality.CZ, User::getNationality)
                .returns("900101/1234", User::getBirthNumber)
                .returns(true, User::isActive);
    }

    @Test
    public void findByIdOrThrow_UserNotFound_ThrowsException() {
        assertThatThrownBy(() -> userRepository.findByIdOrThrow(UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bec8"
        )))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id 5e4b3326-38b5-4484-8034-33d81f34bec8 not found");
    }


    @Test
    public void findBySearchParams_UsersNotFound_returnEmptyList() {
        Page<User> users = userRepository.findBySearchParams(
                Optional.of("non-existing"),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Pageable.unpaged()
        );
        assertThat(users).isEmpty();
    }

    @Test
    public void findBySearchParams_NoParams_returnAll() {
        Page<User> users = userRepository.findBySearchParams(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Pageable.unpaged()
        );

        assertThat(users).hasSize(4);
    }

    @Test
    public void findBySearchParams_WithPagination_ReturnsPaginated() {
        int pageSize = 2; // Assuming a page size of 2 for testing purposes

        // Fetch the first page
        Page<User> firstPage = userRepository.findBySearchParams(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                PageRequest.of(0, pageSize)
        );

        assertThat(firstPage).hasSize(2);
        assertThat(firstPage.getTotalElements()).isEqualTo(4); // Assuming there are at least 4 users

        // Fetch the second page
        Page<User> secondPage = userRepository.findBySearchParams(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                PageRequest.of(1, pageSize)
        );

        assertThat(secondPage).hasSize(2);
        assertThat(secondPage.getTotalElements()).isEqualTo(4); // Assuming there are at least 4 users
    }

    @Test
    public void findBySearchParams_AllParams_returnSingle() {
        Page<User> users = userRepository.findBySearchParams(
                Optional.of("John"),
                Optional.of("Doe"),
                Optional.of("9707178239"),
                Optional.of("example1@domain.com"),
                Optional.of(LocalDate.of(1990, 1, 1)),
                Optional.of("900101/1234"),
                Optional.of(true),
                Pageable.unpaged()
        );

        assertThat(users).hasSize(1);
        assertThat(users.getContent().getFirst())
                .returns(UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bec2"), User::getId)
                .returns("John", User::getFirstName)
                .returns("Doe", User::getLastName)
                .returns("9707178239", User::getPhoneNumber)
                .returns("example1@domain.com", User::getEmail)
                .returns("1990-01-01", u -> u.getBirthDate().toString())
                .returns(Nationality.CZ, User::getNationality)
                .returns("900101/1234", User::getBirthNumber)
                .returns(true, User::isActive);
    }
}
