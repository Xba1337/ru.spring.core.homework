package sorokinschool.spring.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class OperationConsoleListenerThreadStarter {

    private final OperationConsoleListener consoleListener;
    private Thread thread;

    public OperationConsoleListenerThreadStarter(OperationConsoleListener consoleListener) {
        this.consoleListener = consoleListener;
    }

    @PostConstruct
    public void postConstruct() {
        this.thread = new Thread(consoleListener::startListener);
        thread.start();
    }

    @PreDestroy
    public void preDestroy() {
        thread.interrupt();
    }
}
