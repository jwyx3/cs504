package demo;

import demo.domain.Order;
import demo.domain.Status;
import demo.service.OrderService;
import demo.service.impl.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private LoadBalancerClient loadBalancer;

    private RestTemplate restTemplate;

    private OrderService service;

    private Order inputOrder;

    @Before
    public void setupMock() {
        loadBalancer = mock(LoadBalancerClient.class);
        restTemplate = mock(RestTemplate.class);
        service = new OrderServiceImpl(restTemplate, loadBalancer);
        inputOrder = new Order("order-1", Status.SUCCESS, 1);
    }

    @Test
    public void whenCompleteOrder_sendComplete() {
        ServiceInstance instance = mock(ServiceInstance.class);
        when(instance.getUri()).thenReturn(URI.create("http://localhost"));
        when(loadBalancer.choose("order-service")).thenReturn(instance);
        service.completeOrder(inputOrder);
        verify(restTemplate, times(1)).postForLocation(anyString(), any(Order.class));

        when(loadBalancer.choose("order-service")).thenReturn(null);
        service.completeOrder(inputOrder);
        verify(restTemplate, never());
    }
}
