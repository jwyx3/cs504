package demo.service.impl;

import demo.domain.Payment;
import demo.domain.PaymentRepository;
import demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository repository;

    @Autowired
    public void PaymentServiceImpl(PaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Payment> save(List<Payment> payments) {
        return repository.save(payments);
    }

    @Override
    public Payment save(Payment payment) {
        log.info("create payment: {}", payment);
        return repository.save(payment);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public Payment findFirstByOrderId(String orderId) {
        log.info("find first payment by orderId: {}", orderId);
        return repository.findFirstByOrderId(orderId);
    }
}
