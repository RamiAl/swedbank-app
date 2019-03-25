package com.company.accountSettings;

import com.company.annotations.Column;

public class AccountTypeList {
    @Column
    private String kontoType;


    public AccountTypeList(String kontoType) {
        this.kontoType = kontoType;

    }

    public AccountTypeList(){

    }

    public String getKontoType() {
        return kontoType;
    }
}
