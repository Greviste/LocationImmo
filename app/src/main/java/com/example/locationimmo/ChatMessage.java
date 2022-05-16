package com.example.locationimmo;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    public User from, to;
    public String message;
}
