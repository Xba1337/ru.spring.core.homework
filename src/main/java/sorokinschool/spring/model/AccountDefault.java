package sorokinschool.spring.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccountDefault {

    @Value("${account.default-amount}")
    private long defaultMoneyAmount;

    @Value("${account.transfer-commission}")
    private double transferCommission;


    public long getDefaultMoneyAmount() {
        return defaultMoneyAmount;
    }

    public double getTransferCommission() {
        return transferCommission;
    }
}
