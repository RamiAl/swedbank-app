package com.company.home;

import com.company.annotations.Column;

public class MyAccount {

    @Column
    private String kontoNumber;

    @Column
    private String currentAmount;

    @Column
    private String kontoType;

    @Column ("user_ID")
    private long userID;


    public MyAccount(long userID, String kontoNumber, String currentAmount, String kontoType ) {
        this.kontoNumber = kontoNumber;
        this.currentAmount = currentAmount;
        this.kontoType = kontoType;
        this.userID =userID;
    }

    public MyAccount() {

    }

    @Override
    public String toString(){
        return String.format("%s:   %skr kvar på ditt konto\n  %s ", kontoType, currentAmount, kontoNumber);
    }

    public long getUserID() {
        return userID;
    }

    public String getKontoType() {
        return kontoType;
    }

    public String getKontoNumber(){
        return kontoNumber;
    }
    public String getCurrentAmount(){
        return currentAmount;
    }

}
