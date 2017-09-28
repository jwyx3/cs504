package demo;

import demo.domain.Order;
import demo.domain.OrderRepository;
import demo.domain.OrderRequest;
import demo.domain.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
@WebAppConfiguration
public class OrderRepositoryTest {
    @Autowired
    OrderRepository repository;

    @Test
    public void completeOrder() {
        String orderId = "order-1";
        long paymentId = 1;
        OrderRequest orderRequest = new OrderRequest(orderId, Status.SUCCESS, paymentId);
        Order order = new Order(orderId);

        repository.save(order);
        order = repository.findOne(orderId);
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getStatus()).isEqualTo(Status.PENDING);
        assertThat(order.getPaymentId()).isEqualTo(0L);

        repository.completeOrder(new Order(orderRequest));
        order = repository.findOne(orderId);
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getStatus()).isEqualTo(Status.SUCCESS);
        assertThat(order.getPaymentId()).isEqualTo(paymentId);
        assertThat(order.getDeliveryEstimatedTime()).isBetween(5, 60);
    }
}
