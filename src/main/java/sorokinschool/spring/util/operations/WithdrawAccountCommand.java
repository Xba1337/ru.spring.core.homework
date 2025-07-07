package sorokinschool.spring.util.operations;

import org.springframework.stereotype.Component;
import sorokinschool.spring.service.AccountService;

import java.util.Scanner;

@Component
public class WithdrawAccountCommand implements OperationHandler{

    private final Scanner scanner;
    private final AccountService accountService;

    public WithdrawAccountCommand(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        System.out.println("Введите ID счёта для снятия средств: ");
        String id = scanner.nextLine();
        System.out.println("Введите сумму, которую хотите снять: ");
        String amount = scanner.nextLine();
        accountService.withdrawMoney(Integer.parseInt(id), Integer.parseInt(amount));
        System.out.printf("Средства в размере %s со счета ID %s успешно получены.", amount, id);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_WITHDRAW;
    }
}
