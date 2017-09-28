package demo.rest;

import com.google.common.collect.Lists;
import demo.domain.Order;
import demo.domain.OrderRequest;
import demo.domain.Payment;
import demo.service.OrderService;
import demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@EnableBinding(Source.class)
@RestController
@Slf4j
public class OrderServiceApi {

    private OrderService orderService;
    private PaymentService paymentService;

    @Autowired
    public OrderServiceApi(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    //1. create orders
    //2. create payment using rabbitmq
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public List<Order> upload(@RequestBody List<Order> orders) {
        List<Order> newOrders = Lists.newArrayList(orderService.save(orders));
        log.info("created orders {}", newOrders);
        for (Order order : newOrders) {
            paymentService.processPayment(new Payment(order));
        }
        return newOrders;
    }

    @RequestMapping(value = "/purge", method = RequestMethod.DELETE)
    public void purge() {
        orderService.deleteAll();
    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public void complete(@RequestBody OrderRequest orderRequest) {
        log.info("order request: {}", orderRequest);
        orderService.completeOrder(new Order(orderRequest));
    }

    @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
    public Order findById(@PathVariable("id") String id) {
        return orderService.findOne(id);
    }
}
