package com.example.locationimmo;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String email;
    public String password;
    public UserType type;
    public ArrayList<RentalAd> ads = new ArrayList<>();
}
