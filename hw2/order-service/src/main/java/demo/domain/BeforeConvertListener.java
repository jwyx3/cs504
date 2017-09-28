package demo.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import java.util.Date;

@Slf4j
public class BeforeConvertListener extends AbstractMongoEventListener<Order> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Order> event) {
        Order order = event.getSource();
        log.info("before convert order: {}", order);
        if (order.getId() == null) {
            order.setStatus(Status.PENDING);
            order.setCreatedAt(new Date());
            order.setUpdatedAt(new Date());
        } else {
            order.setUpdatedAt(new Date());
        }
        if (order.getItems() != null) {
            double totalPrice = order.getItems().stream().map(
                    (a) -> a.getQuantity() * a.getPrice()).reduce(.0, (a, b) -> a + b);
            log.info("calculate total price: {}", totalPrice);
            order.setTotalPrice(totalPrice);
        }
    }
}
