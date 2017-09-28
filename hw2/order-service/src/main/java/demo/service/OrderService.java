package demo.service;

import demo.domain.Order;

public interface OrderService {
    Iterable<Order> save(Iterable<Order> orders);

    int completeOrder(Order order);

    void deleteAll();

    Order findOne(String id);
}
