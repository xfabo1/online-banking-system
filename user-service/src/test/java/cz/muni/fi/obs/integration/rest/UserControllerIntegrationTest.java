package cz.muni.fi.obs.integration.rest;

import cz.muni.fi.obs.api.*;
import cz.muni.fi.obs.data.dbo.User;
import cz.muni.fi.obs.data.enums.Nationality;
import cz.muni.fi.obs.data.repository.UserRepository;
import cz.muni.fi.obs.integration.ControllerIntegrationTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerIntegrationTest extends ControllerIntegrationTest {

    private static final String USER_CONTROLLER_PATH = "/api/user-service/v1/users";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createUser_newUser_createsUser() {
        UriComponents components = UriComponentsBuilder.
                fromPath(USER_CONTROLLER_PATH + "/create")
                .build();

        UserCreateDto userCreateDto = new UserCreateDto("Joe",
                                                        "Doe",
                                                        "123456789",
                                                        "test@gmail.com",
                                                        LocalDate.of(2001, 4, 13),
                                                        Nationality.SK,
                                                        "010413/2215"
        );

        UserDto userDto = requestSpecification(components)
                .contentType(ContentType.JSON)
                .body(userCreateDto)
                .post()
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(UserDto.class);


        Optional<User> createdUser = userRepository.findById(userDto.id());
        assertThat(createdUser).isPresent();
        User user = createdUser.get();

        assertThat(user)
                .returns(userCreateDto.firstName(), User::getFirstName)
                .returns(userCreateDto.lastName(), User::getLastName)
                .returns(userCreateDto.phoneNumber(), User::getPhoneNumber)
                .returns(userCreateDto.email(), User::getEmail)
                .returns(userCreateDto.birthDate(), User::getBirthDate)
                .returns(userCreateDto.nationality(), User::getNationality)
                .returns(userCreateDto.birthNumber(), User::getBirthNumber)
                .returns(true, User::isActive);
    }

    @Test
    public void createUser_invalidRequest_returnsValidationError() {

        UriComponents components = UriComponentsBuilder
                .fromPath(USER_CONTROLLER_PATH + "/create")
                .build();

        ValidationFailedResponse response = requestSpecification(components)
                .contentType(ContentType.JSON)
                .body(new UserCreateDto("", "", "", "", LocalDate.now(), null, ""))
                .post()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .as(ValidationFailedResponse.class);

        assertThat(response.message()).isEqualTo("Validation failed");
    }

    @Test
    public void getUserById_userExists_returnsUser() {
        UUID userId = UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bec2");

        UriComponents components = UriComponentsBuilder
                .fromPath(USER_CONTROLLER_PATH + "/" + userId)
                .build();

        UserDto userDto = requestSpecification(components)
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(UserDto.class);

        assertThat(userDto.id()).isEqualTo(userId);
    }

    @Test
    public void getUserById_invalidRequest_returnsValidationError() {

        UriComponents components = UriComponentsBuilder
                .fromPath(USER_CONTROLLER_PATH + "/not-a-uuid")
                .build();

        ValidationFailedResponse response = requestSpecification(components)
                .get()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .as(ValidationFailedResponse.class);

        assertThat(response.message()).isEqualTo("Validation failed");
    }

    @Test
    public void getUserById_userNotExists_returnsNotFoundError() {
        UUID userId = UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bed4");

        UriComponents components = UriComponentsBuilder
                .fromPath(USER_CONTROLLER_PATH + "/" + userId)
                .build();

        NotFoundResponse response = requestSpecification(components)
                .get()
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract()
                .as(NotFoundResponse.class);

        assertThat(response.message()).isEqualTo("User with id " + userId + " not found");
    }

    @Test
    public void updateUser_userExists_returnsUser() {
        UUID userId = UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bec2");

        UriComponents components = UriComponentsBuilder
                .fromPath(USER_CONTROLLER_PATH + "/" + userId)
                .build();

        UserUpdateDto userUpdateDto = new UserUpdateDto(
                Optional.of("NewName"),
                Optional.of("NewSurname"),
                Optional.of("987654321"),
                Optional.of("newemail@email.cz")
        );


        requestSpecification(components)
                .contentType(ContentType.JSON)
                .body(userUpdateDto)
                .put()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(UserDto.class);

        Optional<User> user = userRepository.findById(userId);
        assertThat(user).isPresent();

        User updatedUser = user.get();
        assertThat(updatedUser)
                .returns("NewName", User::getFirstName)
                .returns("NewSurname", User::getLastName)
                .returns("987654321", User::getPhoneNumber)
                .returns("newemail@email.cz", User::getEmail);

    }

    @Test
    public void updateUser_userNotExists_returnsNotFoundError() {
        UUID userId = UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bed4");
        assertThat(userRepository.findById(userId).isPresent()).isFalse();

        UriComponents components = UriComponentsBuilder
                .fromPath(USER_CONTROLLER_PATH + "/" + userId)
                .build();

        UserUpdateDto userUpdateDto = new UserUpdateDto(
                Optional.of("NewName"),
                Optional.of("NewSurname"),
                Optional.of("987654321"),
                Optional.of("newemail@email.cz")
        );

        requestSpecification(components)
                .contentType(ContentType.JSON)
                .body(userUpdateDto)
                .put()
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract()
                .as(NotFoundResponse.class);

        assertThat(userRepository.findById(userId).isPresent()).isFalse();
    }

    @Test
    public void deactivateUser_userNotExists_returnsNotFoundError() {
        UUID userId = UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bed4");
        assertThat(userRepository.findById(userId).isPresent()).isFalse();

        UriComponents components = UriComponentsBuilder
                .fromPath(USER_CONTROLLER_PATH + "/" + userId + "/deactivate")
                .build();


        requestSpecification(components)
                .post()
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract()
                .as(NotFoundResponse.class);

        assertThat(userRepository.findById(userId).isPresent()).isFalse();
    }

    @Test
    public void deactivateUser_deactivatedUser_doesNothing() {
        UUID userId = UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bec5");
        User origUser = userRepository.findById(userId).orElseThrow();
        assertThat(origUser.isActive()).isFalse();

        UriComponents components = UriComponentsBuilder
                .fromPath(USER_CONTROLLER_PATH + "/" + userId + "/deactivate")
                .build();


        UserDto userDto = requestSpecification(components)
                .post()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(UserDto.class);

        User updatedUser = userRepository.findById(userId).orElseThrow();
        assertThat(updatedUser).isEqualTo(origUser);
        assertThat(userDto.active()).isFalse();
    }

    @Test
    public void deactivateUser_activateUser_deactivatesUser() {
        UUID userId = UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bec2");
        User origUser = userRepository.findById(userId).orElseThrow();
        assertThat(origUser.isActive()).isTrue();

        UriComponents components = UriComponentsBuilder
                .fromPath(USER_CONTROLLER_PATH + "/" + userId + "/deactivate")
                .build();


        UserDto userDto = requestSpecification(components)
                .post()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(UserDto.class);

        User updatedUser = userRepository.findById(userId).orElseThrow();
        assertThat(updatedUser.isActive()).isFalse();
        assertThat(userDto.active()).isFalse();
    }

    @Test
    public void activateUser_userNotExists_returnsNotFoundError() {
        UUID userId = UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bed4");
        assertThat(userRepository.findById(userId).isPresent()).isFalse();

        UriComponents components = UriComponentsBuilder
                .fromPath(USER_CONTROLLER_PATH + "/" + userId + "/activate")
                .build();


        requestSpecification(components)
                .post()
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract()
                .as(NotFoundResponse.class);

        assertThat(userRepository.findById(userId).isPresent()).isFalse();
    }

    @Test
    public void activateUser_activatedUser_doesNothing() {
        UUID userId = UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bec2");
        User origUser = userRepository.findById(userId).orElseThrow();
        assertThat(origUser.isActive()).isTrue();

        UriComponents components = UriComponentsBuilder
                .fromPath(USER_CONTROLLER_PATH + "/" + userId + "/activate")
                .build();

        UserDto userDto = requestSpecification(components)
                .post()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(UserDto.class);

        User updatedUser = userRepository.findById(userId).orElseThrow();
        assertThat(updatedUser).isEqualTo(origUser);
        assertThat(userDto.active()).isTrue();
    }

    @Test
    public void activateUser_deactivatedUser_activatesUser() {
        UUID userId = UUID.fromString("5e4b3326-38b5-4484-8034-33d81f34bec5");
        User origUser = userRepository.findById(userId).orElseThrow();
        assertThat(origUser.isActive()).isFalse();

        UriComponents components = UriComponentsBuilder
                .fromPath(USER_CONTROLLER_PATH + "/" + userId + "/activate")
                .build();

        UserDto userDto = requestSpecification(components)
                .post()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(UserDto.class);

        User updatedUser = userRepository.findById(userId).orElseThrow();
        assertThat(updatedUser.isActive()).isTrue();
        assertThat(userDto.active()).isTrue();
    }
}
