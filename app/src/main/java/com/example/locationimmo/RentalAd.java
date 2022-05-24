package com.example.locationimmo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class RentalAd implements Serializable {
    public String title, description, specs;
    public String availability;
    public String address;
    public ContactType contact;
    public Float price;
    public User owner;
    public ArrayList<User> savers = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        RentalAd rentalAd = (RentalAd) o;
        return Objects.equals(title, rentalAd.title) && Objects.equals(description, rentalAd.description) && Objects.equals(specs, rentalAd.specs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, specs, availability, address, contact, price, owner);
    }
}
