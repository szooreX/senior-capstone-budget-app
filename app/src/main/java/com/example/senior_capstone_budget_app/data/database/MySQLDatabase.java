package com.example.senior_capstone_budget_app.data.database;

/**
 * This class implements the DBConnectorInterface for My SQL Databases.
 *
 * Last Updated: 04/05/2021
 *
 * @author Katelynn Urgitus
 */

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLDatabase implements DBConnectorInterface {

    Connection connection = null;
    Statement statement = null;

    static String connectionURL = "jdbc:mysql://sql9.freemysqlhosting.net:3306/sql9368288?zeroDateTimeBehavior=convertToNull";
    static String user = "sql5403571";
    static String pwd = "F3VyK79v23";

    /**
     * Default Constructor connects to the MS SQL Database
     */
    public MySQLDatabase() {
        this.connect();
    }

    /**
     * This method creates a new row entry in the given _table
     *
     * @param _keyValuePair the object map being inserted
     * @param _table the _table in the database that needs to be inserted to
     * @return value of the key determines whether the insert was successful
     */
    @Override
    public int createObject(Map<String, String> _keyValuePair, String _table) {
        String query = "INSERT INTO " + _table;
        String names = "(";
        String values = "VALUES (";
        for (Map.Entry<String, String> entry : _keyValuePair.entrySet()) {
            names += " " + entry.getKey() + ", ";
            values += " '" + entry.getValue() + "', ";
        }
        // Trim off the last comma.
        names = names.substring(0, names.length() - 2);
        values = values.substring(0, values.length() - 2);
        names += ") ";
        values += ")";
        query += names + values;
        // Execute the query.
        int newKey = this.executeInsert(query);
        if (newKey == -1) {
            System.out.println("Database Error: Could not create new record");
            return 0;
        }
        return newKey;
    }

    /**
     * This is a helper method to execute the insert _query
     *
     * @param _query SQL statement to be executed
     * @return the key value determines if the execute was successful
     */
    private int executeInsert(String _query) {
        int key = -1;
        try {
            this.statement.executeUpdate(_query, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                key = result.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Query Error: " + ex.getMessage());
            Logger.getLogger(DataStoreAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return key;
    }

    /**
     * Calls on the other readObject method but only accounts for active entries
     *
     * @param _keyValuePair object map being searched for
     * @param _table _table searched
     * @return the data requested from the _table
     */
    @Override
    public HashMap<String, Object> readObject(Map<String, String> _keyValuePair, String _table) {
        return this.readObject(_keyValuePair, _table, false);
    }

    /**
     * Selects requested data from a specific data _table
     *
     * @param _keyValuePair data being requested
     * @param _table _table searched
     * @param _deleted not yet implemented but will be able to search only
     * active users or active and "_deleted" users
     * @return the data requested if it is there; If not it returns NULL
     */
    public HashMap<String, Object> readObject(Map<String, String> _keyValuePair, String _table, boolean _deleted) {

        String query = "SELECT * FROM " + _table + " WHERE ";
        String condition = "";
        // Iterate over map.
        for (Map.Entry<String, String> entry : _keyValuePair.entrySet()) {
            condition += " `" + entry.getKey() + "` = \"" + entry.getValue() + "\" AND";
        }
        if (_deleted) {
            // Then we'll ignore the active=1 condition and just shed off the last AND
            condition = condition.substring(0, condition.length() - 3);
        } else {
            // We'll add the condition that the object must be active.
            condition += " `active` = 1";
        }
        // Combine the query with the condition.
        query = query + condition;
        // Initialize a object to store the results.
        HashMap<String, Object> returnData = new HashMap();
        ResultSet results = this.runQuery(query);
        // Create a flag for judging if the result set is empty.
        boolean isEmpty = true;
        try {
            while (results.next()) {
                isEmpty = false;
                ResultSetMetaData data = results.getMetaData();
                int count = data.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    String columnName = data.getColumnName(i);
                    returnData.put(columnName, results.getObject(i));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Close the statement connection.
        if (isEmpty) {
            // No results were loaded, return null.
            return null;
        }
        return returnData;
    }

    /**
     * Updates existing data fields in a given _table for the given UUID
     *
     * @param _keyValuePair data to be updated
     * @param _uuid UUID of the entry to be updated
     * @param _table where the update is taking place
     * @return true if the update was successful, false if unsuccessful.
     */
    @Override
    public Boolean updateObject(Map<String, String> _keyValuePair, String _uuid, String _table) {
        String query = "UPDATE " + _table + " SET ";
        //iterate over map
        String updates = "";
        for (Map.Entry<String, String> entry : _keyValuePair.entrySet()) {
            updates += " " + entry.getKey() + " = " + entry.getValue() + ",";
        }
        //shed off the last comma
        updates = updates.substring(0, updates.length() - 1);
        query = query + updates + " WHERE UUID = '" + _uuid + "'";
        return this.executeUpdate(query);
    }

    /**
     * Updates existing data fields in a given _table for the given UUID
     *
     * @param _keyValuePair data to be updated
     * @param _uuid UUID of the entry to be updated
     * @param _table1 where the update is taking place
     * @param _table2 where you are pulling the data to be updated from
     * @return true if the update was successful, false if unsuccessful.
     */
    public Boolean updateObject(Map<String, String> _keyValuePair, String _uuid, String _table1, String _table2) {
        String query = "UPDATE " + _table1 + " SET ";
        //iterate over map
        String updates = "";
        for (Map.Entry<String, String> entry : _keyValuePair.entrySet()) {
            updates += " " + entry.getKey() + " = (SELECT " + entry.getKey() + " FROM " + _table2 + " WHERE " + entry.getValue() + " = '" + _uuid + "')" + ",";
        }
        //shed off the last comma
        updates = updates.substring(0, updates.length() - 1);
        query = query + updates + " WHERE UUID = '" + _uuid + "'";
        return this.executeUpdate(query);
    }

    /**
     * Helper method to execute an update
     *
     * @param _query SQL statement being executed
     * @return true if successful, false if nothing happened
     */
    private boolean executeUpdate(String _query) {
        int result = 0;
        try {
            result = statement.executeUpdate(_query, Statement.NO_GENERATED_KEYS);
        } catch (SQLException ex) {
            System.out.println("Query Error: " + ex.getMessage());
            Logger.getLogger(DataStoreAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Result returns the number of rows affected. If no rows were affected,
        // then return false.
        return (result > 0);
    }

    /**
     * This method deletes an object from the database.
     *
     * @TODO - Implement this method. We want to just make an update to the
     * database to set an active flag to false instead of actually deleting
     * records.
     * @param _uuid UUID of entries to be deemed "Inactive"
     * @return Boolean statement true if deleted, false if nothing happened
     */
    @Override
    public Boolean deleteObject(String _uuid) {
        Map<String, String> setInactive = new HashMap();
        setInactive.put("active", "0");
        return this.updateObject(setInactive, _uuid, "user");
    }

    /**
     * Runs SQL _query
     *
     * @param _query to be executed
     * @return result set
     */
    protected ResultSet runQuery(String _query) {
        ResultSet result = null;
        try {
            result = statement.executeQuery(_query);
        } catch (SQLException ex) {
            System.out.println("Query Error: " + ex.getMessage());
        }
        return result;
    }

    /**
     * Connects to Database
     */
    private void connect() {
        try {
            this.connection = DriverManager.getConnection(MySQLDatabase.connectionURL, MySQLDatabase.user, MySQLDatabase.pwd);
            this.statement = connection.createStatement();
        } catch (SQLException ex) {
            System.out.println("Could Not Connect To Database" + ex.getMessage());
        }
    }

    /**
     * A helper method to execute a simple select
     *
     * @param _query SQL statement
     * @return the selected set
     */
    public ResultSet executeSelect(String _query) {
        ResultSet result = this.runQuery(_query);
        return result;
    }
}
