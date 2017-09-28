package demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @JsonIgnore
    private static final Random random = new Random(System.currentTimeMillis());

    @Id
    private String id;
    private String note;
    private Status status;
    private Date createdAt;
    private Date updatedAt;
    private double totalPrice = 0;
    private long restaurantId;
    private long paymentId;
    private String deliveryCustomer;
    private Address deliveryAddress;
    private int deliveryEstimatedTime;
    private List<Item> items;
    private CreditCard creditCard;

    public Order(String id) {
        this.id = id;
        this.status = Status.PENDING;
    }

    public Order(OrderRequest orderRequest) {
        this.id = orderRequest.getId();
        this.paymentId = orderRequest.getPaymentId();
        this.status = orderRequest.getStatus();
        this.deliveryEstimatedTime = random.nextInt(55 + 1) + 5;
    }
}
