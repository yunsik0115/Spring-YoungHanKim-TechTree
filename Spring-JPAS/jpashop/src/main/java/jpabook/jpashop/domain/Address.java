package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter // Value must be immutable
@Embeddable // JPA's Embedded Type indication(Can be embedded somewhere)
public class Address {

    private String city;

    private String street;

    private String zipcode;

    protected Address(){

    } // JPA uses REFLECTION, PROXY, Both Requires Default Constructor(protected Allowed)

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
