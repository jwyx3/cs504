package demo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Item {
    private String name;
    private String description;
    private int quantity;
    private double price;
}
