package demo;

import demo.domain.Item;
import demo.domain.Restaurant;
import demo.domain.RestaurantRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RestaurantServiceApplication.class)
@WebAppConfiguration
@DataJpaTest
public class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository repository;

    private Restaurant generateRestaurant(long id, String name) {
        return new Restaurant(id, name);
    }

    private Item generateItem(long id, String name, Restaurant restaurant) {
        Item item = new Item(id, name);
        item.setRestaurant(restaurant);
        return item;
    }

    @Test
    public void findByName() {
        String restaurantName = "rest-1";
        Restaurant restaurant = generateRestaurant(1, restaurantName);
//        List<Item> items = new ArrayList<>();
//        for (int i = 0; i < 5; i ++) {
//            items.add(generateItem(i, "item-" + i, restaurant));
//        }
//        restaurant.setItems(items);

        repository.save(Arrays.asList(restaurant));
        Page<Restaurant> result = repository.findByName(restaurantName, new PageRequest(0, 1));
        Restaurant actualRestaurant = result.getContent().get(0);
        assertThat(actualRestaurant.getName()).isEqualTo(restaurantName);
//        assertThat(actualRestaurant.getItems().size()).isEqualTo(items.size());
    }
}
