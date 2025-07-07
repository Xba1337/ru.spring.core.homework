package sorokinschool.spring.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import sorokinschool.spring.model.Account;
import sorokinschool.spring.model.AccountDefault;
import sorokinschool.spring.model.User;
import sorokinschool.spring.util.TransactionHelper;

import java.util.Comparator;
import java.util.List;


@Service
public class AccountService {

    private final SessionFactory sessionFactory;
    private final AccountDefault accountDefault;
    private final TransactionHelper transactionHelper;

    public AccountService(SessionFactory sessionFactory, AccountDefault accountDefault, TransactionHelper transactionHelper) {
        this.sessionFactory = sessionFactory;
        this.accountDefault = accountDefault;
        this.transactionHelper = transactionHelper;
    }

    public Account findAccountById(int id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(Account.class, id);
    }

    public Account createAccount(User user) {
        return transactionHelper.executeTransaction(session -> {

            Account account = new Account(user,
                    accountDefault.getDefaultMoneyAmount());
            session.persist(account);

            return account;
        });
    }

    public long getNumberOfAccounts(int userId) {
        Session session = sessionFactory.openSession();

        return session.createQuery("SELECT COUNT(*) FROM Account a WHERE a.user.id = :user_id", Long.class)
                .setParameter("user_id", userId)
                .getSingleResult();
    }

    public void closeAccount(int accountId) {
        transactionHelper.executeTransaction(session -> {

            Account accountForClose = session.createQuery("SELECT a FROM Account a JOIN FETCH a.user u JOIN FETCH u.accountList WHERE a.id = :id",
                            Account.class)
                    .setParameter("id", accountId)
                    .uniqueResult();
            if (accountForClose == null) {
                throw new IllegalArgumentException("Счёт с ID %s не существует!".formatted(accountId));
            }

            if (getNumberOfAccounts(accountForClose.getUser().getId()) <= 1) {
                throw new IllegalArgumentException("Счёт %s не может быть удален, отсутствуют альтернативные счета для перевода средств!".formatted(accountId));
            }
            List<Account> accountList = accountForClose.getUser().getAccountList();
            Account accountForTransfer = accountList.stream()
                    .sorted(Comparator.comparing(Account::getId))
                    .filter(account -> account.getId() != accountId)
                    .findFirst()
                    .orElseThrow();

            accountForTransfer.setMoneyAmount(accountForTransfer.getMoneyAmount() + accountForClose.getMoneyAmount());
            session.remove(accountForClose);

            return 0;
        });
    }

    public void depositMoney(int accountId, int moneyAmount) {
        transactionHelper.executeTransaction(session -> {
            Account account = findAccountById(accountId);
            if (account == null) {
                throw new IllegalArgumentException("Счёт с ID %s не существует!".formatted(accountId));
            }
            account.setMoneyAmount(account.getMoneyAmount() + moneyAmount);

            return 0;
        });
    }

    public void transferMoney(int senderAccountId, int recipientAccountId, int moneyAmount) {
        transactionHelper.executeTransaction(session -> {
            if (senderAccountId == recipientAccountId) {
                throw new IllegalArgumentException("ID счёта-отправителя и получателя не могут быть одинаковыми.");
            }

            Account senderAccount = findAccountById(senderAccountId);
            if (senderAccount == null) {
                throw new IllegalArgumentException("Счёт с ID %s не существует!".formatted(senderAccount));
            }

            Account recipientAccount = findAccountById(recipientAccountId);
            if (recipientAccount == null) {
                throw new IllegalArgumentException("Счёт с ID %s не существует!".formatted(recipientAccount));
            }

            if (senderAccount.getMoneyAmount() < moneyAmount) {
                throw new IllegalArgumentException("На счете %s недостаточно средств".formatted(senderAccountId));
            }

            senderAccount.setMoneyAmount(senderAccount.getMoneyAmount() - moneyAmount);
            recipientAccount.setMoneyAmount((long) (recipientAccount.getMoneyAmount() + (moneyAmount - moneyAmount * accountDefault.getTransferCommission())));

            return 0;
        });
    }

    public void withdrawMoney(int accountId, int moneyAmount) {
        transactionHelper.executeTransaction(session -> {
            Account account = findAccountById(accountId);

            if (account == null) {
                throw new IllegalArgumentException("Счёт с ID %s не существует!".formatted(accountId));
            }

            if (account.getMoneyAmount() < moneyAmount) {
                throw new IllegalArgumentException("На счете %s недостаточно средств".formatted(accountId));
            }
            account.setMoneyAmount(account.getMoneyAmount() - moneyAmount);

            return 0;
        });
    }
}
