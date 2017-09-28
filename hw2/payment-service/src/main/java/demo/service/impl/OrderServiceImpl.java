package demo.service.impl;

import demo.domain.Order;
import demo.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private LoadBalancerClient loadBalancer;

    private RestTemplate restTemplate;

    @Autowired
    public OrderServiceImpl(RestTemplate restTemplate, LoadBalancerClient loadBalancer) {
        this.restTemplate = restTemplate;
        this.loadBalancer = loadBalancer;
    }

    @Override
    public void completeOrder(Order order) {
        log.info(String.format("Thread %d complete order %s with status %s",
                Thread.currentThread().getId(), order.getId(), order.getStatus()));
        ServiceInstance instance = loadBalancer.choose("order-service");
        if (instance != null) {
            restTemplate.postForLocation(instance.getUri() + "/complete", order);
        } else {
            log.error("order-service is not available");
        }
    }
}
