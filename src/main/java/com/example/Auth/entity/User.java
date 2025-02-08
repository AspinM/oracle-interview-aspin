package com.example.Auth.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String availabilityStatus; // "Online", "Offline", "Busy"


    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    // Getter and Setter methods for 'password'
    public String getPassword() {
        return password;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
