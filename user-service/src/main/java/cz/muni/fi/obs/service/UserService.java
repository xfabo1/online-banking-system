package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.PagedResponse;
import cz.muni.fi.obs.api.UserCreateDto;
import cz.muni.fi.obs.api.UserSearchParamsDto;
import cz.muni.fi.obs.api.UserUpdateDto;
import cz.muni.fi.obs.data.UserRepository;
import cz.muni.fi.obs.domain.User;
import cz.muni.fi.obs.enums.Nationality;
import org.springframework.beans.factory.annotation.Autowired;
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
                userCreateDto.getFirstName(),
                userCreateDto.getLastName(),
                userCreateDto.getPhoneNumber(),
                userCreateDto.getEmail(),
                userCreateDto.getBirthDate(),
                Nationality.fromString(userCreateDto.getNationality()),
                userCreateDto.getBirthNumber(),
                true
        );
        return userRepository.create(user);
    }

    public User updateUser(String userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId);
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setPhoneNumber(userUpdateDto.getPhoneNumber());
        user.setEmail(userUpdateDto.getEmail());

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

    public PagedResponse<User> findUsers(UserSearchParamsDto searchParams) {
        return userRepository.find(searchParams);
    }
}
