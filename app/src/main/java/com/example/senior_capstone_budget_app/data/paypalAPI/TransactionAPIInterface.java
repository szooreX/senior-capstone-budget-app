package com.example.senior_capstone_budget_app.data.paypalAPI;

import java.util.ArrayList;
/**
 * This is our API Interface for the Transaction API
 *
 * Last Updated 03/12/2021
 *
 * @author Katelynn Urgitus
 */
public interface TransactionAPIInterface {
    public ArrayList<String> getTransactionType();
    public ArrayList<String> getTransactionAmount();
}
