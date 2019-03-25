package com.company.Entities;

import com.company.annotations.Column;

public class User {

    @Column("user_ID")
    private long id;

    @Column
    private String userName;

    @Column
    private String password;

    public User(long id, String name, String password) {
        this.id = id;
        this.userName = name;
        this.password = password;
    }

    public User() {

    }

    @Override
    public String toString(){
        return String.format("Namn: %s}", userName);
    }

    public long getUserID(){
        return id;
    }
    public String getUserName(){
        return userName;
    }
}
