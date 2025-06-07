package sorokinschool.spring.util.operations;

import org.springframework.stereotype.Component;
import sorokinschool.spring.service.AccountService;

import java.util.Scanner;

@Component
public class DepositAccountCommand implements OperationHandler{

    private final Scanner scanner;
    private final AccountService accountService;

    public DepositAccountCommand(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        System.out.println("Введите ID счёта для внесения средств: ");
        String id = scanner.nextLine();
        System.out.println("Введите сумму, на которую хотите пополнить счет: ");
        String amount = scanner.nextLine();
        accountService.depositMoney(Integer.parseInt(id), Integer.parseInt(amount));
        System.out.printf("Средства в размере %s успешно внесены на счет ID %s", amount, id);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_DEPOSIT;
    }
}
