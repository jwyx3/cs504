package demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRequest {
    private String id;
    private Status status = Status.PENDING;
    private long paymentId;
}
