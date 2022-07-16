package com.app.suber.model.user;

public class Driver extends User {
    public static final String NAME = "Drivers";
    private String car;

    public Driver(
            String username, String password, String firstname, String lastname, String car
    ) {
        super(username, password, firstname, lastname);
        this.car = car;
        this.type = User.DRIVER;
    }

    @Override
    public String encode() {
        return String.format("%s:%s:%s:%s", password, firstname, lastname, car);
    }
}
