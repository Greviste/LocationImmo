package com.example.locationimmo;

import java.io.Serializable;
import java.util.Date;

public class RentalAd implements Serializable {
    public String title, description, specs;
    public String availability;
    public String address;
    public ContactType contact;
    public Float price;
    public User owner;
}
