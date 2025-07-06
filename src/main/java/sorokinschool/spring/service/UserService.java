package sorokinschool.spring.service;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import sorokinschool.spring.model.User;
import sorokinschool.spring.util.TransactionHelper;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final SessionFactory sessionFactory;
    private final AccountService accountService;
    private final TransactionHelper transactionHelper;

    public UserService(SessionFactory sessionFactory, AccountService accountService, TransactionHelper transactionHelper) {
        this.sessionFactory = sessionFactory;
        this.accountService = accountService;
        this.transactionHelper = transactionHelper;
    }

    @Transactional
    public User createUser(String login) {
        return transactionHelper.executeTransaction(session -> {

            User user = session.createQuery("FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();

            if (user != null) {
                throw new IllegalArgumentException("Пользователь с логином: %s существует".formatted(login));
            }

            user = new User(null, login, new ArrayList<>());
            session.persist(user);
            accountService.createAccount(user);

            return user;
        });
    }

    public User getUserById(int id) {
        Session session = sessionFactory.openSession();
        User user = session.find(User.class, id);
        session.close();

        return user;
    }

    public List<User> getUsers() {
        Session session = sessionFactory.openSession();
        List<User> users = session.createQuery("from User", User.class).list();
        session.close();

        return users;
    }
}
