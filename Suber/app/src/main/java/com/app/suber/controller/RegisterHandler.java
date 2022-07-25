package com.app.suber.controller;

import com.app.suber.model.user.Driver;
import com.app.suber.model.user.Passenger;
import com.app.suber.model.user.User;

public class RegisterHandler {
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String password;
    private final String extra;
    private final boolean isPassenger;

    public RegisterHandler(
            String username, String password, String firstname,
            String lastname, String extra, boolean isPassenger
    ) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.extra = extra;
        this.isPassenger = isPassenger;
    }

    public User register() {
        User result;
        if (this.isPassenger) {
            result = register_passenger();
        } else {
            result = register_driver();
        }
        return result;
    }

    private User register_driver() {
        if (User.getDriverByUsername(this.username) != null) {
            return null;
        }
        return new Driver(
                this.username,
                this.password,
                this.firstname,
                this.lastname,
                this.extra
        );
    }


    private User register_passenger() {
        if (User.getPassengerByUsername(this.username) != null) {
            return null;
        }
        return new Passenger(
                this.username,
                this.password,
                this.firstname,
                this.lastname
        );
    }
}
