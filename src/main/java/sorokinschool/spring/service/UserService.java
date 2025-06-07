package sorokinschool.spring.service;

import org.springframework.stereotype.Component;
import sorokinschool.spring.model.Account;
import sorokinschool.spring.model.AccountDefault;
import sorokinschool.spring.model.User;
import sorokinschool.spring.storage.DataBase;

import java.util.List;

@Component
public class UserService {

    private final DataBase dataBase;
    private final AccountDefault accountDefault;

    public UserService(DataBase dataBase, AccountDefault accountDefault) {
        this.dataBase = dataBase;
        this.accountDefault = accountDefault;
    }

    public User createUser(String login) {
        if (dataBase.getUserByLogin(login).isPresent()) {
            throw new IllegalArgumentException("Пользователь с данным логином существует!");
        }

        User user = new User(dataBase.generateUserId(), login);
        Account account = new Account(dataBase.generateAccountId(),
                user.getId(),
                accountDefault.getDefaultMoneyAmount());

        user.addToAccountList(account);
        dataBase.addToUserDb(user);
        dataBase.addToAccountDb(account);

        return user;
    }

    public List<User> getUsers() {
        return dataBase.getUsers();
    }
}
