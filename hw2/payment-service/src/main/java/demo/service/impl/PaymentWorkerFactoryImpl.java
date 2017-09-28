package demo.service.impl;

import demo.domain.Payment;
import demo.service.OrderService;
import demo.service.PaymentService;
import demo.service.PaymentWorkerFactory;
import demo.task.PaymentWorker;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
@NoArgsConstructor
@Slf4j
public class PaymentWorkerFactoryImpl implements PaymentWorkerFactory {
    private final AtomicLong instanceCounter = new AtomicLong();

    private PaymentService paymentService;
    private OrderService orderService;

    @Autowired
    public void PaymentWorkerFactoryImpl(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @Override
    public PaymentWorker preparePaymentWorker(Payment payment) {
        long id = instanceCounter.incrementAndGet();
        log.info("create payment worker: {}, {}", id, payment);
        return new PaymentWorker(id, payment, paymentService, orderService);
    }
}
