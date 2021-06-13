package com.example.sanaldolabmuygulamas;
import java.io.Serializable;
public class User implements Serializable {
    private String email;
    private String uid;

    public User(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }
}
