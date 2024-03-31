package cz.muni.fi.obs.data;

import cz.muni.fi.obs.api.PagedResponse;
import cz.muni.fi.obs.api.Pagination;
import cz.muni.fi.obs.api.UserSearchParamsDto;
import cz.muni.fi.obs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    public PagedResponse<User> find(UserSearchParamsDto searchParams) {
        List<User> filteredUsers = new ArrayList<>();
        for (User user : dataStore.users) {
            if ((searchParams.getFirstName() == null || user.getFirstName().startsWith(searchParams.getFirstName())) &&
                    (searchParams.getLastName() == null || user.getLastName().startsWith(searchParams.getLastName())) &&
                    (searchParams.getPhoneNumber() == null || user.getPhoneNumber().startsWith(searchParams.getPhoneNumber())) &&
                    (searchParams.getEmail() == null || user.getEmail().startsWith(searchParams.getEmail())) &&
                    (searchParams.getBirthDate() == null || user.getBirthDate().equals(searchParams.getBirthDate())) &&
                    (searchParams.getBirthNumber() == null || user.getBirthNumber().startsWith(searchParams.getBirthNumber())) &&
                    user.isActive() == searchParams.isActive()) {
                filteredUsers.add(user);
            }
        }

        int fromIndex = searchParams.getPage() * searchParams.getPageSize();
        int toIndex = Math.min(fromIndex + searchParams.getPageSize(), filteredUsers.size());
        List<User> pagedUsers = filteredUsers.subList(fromIndex, toIndex);

        return new PagedResponse<User>(
                pagedUsers,
                new Pagination(
                        filteredUsers.size(),
                        filteredUsers.size() / searchParams.getPageSize() + (filteredUsers.size() % searchParams.getPageSize() == 0 ? 0 : 1),
                        searchParams.getPage(),
                        pagedUsers.size()

                )
        );
    }
}
