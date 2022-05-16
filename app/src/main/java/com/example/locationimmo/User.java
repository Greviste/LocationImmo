package com.example.locationimmo;

import java.io.Serializable;

public class User implements Serializable {
    public String email;
    public String password;
    public UserType type;
    public RentalAd[] ads;
}
