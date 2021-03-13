package com.example.senior_capstone_budget_app.data.paypalAPI;

import java.util.ArrayList;

/**
 * This is a Translator for the PayPal API
 *
 * Last Updated 03/12/2021
 *
 * @author Katelynn Urgitus
 */
public class PayPalAPITranslator extends PayPalTransactionAPI implements TransactionAPIInterface{
    @Override
    public ArrayList<String> getTransactionAmount() {
        return null;
    }

    @Override
    public ArrayList<String> getTransactionType() {
        return null;
    }
}
