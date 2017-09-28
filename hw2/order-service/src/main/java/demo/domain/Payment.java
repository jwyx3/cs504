package demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private String orderId;
    private double price = 0;
    private CreditCard creditCard;

    public Payment(Order order) {
        this.orderId = order.getId();
        this.price = order.getTotalPrice();
        this.creditCard = order.getCreditCard();
    }
}
