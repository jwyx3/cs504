package demo.rest;

import demo.domain.Restaurant;
import demo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestaurantServiceApi {

    private RestaurantService restaurantService;

    @Autowired
    public void RestaurantServiceApi(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void upload(@RequestBody List<Restaurant> restaurants) {
        restaurantService.save(restaurants);
    }

    @RequestMapping(value = "/purge", method = RequestMethod.DELETE)
    public void purge() {
        restaurantService.deleteAll();
    }

    @RequestMapping(value = "/restaurants/{name}", method = RequestMethod.GET)
    public Page<Restaurant> findByName(@PathVariable String name, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return restaurantService.findByName(name, new PageRequest(page, size));
    }
}
