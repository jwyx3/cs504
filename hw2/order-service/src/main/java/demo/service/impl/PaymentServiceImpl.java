package demo.service.impl;

import demo.domain.Payment;
import demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private MessageChannel output;

    @Autowired
    public PaymentServiceImpl(MessageChannel output) {
        this.output = output;
    }

    @Override
    public void processPayment(Payment payment) {
        log.info("sent payment message {}", payment);
        output.send(MessageBuilder.withPayload(payment).build());
    }
}
