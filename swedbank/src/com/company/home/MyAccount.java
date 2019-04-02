package com.company.home;

import com.company.annotations.Column;

public class MyAccount {

    @Column
    private String kontoNumber;

    @Column
    private int currentAmount;

    @Column
    private String kontoType;

    public MyAccount(String kontoNumber, int currentAmount, String kontoType ) {
        this.kontoNumber = kontoNumber;
        this.currentAmount = currentAmount;
        this.kontoType = kontoType;
    }

    public MyAccount() {

    }

    @Override
    public String toString(){
        return String.format("%s:   %skr kvar på ditt konto\n  %s ", kontoType, currentAmount, kontoNumber);
    }

    public String getKontoType() {
        return kontoType;
    }

    public String getKontoNumber(){
        return kontoNumber;
    }
    public int getCurrentAmount(){
        return currentAmount;
    }

}
