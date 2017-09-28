package demo.service;

import demo.domain.Payment;
import demo.task.PaymentWorker;

public interface PaymentWorkerFactory {
    PaymentWorker preparePaymentWorker(Payment payment);
}
