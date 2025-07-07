package sorokinschool.spring.util;

import org.springframework.stereotype.Component;
import sorokinschool.spring.util.operations.OperationHandler;
import sorokinschool.spring.util.operations.OperationType;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OperationConsoleListener{

    private final Scanner scanner;
    private final Map<OperationType, OperationHandler> mapOfOperations;

    public OperationConsoleListener(Scanner scanner, List<OperationHandler> operations) {
        this.scanner = scanner;
        this.mapOfOperations =  operations.stream()
                .collect(Collectors.toMap(OperationHandler::getOperationType,
                        operation -> operation));
    }

    public void startListener() {
        while (!Thread.currentThread().isInterrupted()){
            printOptions();

            String operation = scanner.nextLine();
            if (operation.equalsIgnoreCase("EXIT")) {
                Thread.currentThread().interrupt();
                continue;
            }

            try {
                OperationType operationType = OperationType.valueOf(operation);
                operate(operationType);
            } catch (IllegalArgumentException e) {
                System.out.printf("\nКоманда %s отсутствует в перечне возможных.", operation);
            }
        }
    }

    public void operate(OperationType operationType) {
        try {
            mapOfOperations.get(operationType).execute();
        } catch (Exception e) {
            System.out.printf("Error: \"%s\"", e.getMessage());
        }
    }

    public void printOptions(){
        System.out.println("\nПожалуйста, введите одну из операций, представленных ниже: ");
        mapOfOperations.keySet().forEach(System.out::println);
        System.out.println("Или введите \"EXIT\" для выхода из меню программы.");
    }
}
