package demo.service;

import demo.domain.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> save(List<Payment> payments);

    Payment save(Payment payment);

    void deleteAll();

    Payment findFirstByOrderId(String orderId);
}
