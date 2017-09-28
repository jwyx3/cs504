package demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
    private String name;
    private String number;
    private int year;
    private int month;
    @JsonIgnore
    @Transient
    private int securityCode;
}
