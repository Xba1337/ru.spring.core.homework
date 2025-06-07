package sorokinschool.spring.util.operations;

import org.springframework.stereotype.Component;
import sorokinschool.spring.model.User;
import sorokinschool.spring.service.UserService;

import java.util.Scanner;

@Component
public class CreateUserCommand implements OperationHandler {

    private final Scanner scanner;
    private final UserService userService;

    public CreateUserCommand(Scanner scanner, UserService userService) {
        this.scanner = scanner;
        this.userService = userService;
    }

    @Override
    public void execute() {
        System.out.println("Введите логин для создания нового пользователя: ");
        String login = scanner.nextLine();
        User user = userService.createUser(login);
        System.out.println("Пользователь создан: " + user.toString());
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.USER_CREATE;
    }
}
