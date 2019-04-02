package com.company.Entities;

import com.company.annotations.Column;

public class UserList {

    @Column
    private String firstName;

    @Column ("user_ID")
    private long userID;


    public UserList(String userName, long userID) {
        this.firstName = userName;
        this.userID = userID;

    }

    public UserList() {
    }

    @Override
    public String toString(){
        return String.format("%s",firstName);
    }

    public long getUserID() {
        return userID;
    }
}
