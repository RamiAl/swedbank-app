package com.company.Entities;

import com.company.annotations.Column;

public class UserList {

    @Column
    private String userName;

    @Column ("user_ID")
    private long userID;


    public UserList(String userName, long userID) {
        this.userName = userName;
        this.userID = userID;

    }

    public UserList() {
    }

    @Override
    public String toString(){
        return String.format("%s",userName);
    }

    public long getUserID() {
        return userID;
    }
}
