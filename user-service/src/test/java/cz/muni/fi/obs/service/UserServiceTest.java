package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.UserCreateDto;
import cz.muni.fi.obs.api.UserUpdateDto;
import cz.muni.fi.obs.data.dbo.User;
import cz.muni.fi.obs.data.enums.Nationality;
import cz.muni.fi.obs.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_userCreated_returnsUser() {
        UserCreateDto userCreateDto = new UserCreateDto("Joe",
                                                        "Doe",
                                                        "123456789",
                                                        "test@gmail.com",
                                                        LocalDate.now(),
                                                        Nationality.CZ,
                                                        "900101/1234"
        );

        User user = new User(userCreateDto.firstName(),
                             userCreateDto.lastName(),
                             userCreateDto.phoneNumber(),
                             userCreateDto.email(),
                             userCreateDto.birthDate(),
                             userCreateDto.nationality(),
                             userCreateDto.birthNumber(),
                             true
        );
        when(userRepository.save(user)).thenReturn(user);
        User response = userService.createUser(userCreateDto);

        verify(userRepository).save(user);
        assertThat(response).isEqualTo(user);
    }

    @Test
    void createUser_userEmailAlreadyExists_throwsException() {
        UserCreateDto userCreateDto = new UserCreateDto("Joe",
                                                        "Doe",
                                                        "123456789",
                                                        "test@gmail.com",
                                                        LocalDate.now(),
                                                        Nationality.CZ,
                                                        "900101/1234"
        );

        User user = new User(userCreateDto.firstName(),
                             userCreateDto.lastName(),
                             userCreateDto.phoneNumber(),
                             userCreateDto.email(),
                             userCreateDto.birthDate(),
                             userCreateDto.nationality(),
                             userCreateDto.birthNumber(),
                             true
        );
        when(userRepository.save(user)).thenReturn(user);
        User response = userService.createUser(userCreateDto);

        verify(userRepository).save(user);
        assertThat(response).isEqualTo(user);
    }

    @Test
    void getUser_userFound_returnsUser() {
        User user = new User("Joe",
                             "Doe",
                             "123456789",
                             "test@gmail.com",
                             LocalDate.now(),
                             Nationality.CZ,
                             "900101" + "/123",
                             true
        );
        when(userRepository.findByIdOrThrow(user.getId())).thenReturn(user);

        User response = userService.getUser(user.getId());

        verify(userRepository).findByIdOrThrow(user.getId());
        assertThat(response).isEqualTo(user);
    }

    @Test
    void updateUser_userUpdated_returnsUser() {
        UserUpdateDto userUpdateDto = new UserUpdateDto(Optional.of("Joe"),
                                                        Optional.of("Doe"),
                                                        Optional.of("123456789"),
                                                        Optional.of("test@gmail.com")
        );
        User user = new User("Joe",
                             "Doe",
                             "123456789",
                             "test@gmail.com",
                             LocalDate.now(),
                             Nationality.CZ,
                             "900101" + "/123",
                             true
        );
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByIdOrThrow(user.getId())).thenReturn(user);

        User response = userService.updateUser(user.getId(), userUpdateDto);

        verify(userRepository).save(user);
        assertThat(response).isEqualTo(user);
    }

    @Test
    void deactivateUser_userDeactivated_returnsUser() {
        User user = new User("Joe",
                             "Doe",
                             "123456789",
                             "test@gmail.com",
                             LocalDate.now(),
                             Nationality.CZ,
                             "900101" + "/123",
                             true
        );
        when(userRepository.findByIdOrThrow(user.getId())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User response = userService.deactivateUser(user.getId());

        verify(userRepository).findByIdOrThrow(user.getId());
        assertThat(response).isEqualTo(user);
    }

    @Test
    void activateUser_userActivated_returnsUser() {
        User user = new User("Joe",
                             "Doe",
                             "123456789",
                             "test@gmail.com",
                             LocalDate.now(),
                             Nationality.CZ,
                             "900101" + "/123",
                             true
        );
        when(userRepository.findByIdOrThrow(user.getId())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User response = userService.activateUser(user.getId());

        verify(userRepository).findByIdOrThrow(user.getId());
        assertThat(response).isEqualTo(user);
    }
}
