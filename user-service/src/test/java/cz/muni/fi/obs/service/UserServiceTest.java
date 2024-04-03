package cz.muni.fi.obs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cz.muni.fi.obs.api.UserCreateDto;
import cz.muni.fi.obs.api.UserUpdateDto;
import cz.muni.fi.obs.data.UserRepository;
import cz.muni.fi.obs.domain.User;
import cz.muni.fi.obs.enums.Nationality;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void createUser_userCreated_returnsUser() {
		UserCreateDto userCreateDto = new UserCreateDto("Joe", "Doe", "123456789", "test@gmail.com",
				new Date(), Nationality.CZ.name(), "900101/1234");
		User user = new User("Joe", "Doe", "123456789", "test@gmail.com",
				new Date(), Nationality.CZ, "900101/123", true);
		when(userRepository.create(any())).thenReturn(user);

		User response = userService.createUser(userCreateDto);

		verify(userRepository).create(any());
		assertThat(response).isEqualTo(user);
	}

	@Test
	void getUser_userFound_returnsUser() {
		User user = new User("Joe", "Doe", "123456789", "test@gmail.com",
				new Date(), Nationality.CZ, "900101/123", true);
		when(userRepository.findById("1")).thenReturn(user);

		User response = userService.getUser("1");

		verify(userRepository).findById("1");
		assertThat(response).isEqualTo(user);
	}

	@Test
	void updateUser_userUpdated_returnsUser() {
		UserUpdateDto userUpdateDto = new UserUpdateDto("Joe", "Doe", "123456789", "test@gmail.com");
		User user = new User("Joe", "Doe", "123456789", "test@gmail.com",
				new Date(), Nationality.CZ, "900101/123", true);
		when(userRepository.update(user)).thenReturn(user);
		when(userRepository.findById("1")).thenReturn(user);

		User response = userService.updateUser("1", userUpdateDto);

		verify(userRepository).update(user);
		assertThat(response).isEqualTo(user);
	}

	@Test
	void deactivateUser_userDeactivated_returnsUser() {
		User user = new User("Joe", "Doe", "123456789", "test@gmail.com",
				new Date(), Nationality.CZ, "900101/123", true);
		when(userRepository.findById("1")).thenReturn(user);
		when(userRepository.update(user)).thenReturn(user);

		User response = userService.deactivateUser("1");

		verify(userRepository).findById("1");
		assertThat(response).isEqualTo(user);
	}

	@Test
	void activateUser_userActivated_returnsUser() {
		User user = new User("Joe", "Doe", "123456789", "test@gmail.com",
				new Date(), Nationality.CZ, "900101/123", true);
		when(userRepository.findById("1")).thenReturn(user);
		when(userRepository.update(user)).thenReturn(user);

		User response = userService.activateUser("1");

		verify(userRepository).findById("1");
		assertThat(response).isEqualTo(user);
	}
}
