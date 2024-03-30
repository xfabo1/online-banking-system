package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.UserCreateDto;
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
                Nationality.fromCode(userCreateDto.getNationality()),
                userCreateDto.getBirthNumber()
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

    public User getUser(String userId) {
        return userRepository.findById(userId);
    }

    public User[] getAllUsers() {
        return userRepository.getAll();
    }
}
