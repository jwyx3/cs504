package demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.domain.Order;
import demo.domain.Status;
import demo.rest.PaymentServiceApi;
import demo.service.PaymentService;
import demo.service.PaymentWorkerFactory;
import demo.service.impl.OrderServiceImpl;
import org.junit.Before;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

public class PaymentServiceApiTest {

    private PaymentService paymentService;

    private PaymentWorkerFactory paymentWorkerFactory;

    private AsyncTaskExecutor taskExecutor;

    private ObjectMapper objectMapper;

    private PaymentServiceApi api;

    @Before
    public void setupMock() {
        paymentService = mock(PaymentService.class);
        paymentWorkerFactory = mock(PaymentWorkerFactory.class);
        taskExecutor = mock(AsyncTaskExecutor.class);
        objectMapper = mock(ObjectMapper.class);
        api = new PaymentServiceApi(paymentService, paymentWorkerFactory, taskExecutor, objectMapper);
    }
}
