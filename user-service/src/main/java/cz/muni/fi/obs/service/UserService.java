package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.UserCreateDto;
import cz.muni.fi.obs.api.UserSearchParamsDto;
import cz.muni.fi.obs.api.UserUpdateDto;
import cz.muni.fi.obs.data.dbo.User;
import cz.muni.fi.obs.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserCreateDto userCreateDto) {
        User user = new User(
                userCreateDto.firstName(),
                userCreateDto.lastName(),
                userCreateDto.phoneNumber(),
                userCreateDto.email(),
                userCreateDto.birthDate(),
                userCreateDto.nationality(),
                userCreateDto.birthNumber(),
                true
        );
        return userRepository.save(user);
    }

    public User updateUser(UUID userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findByIdOrThrow(userId);

        userUpdateDto.firstName().ifPresent(user::setFirstName);
        userUpdateDto.lastName().ifPresent(user::setLastName);
        userUpdateDto.phoneNumber().ifPresent(user::setPhoneNumber);
        userUpdateDto.email().ifPresent(user::setEmail);

        return userRepository.save(user);
    }

    public User deactivateUser(UUID userId) {
        User user = userRepository.findByIdOrThrow(userId);

        user.setActive(false);

        return userRepository.save(user);
    }

    public User activateUser(UUID userId) {
        User user = userRepository.findByIdOrThrow(userId);

        user.setActive(true);

        return userRepository.save(user);
    }

    public User getUser(UUID userId) {
        return userRepository.findByIdOrThrow(userId);
    }

    public Page<User> findUsers(UserSearchParamsDto searchParams) {
        return userRepository.findBySearchParams(
                searchParams.firstName(),
                searchParams.lastName(),
                searchParams.phoneNumber(),
                searchParams.email(),
                searchParams.birthDate(),
                searchParams.birthNumber(),
                searchParams.active().isEmpty() ? Optional.of(true) : searchParams.active(),
                searchParams.pageable()
        );
    }
}
