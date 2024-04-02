package cz.muni.fi.obs.data;

import cz.muni.fi.obs.domain.Account;
import cz.muni.fi.obs.domain.User;
import cz.muni.fi.obs.enums.Nationality;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Configuration
public class DataStore {


    public ArrayList<User> users = new ArrayList<>();
    public ArrayList<Account> accounts = new ArrayList<>();

    public DataStore() {
        User john = new User("John", "Doe", "+420 123 456 789", "john@doe.cz", new GregorianCalendar(1990, Calendar.JANUARY, 1).getTime(), Nationality.CZ, "900101/1234", true);
        User jane = new User("Jane", "Doe", "+420 987 654 321", "jane@doe.com", new GregorianCalendar(1995, Calendar.FEBRUARY, 5).getTime(), Nationality.SK, "950205/1234", true);

        users.add(john);
        users.add(jane);

        Account johnAccount1 = new Account(john.getId(), "1234567890", "CZK");
        Account johnAccount2 = new Account(john.getId(), "0987654321", "EUR");

        Account janeAccount1 = new Account(jane.getId(), "6567541655", "CZK");
        Account janeAccount2 = new Account(jane.getId(), "9651235965", "EUR");

        accounts.add(johnAccount1);
        accounts.add(johnAccount2);
        accounts.add(janeAccount1);
        accounts.add(janeAccount2);
    }
}