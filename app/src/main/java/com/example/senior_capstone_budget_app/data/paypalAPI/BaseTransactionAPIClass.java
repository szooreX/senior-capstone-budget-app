package com.example.senior_capstone_budget_app.data.paypalAPI;

/**
 * This is a BaseAPI Class for our com.example.senior_capstone_budget_app.Transaction API
 *
 * Last Updated 04/06/2021
 *
 * @author Katelynn Urgitus
 */
public class BaseTransactionAPIClass {
    public final static TransactionAPIInterface payPalAPI = new PayPalAPITranslator();
}
