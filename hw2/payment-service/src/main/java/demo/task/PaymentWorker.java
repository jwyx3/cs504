package demo.task;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import demo.domain.Order;
import demo.domain.Payment;
import demo.domain.Status;
import demo.service.OrderService;
import demo.service.PaymentService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Slf4j
public class PaymentWorker implements Runnable {
    final static Random random = new Random(System.currentTimeMillis());

    private PaymentService paymentService;
    private OrderService orderService;

    private long id;
    private Payment payment;
    private AtomicBoolean cancelled = new AtomicBoolean(false);

    public PaymentWorker(long id, Payment payment, PaymentService paymentService, OrderService orderService) {
        this.id = id;
        this.payment = payment;
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @Override
    public void run() {
        try {
            if (cancelled.get()) {
                destroy();
                return;
            }
            if (payment != null) {
                Thread.sleep(10000);
                // create failure by 1/10 probability
                if (random.nextInt(10) < 1) {
                    payment.setStatus(Status.FAILURE);
                } else {
                    payment.setStatus(Status.SUCCESS);
                }
                paymentService.save(payment);

                completeOrder();
            }
        } catch (InterruptedException e) {
            destroy();
            return;
        }

        destroy();
    }

    @HystrixCommand(fallbackMethod = "completeOrderFallback")
    public void completeOrder() {
        orderService.completeOrder(new Order(payment.getOrderId(), payment.getStatus(), payment.getId()));
    }

    public void completeOrderFallback() {
        log.error("Hystrix Fallback Method. Unable to send message for order-service.");
    }

    public synchronized void cancel() {
        cancelled.set(true);
    }

    public void destroy() {
        payment = null;
    }
}
