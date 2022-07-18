package com.app.suber.model.user;

public class Passenger extends User {
    public static final String NAME = "Passengers";

    public Passenger(
            String username, String password, String firstname, String lastname
    ) {
        super(username, password, firstname, lastname);
        this.type = User.PASSENGER;
    }

    @Override
    public String encode() {
        return String.format("%s:%s:%s:%s", password, firstname, lastname);
    }
}
