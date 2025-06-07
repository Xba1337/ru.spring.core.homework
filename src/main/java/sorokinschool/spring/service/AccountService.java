package sorokinschool.spring.service;

import org.springframework.stereotype.Component;
import sorokinschool.spring.model.Account;
import sorokinschool.spring.model.AccountDefault;
import sorokinschool.spring.model.User;
import sorokinschool.spring.storage.DataBase;

import java.util.Iterator;
import java.util.Optional;

@Component
public class AccountService {

    private final DataBase dataBase;

    private final AccountDefault accountDefault;

    public AccountService(DataBase dataBase, AccountDefault accountDefault) {
        this.dataBase = dataBase;
        this.accountDefault = accountDefault;
    }

    public Account createAccount(int userId) {
        User user = dataBase.getUserById(userId).orElseThrow(
                () -> new RuntimeException("Пользователь с ID %s не существует!".formatted(userId))
        );
        Account account = new Account(dataBase.generateAccountId(),
                user.getId(),
                accountDefault.getDefaultMoneyAmount());

        user.addToAccountList(account);
        dataBase.addToAccountDb(account);

        return account;
    }

    public void closeAccount(int accountId) {
        if (dataBase.getAccounts().isEmpty()) {
            throw new IllegalArgumentException("Счёт с ID %s не существует!".formatted(accountId));
        }

        Iterator<Account> accountList = dataBase.getAccounts().iterator();

        while (accountList.hasNext()) {
            Account accountForClosing = accountList.next();

            if (accountForClosing.getId() == accountId) {
                User user = dataBase.getUserById(accountForClosing.getUserId()).orElseThrow(
                        () -> new IllegalArgumentException("Счёт с ID %s не существует!".formatted(accountForClosing.getId()))
                );

                if (user.getAccountList().size() == 1) {
                    throw new IllegalArgumentException("Счёт %s не может быть удален, отсутствуют альтернативные счета для перевода средств!".formatted(accountId));
                } else {
                    Account firstAccount = user.getAccountList().get(0);
                    firstAccount.setMoneyAmount(firstAccount.getMoneyAmount() + accountForClosing.getMoneyAmount());
                    accountList.remove();
                    user.getAccountList().remove(accountForClosing);
                }
            }
        }
    }

    public void depositMoney(int accountId, int moneyAmount) {
        Optional<Account> account = dataBase.getAccountById(accountId);

        if (account.isPresent()) {
            account.get().setMoneyAmount(account.get().getMoneyAmount() + moneyAmount);
        } else throw new IllegalArgumentException("Счёт с ID %s не существует!".formatted(accountId));
    }

    public void transferMoney(int senderAccountId, int recipientAccountId, int moneyAmount) {
        if (senderAccountId == recipientAccountId) {
            throw new IllegalArgumentException("ID счёта-отправителя и получателя не могут быть одинаковыми.");
        }

        Account senderAccount = dataBase.getAccountById(senderAccountId).orElseThrow(
                () -> new IllegalArgumentException("Счёт с ID %s не существует!".formatted(senderAccountId))
        );

        Account recipientAccount = dataBase.getAccountById(recipientAccountId).orElseThrow(
                () -> new IllegalArgumentException("Счёт с ID %s не существует!".formatted(recipientAccountId))
        );

        if (senderAccount.getMoneyAmount() < moneyAmount) {
            throw new IllegalArgumentException("На счете %s недостаточно средств".formatted(senderAccountId));
        }

        long newMoneyAmountForSender = senderAccount.getMoneyAmount() - moneyAmount;
        long newMoneyAmountForRecipient = Math.round(recipientAccount.getMoneyAmount() +
                (moneyAmount - moneyAmount * accountDefault.getTransferCommission()));

        senderAccount.setMoneyAmount(newMoneyAmountForSender);
        recipientAccount.setMoneyAmount(newMoneyAmountForRecipient);
    }

    public void withdrawMoney(int accountId, int moneyAmount) {
        Account account = dataBase.getAccountById(accountId).orElseThrow(
                () -> new IllegalArgumentException("Счёт с ID %s не существует!".formatted(accountId))
        );

        if (account.getMoneyAmount() >= moneyAmount) {
            account.setMoneyAmount(account.getMoneyAmount() - moneyAmount);
        } else throw new IllegalArgumentException("На счете %s недостаточно средств".formatted(accountId));
    }
}
