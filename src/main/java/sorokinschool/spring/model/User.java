package sorokinschool.spring.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final int id;

    private final String login;

    private final List<Account> accountList;

    public User(int id, String login) {
        this.id = id;
        this.login = login;
        this.accountList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void addToAccountList(Account account) {
        accountList.add(account);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", логин='" + login + '\'' +
                ", имеющиеся счета=" + accountList +
                '}';
    }
}
