package com.example.locationimmo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class User implements Serializable {
    public String email;
    public String password;
    public UserType type;
    public ArrayList<RentalAd> ads = new ArrayList<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(type, user.type);
    }
}
