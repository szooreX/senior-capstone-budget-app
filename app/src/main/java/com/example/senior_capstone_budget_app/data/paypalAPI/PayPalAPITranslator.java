package com.example.senior_capstone_budget_app.data.paypalAPI;

import java.util.ArrayList;

/**
 * This is a Translator for the PayPal API
 *
 * Last Updated 04/06/2021
 *
 * @author Katelynn Urgitus
 */
public class PayPalAPITranslator extends PayPalTransactionAPI implements TransactionAPIInterface{
    @Override
    public String findTransaction() {return this.findTransaction(); }

    @Override
    public String findBalance() {
        return this.findBalance();
    }
}
