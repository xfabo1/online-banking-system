package cz.muni.fi.obs.data;

import cz.muni.fi.obs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final DataStore dataStore;

    @Autowired
    public UserRepository(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public User create(User user) {
        dataStore.users.add(user);

        return user;
    }

    public User update(User user) {
        User existingUser = dataStore.users.stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null);
        if (existingUser != null) {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setBirthDate(user.getBirthDate());
            existingUser.setBirthNumber(user.getBirthNumber());
            existingUser.setEmail(user.getEmail());
        }

        return existingUser;
    }

    public User findById(String userId) {
        return dataStore.users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
    }

    public User[] getAll() {
        User[] array = new User[dataStore.users.size()];

        return dataStore.users.toArray(array);
    }
}
