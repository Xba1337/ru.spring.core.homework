package sorokinschool.spring.util.operations;

import org.springframework.stereotype.Component;
import sorokinschool.spring.model.User;
import sorokinschool.spring.service.UserService;

import java.util.List;
import java.util.Scanner;

@Component
public class ShowUsersCommand implements OperationHandler{

    private final Scanner scanner;
    private final UserService userService;

    public ShowUsersCommand(Scanner scanner, UserService userService) {
        this.scanner = scanner;
        this.userService = userService;
    }

    @Override
    public void execute() {
        List<User> users = userService.getUsers();
        users.forEach(System.out::println);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.SHOW_ALL_USERS;
    }
}
