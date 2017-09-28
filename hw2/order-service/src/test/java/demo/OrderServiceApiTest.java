package demo;

import demo.domain.Order;
import demo.domain.OrderRequest;
import demo.domain.Status;
import demo.rest.OrderServiceApi;
import demo.service.OrderService;
import demo.service.PaymentService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class OrderServiceApiTest {

    private OrderService orderService;

    private PaymentService paymentService;

    private OrderServiceApi api;

    private List<Order> inputOrders;

    @Before
    public void setupMock() {
        orderService = mock(OrderService.class);
        paymentService = mock(PaymentService.class);
        api = new OrderServiceApi(orderService, paymentService);

        inputOrders = new ArrayList<>();
        inputOrders.add(generateOrder("order-1", 10));
        inputOrders.add(generateOrder("order-2", 15));
    }

    private Order generateOrder(String id, double price) {
        Order order = new Order(id);
        order.setTotalPrice(price);
        return order;
    }

    @Test
    public void whenUpload_returnOrders() {
        when(orderService.save(inputOrders)).thenReturn(inputOrders);
        api.upload(inputOrders);
        verify(paymentService, times(inputOrders.size())).processPayment(any());
    }

    @Test
    public void whenPurge_removeOrders() {
        api.purge();
        verify(orderService, times(1)).deleteAll();
    }

    @Test
    public void whenComplete_updateOrder() {
        OrderRequest orderRequest = new OrderRequest("order-1", Status.SUCCESS, 1);
        api.complete(orderRequest);
        verify(orderService, times(1)).completeOrder(any(Order.class));
    }
}
