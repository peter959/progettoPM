package com.example.instantreservation;

public class User {
    public String fullname;
    public String email;
    public String phone;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String fullname, String email, String phone) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
    }
}
