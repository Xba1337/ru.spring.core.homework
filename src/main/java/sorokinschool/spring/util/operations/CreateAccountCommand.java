package sorokinschool.spring.util.operations;

import org.springframework.stereotype.Component;
import sorokinschool.spring.model.Account;
import sorokinschool.spring.model.User;
import sorokinschool.spring.service.AccountService;
import sorokinschool.spring.service.UserService;

import java.util.Scanner;

@Component
public class CreateAccountCommand implements OperationHandler{

    private final Scanner scanner;
    private final AccountService accountService;
    private final UserService userService;

    public CreateAccountCommand(Scanner scanner, AccountService accountService, UserService userService) {
        this.scanner = scanner;
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public void execute() {
        System.out.println("Введите ID пользователя для создания счёта.");
        String id = scanner.nextLine();
        User user = userService.getUserById(Integer.parseInt(id));

        Account account = accountService.createAccount(user);
        System.out.printf("Счёт с ID %s успешно создан", account.getId());
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_CREATE;
    }
}
