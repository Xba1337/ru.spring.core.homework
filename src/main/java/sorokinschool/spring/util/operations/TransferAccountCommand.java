package sorokinschool.spring.util.operations;

import org.springframework.stereotype.Component;
import sorokinschool.spring.service.AccountService;

import java.util.Scanner;

@Component
public class TransferAccountCommand implements OperationHandler{

    private final Scanner scanner;
    private final AccountService accountService;

    public TransferAccountCommand(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        System.out.println("Введите ID счёта-отправителя: ");
        String senderId = scanner.nextLine();
        System.out.println("Введите ID счёта-получателя: ");
        String recipientId = scanner.nextLine();
        System.out.println("Введите сумму, которую хотите перевести: ");
        String amount = scanner.nextLine();
        try {
            accountService.transferMoney(Integer.parseInt(senderId),
                    Integer.parseInt(recipientId),
                    Integer.parseInt(amount));
            System.out.printf("Средства в размере %s успешно переведены со счёта ID %s на счёт ID %s.",
                    amount, senderId ,recipientId );
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: \"%s\"", e.getMessage());
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_TRANSFER;
    }
}
