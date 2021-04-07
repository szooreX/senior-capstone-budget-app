package com.example.senior_capstone_budget_app.data.model;

/**
 * The ArvioUser Class builds a User and places the User data into the MySQLDatabase
 *
 * Last Updated: 04/05/2021
 *
 * @author Katelynn Urgitus
 */

import java.io.*;
import com.example.senior_capstone_budget_app.data.database.MySQLDatabase;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ArvioUser extends com.example.senior_capstone_budget_app.data.database.DataObject {

    private String firstName;
    private String lastName;
    private String userEmail;
    private String userPassword;
    private final MySQLDatabase connector = new MySQLDatabase();
    protected static ArvioUser user = new ArvioUser();

    /**
     * Constructor for ArvioUser Class
     * @param _firstName
     * @param _lastName
     * @param _email
     * @param _password
     * @throws IOException
     */
    public ArvioUser(String _firstName, String _lastName, String _email, String _password) throws IOException{
        this.firstName = _firstName;
        this.lastName = _lastName;
        this.userEmail = _email;
        this.userPassword = _password;

        this.setUuid(com.example.senior_capstone_budget_app.data.database.DataObject.generateUuid());
        saveUserInfo();
    }

    /**
     *  Default empty Constructor for ArvioUser
     */
    public ArvioUser(){

    }

    /**
     * Saves User information to database
     * @throws IOException
     */
    public void saveUserInfo() throws IOException{
        HashMap<String, String> saveUser = new HashMap();
        saveUser.put("UUID", this.getUuid());
        saveUser.put("firstName", this.getFirstName());
        saveUser.put("lastName", this.getLastName());
        saveUser.put("email", this.getEmail());
        saveUser.put("userPassword", "SHA1('"+ this.getPassword()+"')");

        connector.createObject(saveUser, "user");
    }

    /**
     *
     * @param _email to be validated.
     * @return true if valid, false if invalid.
     */
    public boolean checkValidEmail(String _email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (_email == null) {
            return false;
        }
        return pat.matcher(_email).matches();
    }

    /**
     * Verify that the User account exists
     *
     * @param _email user email
     * @param _password user password
     * @return results from query (the user if they exist, otherwise null)
     */
    public boolean checkValidUser(String _email, String _password) {
        boolean validUser = false;
        Map<String, String> checkExists = new HashMap();
        checkExists.put("userEmail", _email);
        checkExists.put("userPassword", _password);

        if (connector.readObject(checkExists, "user") != null) {
            validUser = true;
        }

        return validUser;
    }

    // ================================ GETTERS ===================================
    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.userEmail;
    }

    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String toString() {
        String str = this.firstName + " " + this.lastName;
        return str;
    }

    // ================================ SETTERS ====================================
    /**
     *
     * @param _fname user first name
     */
    public void setFirstName(String _fname) {
        this.firstName = _fname;
    }

    /**
     *
     * @param _lname user last name
     */
    public void setLastName(String _lname) {
        this.lastName = _lname;
    }

    /**
     *
     * @param _email user email
     */
    public void setEmail(String _email) {
        this.userEmail = _email;
    }

    /**
     *
     * @param _password user password
     */
    public void setPassword(String _password) {
        this.userPassword = _password;
    }

}
