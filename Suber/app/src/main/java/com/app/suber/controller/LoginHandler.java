package com.app.suber.controller;

import com.app.suber.model.user.User;

public class LoginHandler {
    private final String username;
    private final String password;

    public LoginHandler(
            String username, String password
    ) {
        this.username = username;
        this.password = password;
    }

    public User login() {
        User user = User.getUserByUsername(this.username);
        if (user == null) {
            return null;
        }
        if (!user.getPassword().equals(this.password)) {
            return null;
        }
        return user;
    }
}