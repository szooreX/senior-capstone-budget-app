package com.example.senior_capstone_budget_app.data.database;

/**
 * The Database Interface to implement the CRUD operations that allow us to use
 * any database
 *
 * Last Updated: 03/23/2021
 *
 * @author Katelynn Urgitus
 */

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DataObject extends DataFactory{
    protected final String dataTable = "";
    protected int id;
    protected String uuid;
    protected String name;
    protected boolean active = true;

    /**
     * Default constructor creates a UUID for the new object
     */
    public DataObject() {
        this.setUuid(DataObject.generateUuid());
    }

    /**
     *
     * @param _id
     * @return An object from the database with the given ID
     */
    public DataObject loadById(int _id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", Integer.toString(_id));
        return this.loadByCondition(map);
    }

    /**
     *
     * @param _uuid
     * @return An object from the database with the given UUID
     */
    public DataObject loadByUuid(String _uuid) {
        HashMap<String, String> map = new HashMap<>();
        map.put("uuid", _uuid);
        return this.loadByCondition(map);
    }

    /**
     *
     * @param _name
     * @param _value
     * @return An object from the database given the name value pair
     */
    public DataObject loadByCondition(String _name, String _value) {
        HashMap<String, String> map = new HashMap<>();
        map.put(_name, _value);
        return this.loadByCondition(map);
    }

    /**
     *
     * @return true if save was successful, false otherwise
     */
    public Boolean save() {
        // Has this object already been created?
        if (this.id == 0) {
            try {
                return DataStoreAdapter.createObject(this);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchFieldException ex) {
                Logger.getLogger(DataObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                // This is an exisitng object in the database, just update the object.
                return DataStoreAdapter.updateObject(this);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchFieldException ex) {
                Logger.getLogger(DataObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    /**
     * Deletes an entry/Makes entry inactive
     *
     * @return true if "deleted", false if nothing happened
     */
    public Boolean delete() {
        try {
            return DataStoreAdapter.deleteObject(this);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchFieldException ex) {
            Logger.getLogger(DataObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Makes the entry active
     */
    public void makeActive() {
        this.active = true;
    }

    /**
     * Makes the entry inactive
     */
    public void makeInactive() {
        this.active = false;
    }

    /**
     *
     * @return a generated UUID
     */
    protected static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    // ================================ GETTERS ====================================
    public String getName() {
        return this.name;
    }

    public String getUuid() {
        return this.uuid;
    }

    public int getId() {
        return this.id;
    }

    // ================================ SETTERS ====================================
    public void setName(String _name) {
        this.name = _name;
    }

    public void setUuid(String _uuid) {
        this.uuid = _uuid;
    }

    public void setId(int _id) {
        this.id = _id;
    }
}

