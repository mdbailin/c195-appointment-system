package dbQueries;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The CountryDB class serves as a module for managing data and accessing country objects through the data access
 * object (DAO) pattern. It contains a collection of methods that enable applications to perform CRUD
 * (Create, Read, Update, Delete) operations on country objects in a database.
 *
 * By implementing the DAO pattern, this class promotes separation of concerns and reduces coupling
 * between the application and the underlying database layer. It encapsulates all the
 * database-related code within its methods, simplifying the process of retrieving and manipulating data.
 *
 * Users of this class can easily perform tasks such as inserting a new country into the database,
 * retrieving country data based on a search parameter,
 * updating an existing country's attributes, and deleting a country from the database.
 *
 * Note that this class assumes existence of a pre-configured database connection
 * and appropriate credentials to perform CRUD operations.
 *
 * @author Matthew Bailin
 * @version 1.0
 */
public class CountryQueries {

    /**
     * This method retrieves data on all the countries that are currently stored in the database and
     * packages the information into an ObservableList object for convenient use by the calling application.
     * Each country's name is included in the list.
     *
     * @return An ObservableList that includes the name of each country stored in the database.
     * @throws SQLException if an error occurs while attempting to connect to the database or execute the query.
     */
    public static ObservableList<String> getAllCountries() throws SQLException {
        ObservableList<String> countryList = FXCollections.observableArrayList();
        //SQL Statement
        String countryQuery = "SELECT * FROM COUNTRIES";

        try (PreparedStatement statement = JDBC.openConnection().prepareStatement(countryQuery);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                String country = results.getString("Country");
                countryList.add(country);
            }
        } catch (SQLException sqlException) {
            throw new SQLException("Error occurred while fetching country names", sqlException.getCause());
        }
        return countryList;
    }
}
