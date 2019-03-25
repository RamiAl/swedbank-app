package com.company.home;

import com.company.annotations.Column;

public class Transaction {

    @Column
    private String amount;

    @Column ("from_to")
    private String fromOrTo;

    @Column
    private String kontoNumber;
    public Transaction(String amount, String fromOrTo, String kontoNumber) {
        this.amount = amount;
        this.fromOrTo = fromOrTo;
        this.kontoNumber = kontoNumber;
    }

    public Transaction() {
    }

    @Override
    public String toString(){
        return String.format("%s:\n    %skr",fromOrTo, amount);
    }
}
