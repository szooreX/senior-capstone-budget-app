package com.example.senior_capstone_budget_app.data.database;

/**
 * The Data Factory Class can generate Class Objects and get/set properties for
 * the class type in which it is being called
 * <p>
 * Last Updated: 03/23/2021
 *
 * @author Katelynn Urgitus
 */

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataFactory {
    protected final String dataTable = "";

    /**
     *
     * @return a map of properties for the calling class
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Map getProperties() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        // A transport data object for name-value pairs.
        Map<String, String> pairs = new HashMap<>();
        // Find out what the calling class is.
        Class<?> c = this.getClass();
        // Iterate up through the chain of inherited clases until we hit a generic Object class.
        while (!c.getName().equals("java.lang.Object")) {
            // Get the fields of this class.
            Field[] fields = c.getDeclaredFields();
            // Iterate through each.
            for (Field f : fields) {
                // Grab the name of this class property.
                String fieldName = f.getName();
                // Grab the modifiers that describe this property.
                int mods = f.getModifiers();
                // Make sure that this field is neither static, public or final.
                if (Modifier.isPublic(mods) || Modifier.isStatic(mods) || Modifier.isFinal(mods)) {
                    continue;
                }
                // If this is a boolean, make convert to an int.
                if (f.getType() == boolean.class) {
                    String fieldValue = (f.get(this).equals(true) ? "1" : "0");
                    pairs.put(fieldName, fieldValue);
                } else {
                    // Grab this field's value and cast it as a string.
                    String fieldValue = f.get(this).toString();
                    // Add the name value pair to the map.
                    pairs.put(fieldName, fieldValue);
                }
            }
            // Move to the next parent class.
            c = c.getSuperclass();
        }
        // Return the hashmap of name-value pairs.
        return pairs;
    }

    /**
     *
     * @param _pairs sets the properties for the calling class
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void setProperties(HashMap _pairs) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        // Grab the class type for this object.
        Class<?> c = this.getClass();
        // Iterate up through the chain of inherited clases until we hit a generic Object class.
        while (!c.getName().equals("java.lang.Object")) {
            // Get a list of fields for this class.
            Field[] fields = c.getDeclaredFields();
            // Iterate through each.
            for (Field f : fields) {
                // Get the name of the field. This should correspond to the name of the name-value pairs.
                String fieldName = f.getName();
                // Get any modifiers for this property.
                int mods = f.getModifiers();
                // Make sure this property is neither static, public of final.
                if (!Modifier.isPublic(mods) && !Modifier.isStatic(mods) && !Modifier.isFinal(mods)) {
                    // Get the class type of this property.
                    Class type = f.getType();
                    // Find the class type and grab the associated setter to set the value.
                    if (type == int.class) {
                        int fieldValue = (int) _pairs.get(fieldName);
                        f.setInt(this, fieldValue);
                    } else if (type == String.class) {
                        String fieldValue = (String) _pairs.get(fieldName);
                        f.set(this, fieldValue);
                    } else if (type == double.class) {
                        double fieldValue = (double) _pairs.get(fieldName);
                        f.setDouble(this, fieldValue);
                    } else if (type == boolean.class) {
                        boolean fieldValue = (boolean) _pairs.get(fieldName);
                        f.setBoolean(this, fieldValue);
                    } else if (type == float.class) {
                        float fieldValue = (float) _pairs.get(fieldName);
                        f.setFloat(this, fieldValue);
                    } else if (type == char.class) {
                        char fieldValue = (char) _pairs.get(fieldName);
                        f.setChar(this, fieldValue);
                    } else if (type == long.class) {
                        long fieldValue = (long) _pairs.get(fieldName);
                        f.setLong(this, fieldValue);
                    } else if (type == short.class) {
                        short fieldValue = (short) _pairs.get(fieldName);
                        f.setShort(this, fieldValue);
                    } else if (type == byte.class) {
                        byte fieldValue = (byte) _pairs.get(fieldName);
                        f.setByte(this, fieldValue);
                    } else {
                        // Try a generic object type if it is not primitive. It should be primitive.
                        Object fieldValue = _pairs.get(fieldName);
                        f.set(this, fieldValue);
                    }
                }
            }
            // Iterate to the next parent class.
            c = c.getSuperclass();
        }
    }

    /**
     *
     * @param _class
     * @return an object for the given class
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static Object objectFactory(String _class) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class thisClass = Class.forName(_class);
        Object thisObject = thisClass.getDeclaredConstructor().newInstance();
        thisClass.cast(thisObject);
        return thisObject;
    }

    /**
     *
     * @param _class
     * @param _data
     * @return an object with set data for the given class
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object objectFactory(String _class, HashMap<String, Object> _data) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        // Generate a Class object based on the class name.
        Class thisClass = Class.forName(_class);
        // Create an empty instance of this class.
        Object thisObject = thisClass.getDeclaredConstructor().newInstance();
        // Create an array of class objects to represent the parameters for the setProperties method.
        Class[] params = new Class[1];
        params[0] = HashMap.class;
        // Create a method object based on the method name and list of parameters.
        Method thisMethod = thisClass.getMethod("setProperties", params);
        // Invoke the setProperties method.
        thisMethod.invoke(thisObject, _data);
        // Return the generic object.
        return thisObject;
    }

    /**
     *
     * @return the data table for the calling class
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public String getDataTable() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> c = this.getClass();
        Field f = c.getDeclaredField("dataTable");
        return f.get(this).toString();
    }

    /**
     *
     * @return name of the calling class
     */
    public String getClassName() {
        return this.getClass().getName();
    }

    /**
     *
     * @param _data
     * @return An object with information from the database
     */
    public DataObject loadByCondition(HashMap<String, String> _data) {
        try {
            // Given an empty object, this method fills this object with data from the database.
            return DataStoreAdapter.readObject(_data, this.getDataTable(), this.getClassName());
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException | ClassNotFoundException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            Logger.getLogger(DataObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }
}

