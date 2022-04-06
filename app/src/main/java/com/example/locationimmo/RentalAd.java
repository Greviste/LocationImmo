package com.example.locationimmo;

import java.util.Date;

public class RentalAd {
    String title, description, specs;
    String[] availability;
    String address;
    String contacts;            //mail, tél, appli ...
    Float price;

    public RentalAd(String _title, String _desc, String _specs, String[] _available, String _addr, String _contacts, Float _price){
        this.title = _title;
        this.description = _desc;
        this.specs = _specs;
        this.availability = _available;
        this.address = _addr;
        this.contacts = _contacts;
        this.price = _price;
    }

    public String getAddress() {
        return address;
    }

    public String[] getAvailability() {
        return availability;
    }

    public String getContacts() {
        return contacts;
    }

    public String getDescription() {
        return description;
    }

    public String getSpecs() {
        return specs;
    }

    public String getTitle() {
        return title;
    }

    public Float getPrice() {
        return price;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAvailability(String[] availability) {
        this.availability = availability;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
