package com.company.Entities;

import com.company.annotations.Column;

public class UserAccountList {

    @Column
    private String kontoNumber;


    public UserAccountList(String kontoNumber) {
        this.kontoNumber = kontoNumber;

    }

    public UserAccountList() {
    }

    public String getKontoNumber() {
        return kontoNumber;
    }
}
