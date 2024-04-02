package cz.muni.fi.obs.data;

import cz.muni.fi.obs.api.UserSearchParamsDto;
import cz.muni.fi.obs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

    public Page<User> find(UserSearchParamsDto searchParams) {
        List<User> filteredUsers = new ArrayList<>();
        for (User user : dataStore.users) {
            if ((searchParams.firstName() == null || user.getFirstName().startsWith(searchParams.firstName())) &&
                    (searchParams.lastName() == null || user.getLastName().startsWith(searchParams.lastName())) &&
                    (searchParams.phoneNumber() == null || user.getPhoneNumber().startsWith(searchParams.phoneNumber())) &&
                    (searchParams.email() == null || user.getEmail().startsWith(searchParams.email())) &&
                    (searchParams.birthDate() == null || user.getBirthDate().equals(searchParams.birthDate())) &&
                    (searchParams.birthNumber() == null || user.getBirthNumber().startsWith(searchParams.birthNumber())) &&
                    (searchParams.active() == null || user.getActive() == searchParams.active())) {
                filteredUsers.add(user);
            }
        }
        if (searchParams.pageable() == null) {
            return new PageImpl<>(filteredUsers);
        }

        int fromIndex = (int) searchParams.pageable().getOffset();
        int toIndex = fromIndex + searchParams.pageable().getPageSize();
        List<User> pagedUsers = filteredUsers.subList(fromIndex, toIndex);

        return new PageImpl<>(pagedUsers, searchParams.pageable(), filteredUsers.size());
    }
}
