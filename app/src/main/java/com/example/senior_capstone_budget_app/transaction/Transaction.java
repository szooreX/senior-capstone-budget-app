package com.example.senior_capstone_budget_app.transaction;


import com.example.senior_capstone_budget_app.data.Categories;

import java.util.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Transaction {
    private double amount;
    private String paymentTo;
    private Date paymentDate;
    private Timestamp paymentTimestamp;
    private Categories category;

    //For payment generation from database
    public Transaction(double amount, String paymentTo, Timestamp paymentDate, int c) {
        this.amount = amount;
        this.paymentTo = paymentTo;
        this.paymentTimestamp = paymentDate;
        this.paymentDate = new Date(paymentTimestamp.getTime());
        switch (c){
            case 0:
                this.category = Categories.UNCATEGORIZED;
                break;
            case 1:
                this.category = Categories.RENT;
                break;
            case 2:
                this.category = Categories.UTILITIES;
                break;
            case 4:
                this.category = Categories.HOUSEHOLD;
                break;
            case 5:
                this.category = Categories.PERSONAL;
                break;
            case 6:
                this.category = Categories.MEDICAL;
                break;
            case 8:
                this.category = Categories.FINANCIAL;
                break;
        }
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", paymentTo='" + paymentTo + '\'' +
                ", paymentDate=" + paymentDate +
                ", paymentTimestamp=" + paymentTimestamp +
                ", category=" + category +
                '}';
    }

    //For in app transaction generation
    public Transaction(double amount, String paymentTo, Timestamp paymentDate, Categories category) {
        this.amount = amount;
        this.paymentTo = paymentTo;
        this.paymentDate = paymentDate;
        this.category = category;
        this.paymentTimestamp = new Timestamp(paymentDate.getTime());
    }

    //====================================Getters====================================//
    public double getAmount() {return amount;}
    public String getPaymentTo() {return paymentTo;}
    public Date getPaymentDate() {return paymentDate;}
    public Timestamp getPaymentTimestamp() {return paymentTimestamp;}
    public Categories getCategory() {return category;}

    //====================================Setters====================================//
    public void setAmount(double amount) {this.amount = amount;}
    public void setPaymentTo(String paymentTo) {this.paymentTo = paymentTo;}
    //For use by the user in app
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
        this.paymentTimestamp = new Timestamp(paymentDate.getTime());
    }
    //For use when saving
    public void setPaymentTimestamp(Timestamp paymentTimestamp) {
        this.paymentTimestamp = paymentTimestamp;
        this.paymentDate = new Date(paymentTimestamp.getTime());
    }
    public void setCategory(Categories category) {this.category = category;}
}
