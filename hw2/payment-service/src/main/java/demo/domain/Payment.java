package demo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue
    private long id;

    private Status status;
    private String orderId;
    private Date createdAt;
    private Date updatedAt;
    private double price = 0;

    @PrePersist
    protected void onCreate() {
        this.status = Status.PENDING;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "year", column = @Column(name = "creditCard_year")),
            @AttributeOverride(name = "month", column = @Column(name = "creditCard_month")),
            @AttributeOverride(name = "number", column = @Column(name = "creditCard_number")),
            @AttributeOverride(name = "securityCode", column = @Column(name = "creditCard_securityCode"))
    })
    private CreditCard creditCard;

    public Payment(long id) {
        this.id = id;
    }
}
