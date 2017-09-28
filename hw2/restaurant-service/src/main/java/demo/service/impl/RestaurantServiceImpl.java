package demo.service.impl;

import demo.domain.Item;
import demo.domain.Restaurant;
import demo.domain.RestaurantRepository;
import demo.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

    private RestaurantRepository restaurantRepository;

    @Autowired
    public void RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Restaurant> save(List<Restaurant> restaurants) {
        log.info("save restaurant: {}", restaurants);
        for (Restaurant restaurant : restaurants) {
            for (Item item : restaurant.getItems()) {
                item.setRestaurant(restaurant);
            }
        }
        return restaurantRepository.save(restaurants);
    }

    @Override
    public void deleteAll() {
        restaurantRepository.deleteAll();
    }

    @Override
    public Page<Restaurant> findByName(String name, Pageable pageable) {
        log.info("find by name: {}", name);
        return restaurantRepository.findByName(name, pageable);
    }
}
