package demo.service.impl;

import demo.domain.Order;
import demo.domain.OrderRepository;
import demo.domain.OrderRequest;
import demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public void OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Iterable<Order> save(Iterable<Order> orders) {
        return orderRepository.save(orders);
    }

    @Override
    public int completeOrder(Order order) {
        return orderRepository.completeOrder(order);
    }

    @Override
    public void deleteAll() {
        orderRepository.deleteAll();
    }

    @Override
    public Order findOne(String id) {
        return orderRepository.findOne(id);
    }
}
