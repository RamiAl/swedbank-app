package com.company.home;

import com.company.annotations.Column;

public class Transaction {

    @Column
    private String amount;

    @Column ("from_to")
    private String fromOrTo;

    @Column ("user_ID")
    private String kontoNumber;

    @Column
    private long userID;

    public Transaction(String amount, String fromOrTo, String kontoNumber, long userID) {
        this.amount = amount;
        this.fromOrTo = fromOrTo;
        this.kontoNumber = kontoNumber;
        this.userID = userID;
    }

    public Transaction() {
    }

    @Override
    public String toString(){
        return String.format("%s:\n    %skr",fromOrTo, amount);
    }
}
