package com.learn.mobile.model;

public class User extends DAbstractUser {
    @Override
    public String getTitle() {
        return (fullName != null ? fullName : "");
    }
}