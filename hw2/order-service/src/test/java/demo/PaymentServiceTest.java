package demo;

import demo.domain.Payment;
import demo.service.PaymentService;
import demo.service.impl.PaymentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    private MessageChannel output;

    private PaymentService paymentService;

    private Payment payment;

    private Message<Payment> message;

    @Before
    public void setupMock(){
        output = mock(MessageChannel.class);
        paymentService = new PaymentServiceImpl(output);
        payment = new Payment();
        message = MessageBuilder.withPayload(payment).build();
    }

    @Test
    public void whenProcessPayment_sendPayment(){
        paymentService.processPayment(payment);
        verify(output, times(1)).send(any());
    }
}
