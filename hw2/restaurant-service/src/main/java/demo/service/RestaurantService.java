package demo.service;

import demo.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestaurantService {
    List<Restaurant> save(List<Restaurant> restaurants);
    void deleteAll();
    Page<Restaurant> findByName(String name, Pageable pageable);
}
