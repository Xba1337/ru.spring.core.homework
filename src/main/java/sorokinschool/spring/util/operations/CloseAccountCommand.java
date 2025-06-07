package sorokinschool.spring.util.operations;

import org.springframework.stereotype.Component;
import sorokinschool.spring.service.AccountService;

import java.util.Scanner;

@Component
public class CloseAccountCommand implements OperationHandler{

    private final Scanner scanner;
    private final AccountService accountService;

    public CloseAccountCommand(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        System.out.println("Введите ID счета, который Вы хотите закрыть: ");
        String id = scanner.nextLine();
        accountService.closeAccount(Integer.parseInt(id));
        System.out.printf("Счет с ID %s успешно закрыт.", Integer.parseInt(id));
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_CLOSE;
    }
}
