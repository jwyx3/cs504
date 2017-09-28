package demo.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {
    private String id;
    private Status status = Status.PENDING;
    private long paymentId;
}
