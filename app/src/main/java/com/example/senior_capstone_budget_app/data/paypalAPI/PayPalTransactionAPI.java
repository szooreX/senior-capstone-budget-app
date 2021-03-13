package com.example.senior_capstone_budget_app.data.paypalAPI;

import com.android.volley.toolbox.HttpResponse;

import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is an API for PayPal's Transaction Search
 *
 * Last Updated 03/12/2021
 *
 * @author Katelynn Urgitus
 */
public class PayPalTransactionAPI {

    private final String clientID;
    private final String secret;
    private URL url;
    private String authToken;

    public PayPalTransactionAPI() {
        this.clientID = "AYrRb_N3xKvsqWCkqtANS7pxKFMol0bitnq7MkciLCMVYTFiqtu32feWTefYYJwov8WTO6GrsRnSSxCQ";
        this.secret = "ECeue3J28wU-Nzk9T0jZipiVoVp3jwVg0QTj9CSiMAR0yUFRoKq_nTG6SyHL4fw3kJ8nicjHI3KdAxAI";
        this.authToken = setAuthToken();
    }

    protected ArrayList<Object> getDataFromAPI(String _contentRequested, String _transactionID) {
        String callAction = "";
        String objNeeded = "";
        switch (_contentRequested){
            case "transactions":
                callAction = "/v1/reporting/transactions";
                objNeeded = "transaction_amount";
                break;
            case "balances":
                callAction = "/v1/reporting/balances";
                objNeeded = "total_balance";
                break;
        }
        String baseUrl = "https://api-m.sandbox.paypal.com";
        String urlString = baseUrl + callAction;
        ArrayList<Object> notSoEmptyList = new ArrayList();
        try {
            url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + authToken);
            con.setRequestProperty("Accept-Language", "en_US");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            StringBuffer content;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            con.disconnect();
            JSONArray obj = new JSONArray(content.toString());

            for (int i = 0; i < obj.length() - 1; i++) {
                JSONObject temp = new JSONObject(obj.get(i).toString());
                notSoEmptyList.add(temp.getJSONObject(objNeeded));

            }
        } catch (IOException | JSONException ex) {
            Logger.getLogger(PayPalTransactionAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notSoEmptyList;
    }

    private String setAuthToken() {
        String baseUrl = "https://api-m.sandbox.paypal.com";
        String callActionAuth = "/v1/oauth2/token";
        URL urlAuth;
        try {
            urlAuth = new URL(baseUrl + callActionAuth);
            HttpURLConnection con = (HttpURLConnection) urlAuth.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Accept-Language", "en_US");
            con.setRequestProperty("client_id", this.clientID);
            con.setRequestProperty("secret", this.secret);
            con.setRequestProperty("grant_type", "client_credentials");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
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
            return "failed to connect";
        }
    }
}
