package com.example.senior_capstone_budget_app.data.database;

/**
 * The DataStore Adapter allows us to switch between specific databases in case
 * we would like to use a new or different database and connects that to the
 * system.
 * <p>
 * Last Updated: 04/05/2021
 *
 * @author Katelynn Urgitus
 */

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataStoreAdapter {

    protected static final DBConnectorInterface connector = new MySQLDatabase();

    /**
     *
     * @param _obj
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    public static Boolean createObject(DataObject _obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        // Send name-value pairs to the connector class. This class should
        // return a generated id number.
        int id = connector.createObject(_obj.getProperties(), _obj.getDataTable());
        // Associate this new id number with the object that was just saved.
        _obj.setId(id);
        // Return true if we have an id number. false otherwise.
        return (id != 0);
    }

    /**
     *
     * @param _map
     * @param _table
     * @param _class
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public static DataObject readObject(Map<String, String> _map, String _table, String _class) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // The Connector object returns a Hashmap of name-value pairs.
        HashMap<String, Object> results = connector.readObject(_map, _table);
        if (results == null) {
            return null;
        }
        try {
            // Here we create an object (cast as a generic DataObject) and return it.
            return (DataObject) DataFactory.objectFactory(_class, results);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(DataStoreAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param _obj
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    public static Boolean updateObject(DataObject _obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        return connector.updateObject(_obj.getProperties(), _obj.getUuid(), _obj.getDataTable());
    }

    /**
     *
     * @param _obj
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    public static Boolean deleteObject(DataObject _obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        _obj.makeInactive();
        return DataStoreAdapter.updateObject(_obj);
    }
}

