package sorokinschool.spring.storage;

import org.springframework.stereotype.Component;
import sorokinschool.spring.model.Account;
import sorokinschool.spring.model.User;

import java.util.List;
import java.util.Optional;

@Component
public class DataBase {

    private final List<Account> accounts;
    private final List<User> users;

    private static int USER_ID_COUNTER = 0;
    private static int ACCOUNT_ID_COUNTER = 0;

    public DataBase(List<Account> accounts, List<User> users) {
        this.accounts = accounts;
        this.users = users;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
    public List<User> getUsers() {
        return users;
    }

    public int generateAccountId() {
        return ++ACCOUNT_ID_COUNTER;
    }

    public int generateUserId() {
        return ++USER_ID_COUNTER;
    }

    public Optional<Account> getAccountById(int id) {
        return accounts.stream()
                .filter(account -> account.getId() == id)
                .findFirst();
    }

    public Optional<User> getUserById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    public Optional<User> getUserByLogin(String login) {
        return users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }

    public void addToAccountDb(Account account) {
        accounts.add(account);
    }

    public void addToUserDb(User user) {
        users.add(user);
    }
}
