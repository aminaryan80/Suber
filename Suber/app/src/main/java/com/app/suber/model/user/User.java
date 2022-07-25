package com.app.suber.model.user;

import java.util.ArrayList;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public abstract class User {
    public static final String DRIVER = "driver";
    public static final String PASSENGER = "passenger";

    static ArrayList<User> users = new ArrayList<>();
    protected String username;
    protected String firstname;
    protected String lastname;
    protected String password;
    protected String type;

    public User(String username, String password, String firstname, String lastname) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        users.add(this);
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public String getDisplayName() {
        return firstname + " " + lastname;
    }

    public boolean isPassenger() {
        return this.type.equals(PASSENGER);
    }

    public static Driver getDriverByUsername(String username) {
        return (Driver) getUserByUsername(username, User.DRIVER);
    }

    public static Passenger getPassengerByUsername(String username) {
        return (Passenger) getUserByUsername(username, User.PASSENGER);
    }

    public static User getUserByUsername(String username, String type) {
        for (User user : users) {
            if (user.type.equals(type) && user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static String[] decode(String value) {
        return value.split(":");
    }

    public abstract String encode();
}