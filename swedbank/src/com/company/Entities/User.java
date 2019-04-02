package com.company.Entities;

import com.company.annotations.Column;

public class User {

    @Column("user_ID")
    private long id;

    @Column
    private String firstName;

    public User(long id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }

    public User() {

    }

    public long getUserID(){
        return id;
    }
    public String getUserName(){
        return firstName;
    }
}
