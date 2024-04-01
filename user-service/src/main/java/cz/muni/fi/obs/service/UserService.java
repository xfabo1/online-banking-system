package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.UserCreateDto;
import cz.muni.fi.obs.api.UserSearchParamsDto;
import cz.muni.fi.obs.api.UserUpdateDto;
import cz.muni.fi.obs.data.UserRepository;
import cz.muni.fi.obs.domain.User;
import cz.muni.fi.obs.enums.Nationality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
                Nationality.fromString(userCreateDto.nationality()),
                userCreateDto.birthNumber(),
                true
        );
        return userRepository.create(user);
    }

    public User updateUser(String userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId);
        user.setFirstName(userUpdateDto.firstName());
        user.setLastName(userUpdateDto.lastName());
        user.setPhoneNumber(userUpdateDto.phoneNumber());
        user.setEmail(userUpdateDto.email());

        return userRepository.update(user);
    }

    public User deactivateUser(String userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return null;
        }

        user.setActive(false);
        return userRepository.update(user);
    }

    public User activateUser(String userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return null;
        }

        user.setActive(true);
        return userRepository.update(user);
    }

    public User getUser(String userId) {
        return userRepository.findById(userId);
    }

    public Page<User> findUsers(UserSearchParamsDto searchParams) {
        return userRepository.find(searchParams);
    }
}
