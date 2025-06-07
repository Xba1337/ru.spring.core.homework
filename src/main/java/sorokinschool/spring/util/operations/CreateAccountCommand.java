package sorokinschool.spring.util.operations;

import org.springframework.stereotype.Component;
import sorokinschool.spring.model.Account;
import sorokinschool.spring.service.AccountService;

import java.util.Scanner;

@Component
public class CreateAccountCommand implements OperationHandler{

    private final Scanner scanner;
    private final AccountService accountService;

    public CreateAccountCommand(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        System.out.println("Введите ID пользователя для создания счёта.");
        String id = scanner.nextLine();
        Account account = accountService.createAccount(Integer.parseInt(id));
        System.out.printf("Счёт с ID %s успешно создан", account.getId());
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_CREATE;
    }
}
