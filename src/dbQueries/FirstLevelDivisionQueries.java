package dbQueries;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
/**
 * This class serves as the primary handler for all the queries associated with accessing and manipulating first level divisions in the database.
 * As the database contains a large amount of data, it is essential to have a dedicated class that specifically deals with this particular data set.
 * With this class, users can easily access and manage first level division data with minimal effort.
 * Additionally, this class is designed to be highly optimized for database queries, ensuring maximum efficiency and speed for all data manipulations.
 */

public class FirstLevelDivisionQueries {
    /**
     * This method retrieves all the first-level division names and their corresponding IDs from the
     * FIRST_LEVEL_DIVISIONS table in the database. The retrieved information is stored in a HashMap,
     * where the division names serve as keys and the IDs as values.
     * @return HashMap - A collection which pairs a Division name with its corresponding ID.
     */
    public static HashMap<String, Integer> retrieveAllDivisionIDs() {
        HashMap<String, Integer> divisionHashMap = new HashMap<>();
        try {
            //SQL Statement
            String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(sql);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                String division = results.getString("Division");
                int id = results.getInt("Division_ID");
                divisionHashMap.put(division, id);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return divisionHashMap;
    }

    /**
     * Returns a Map that contains information about first-level divisions.
     * The keys in the Map correspond to the unique IDs assigned to each division,
     * while the values represent the names of each division. This method provides a
     * convenient way to quickly reference and retrieve information about first-level divisions.
     * @return A Map object that maps Division IDs to their corresponding Division names.
     */
    public static HashMap<Integer, String> buildDivisionMap() {
        HashMap<Integer, String> divisionMap = new HashMap<>();
        try {
            //SQL Statement
            String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(sql);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                int id = results.getInt("Division_ID");
                String division = results.getString("Division");

                divisionMap.put(id, division);
            }
        }
        catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return divisionMap;
    }

    /**
     * This method queries the database to retrieve a list of US divisions.
     * @return An observable list of division names associated with US divisions in the database.
     */
    public static ObservableList<String> getUSDivisions() {
        ObservableList<String> divisionList = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID BETWEEN 1 AND 54";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(query);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                String divisionName = results.getString("Division");
                divisionList.add(divisionName);
            }
        }
        catch(SQLException exception) {
            exception.printStackTrace();
        }
        return divisionList;
    }

    /**
     * Returns an ObservableList of Canadian Divisions from the database. This method fetches the
     * divisions with IDs ranging from
     * 60 to 72 from the database and converts them into an observable list of division names.
     * @return ObservableList of Division names. An observable list is a collection that allows
     * listeners to track changes when they occur. This means that changes to the list will be
     * automatically reflected in any user interface that displays it.
     */
    public static ObservableList<String> getCADivisions() {
        ObservableList<String> divisionList = FXCollections.observableArrayList();
        try {
            //SQL Statement
            String sqlStatement = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID BETWEEN 60 AND 72";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(sqlStatement);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                String division = results.getString("Division");
                divisionList.add(division);
            }
        }
        catch(SQLException e) {
            System.err.println("Error querying database: " + e.getMessage());
        }
        return divisionList;
    }

    /**
     * This method fetches a collection of UK divisions that are stored in the database. The fetched list of divisions is
     * returned to the form of an ObservableList, which contains the names of all the UK divisions.
     * @return An ObservableList that contains the names of all the UK divisions stored in the database.
     */
    public static ObservableList<String> retrieveUKDivisions() {
        ObservableList<String> divisionList = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID BETWEEN 101 AND 104";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String divisionName = resultSet.getString("Division");
                divisionList.add(divisionName);
            }
        }
        catch(SQLException exception) {
            exception.printStackTrace();
        }
        return divisionList;
    }
}
