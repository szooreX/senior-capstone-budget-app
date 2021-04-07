package com.example.senior_capstone_budget_app.data.paypalAPI;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.UnsupportedEncodingException;

import io.reactivex.rxjava3.core.Single;


/**
 * This is an API for PayPal's com.example.senior_capstone_budget_app.transaction.Transaction Search
 * <p>
 * Last Updated 03/30/2021
 *
 * @author Katelynn Urgitus
 */
public class PayPalTransactionAPI {

    private final String clientID;
    private final String secret;
    private URL url;
    private String authToken;

    /**
     * Constructor
     */
    public PayPalTransactionAPI() {
        this.clientID = "AYrRb_N3xKvsqWCkqtANS7pxKFMol0bitnq7MkciLCMVYTFiqtu32feWTefYYJwov8WTO6GrsRnSSxCQ";
        this.secret = "ECeue3J28wU-Nzk9T0jZipiVoVp3jwVg0QTj9CSiMAR0yUFRoKq_nTG6SyHL4fw3kJ8nicjHI3KdAxAI";
        this.authToken = setAuthToken();
    }

    /**
     * Gets the users total account balance
     *
     * @return
     */
    public String findBalance() {
        String baseUrl = "https://api-m.sandbox.paypal.com";
        String callAction = "/v1/reporting/balances?currency_code=USD";
        String urlString = baseUrl + callAction;
        String balance = "";
        try {
            url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + this.authToken);
            con.setRequestProperty("Accept-Language", "en_US");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");
            StringBuffer content;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            con.disconnect();
            JSONObject obj = new JSONObject(content.toString());

            balance = obj.getString("balances.total_balance.value");
//            JSONArray arr = new JSONArray();
//            obj.toJSONArray(arr);


        } catch (IOException | JSONException ex) {
            Logger.getLogger(PayPalTransactionAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return balance;
    }

    /**
     * Gets the transaction amounts for a user
     *
     * @return
     */
    public String findTransaction() {
        String baseUrl = "https://api-m.sandbox.paypal.com";
        String callAction = "/v1/reporting/transactions?start_date=2014-07-01T00:00:00-0700&end_date=2021-03-30T23:59:59-0700";
        String urlString = baseUrl + callAction;
        String transactionAmount = "";
        try {
            url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + this.authToken);
            con.setRequestProperty("Accept-Language", "en_US");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");
            StringBuffer content;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            con.disconnect();
            JSONObject obj = new JSONObject(content.toString());
            transactionAmount = obj.getString("transaction_details.transaction_info.transaction_amount.value");

        } catch (IOException | JSONException ex) {
            Logger.getLogger(PayPalTransactionAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return transactionAmount;
    }

    /**
     * Retrieves the Authorization Token from the API
     *
     * @return
     */
    public String setAuthToken() {
        byte[] authBytes = (this.clientID + ":" + this.secret).getBytes();
        byte[] authBase64Encoded = android.util.Base64.encode(authBytes, Base64.NO_WRAP);
        String credentialsBase64Encoded = "";
        try {
            credentialsBase64Encoded = new String(authBase64Encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.getLogger(PayPalTransactionAPI.class.getName()).log(Level.SEVERE, "Unable to base 64 encode", e);
        }
        String baseUrl = "https://api-m.sandbox.paypal.com";

        String callActionAuth = "/v1/oauth2/token?grant_type=client_credentials";
        URL urlAuth;
        try {
            urlAuth = new URL(baseUrl + callActionAuth);
            HttpURLConnection con = (HttpURLConnection) urlAuth.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Accept-Language", "en_US");
            con.setRequestProperty("Authorization", "Basic " + credentialsBase64Encoded);
            StringBuffer content;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            con.disconnect();

            JSONObject obj = new JSONObject(content.toString());
            authToken = obj.getString("access_token");
            return authToken;
        } catch (IOException | JSONException ex) {
            Logger.getLogger(PayPalTransactionAPI.class.getName()).log(Level.SEVERE, null, ex);
            return "failed to connect " + ex.getMessage();
        }
    }
}
