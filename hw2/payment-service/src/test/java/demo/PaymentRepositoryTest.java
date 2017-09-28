package demo;

import demo.domain.Order;
import demo.domain.Payment;
import demo.domain.PaymentRepository;
import demo.domain.Status;
import org.apache.commons.collections.iterators.ArrayIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PaymentServiceApplication.class)
@WebAppConfiguration
@DataJpaTest
public class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository repository;

    private Payment generatePayment(long id, String orderId) {
        Payment payment = new Payment(id);
        payment.setOrderId(orderId);
        return payment;
    }

    @Test
    public void findFirstByOrderId() {
        long paymentId = 1;
        String orderId = "order-1";
        Payment payment = generatePayment(paymentId, orderId);
        repository.save(Arrays.asList(payment));
        payment = repository.findFirstByOrderId(orderId);
        assertThat(payment.getId()).isEqualTo(paymentId);
        assertThat(payment.getOrderId()).isEqualTo(orderId);
    }
}
