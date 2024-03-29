package cz.muni.fi.obs.data;

import cz.muni.fi.obs.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class UserAccountRepository {
    // TODO: replace with transaction-microservice api calls

    private final DataStore dataStore;

    @Autowired
    public UserAccountRepository(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Account create(Account account) {
        dataStore.accounts.add(account);

        return account;
    }

    public Account[] findByUserId(String userId) {
        ArrayList<Account> accounts = dataStore.accounts.stream().filter(a -> a.getUserId().equals(userId)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        Account[] array = new Account[accounts.size()];

        return accounts.toArray(array);
    }

}
