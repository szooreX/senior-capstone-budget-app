package com.example.senior_capstone_budget_app.data.paypalAPI;

/**
 * This is a BaseAPI Class for our Transaction API
 *
 * Last Updated 03/12/2021
 *
 * @author Katelynn Urgitus
 */
public class BasePayPalAPIClass {
    protected final static TransactionAPIInterface payPalAPI = new PayPalAPITranslator();
}
