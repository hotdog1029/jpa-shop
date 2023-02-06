package jpabook.jpashop.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable // jpa의 내장 타입이기 때문에
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcod;

    protected Address() {
    }

    public Address(String city, String street, String zipcod) {
        this.city = city;
        this.street = street;
        this.zipcod = zipcod;
    }
}
