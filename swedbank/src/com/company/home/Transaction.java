package com.company.home;

import com.company.annotations.Column;

import java.sql.Timestamp;
import java.util.Date;

public class Transaction {

    @Column
    private int amount;

    @Column ("to_kontoNumber")
    private String toKontoNumber;

    @Column ("from_kontoNumber")
    private String fromKontoNumber;

    @Column
    private Timestamp time = new Timestamp(0);

    public Transaction(int amount, String fromKontoNumber, String toKontoNumber) {
        this.amount = amount;
        this.toKontoNumber = toKontoNumber;
        this.fromKontoNumber = fromKontoNumber;
    }

    public Transaction() {
    }

    @Override
    public String toString(){
        String accName = toKontoNumber == null ? fromKontoNumber : toKontoNumber;
        return String.format("%s:\n    %skr   %s", accName, amount,
                time.toLocalDateTime().toString().replace('T', ' '));
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getTime() {
        return time.getTime();
    }

    public String getKontoNumber() {
        return fromKontoNumber;
    }
}
