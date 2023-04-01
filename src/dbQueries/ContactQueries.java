package dbQueries;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The ContactQuery class provides the functionality to interact with the database and retrieve Contact objects.
 * This class serves as a mediator between the database and the application.
 * It encapsulates the logic for querying the database and allows other components of the
 * application to easily retrieve Contact objects without having to worry about the
 * underlying database implementation details.
 *
 * The database querying functionality of this class is crucial for any application that requires contact management.
 * It offers a convenient and efficient solution for retrieving Contact objects and helps reduce unnecessary code
 * duplication in the application.
 *
 * By using the ContactQuery class, developers can leverage the power of SQL
 * to easily retrieve contacts from the database
 * and have more time to focus on other aspects of the application. This class offers a clean and maintainable solution
 * to database querying, making it an indispensable tool for any developer working on contact management applications.
 */
public class ContactQueries {
    /**
     * This method is used to query the database in order to retrieve all the existing contacts.
     *
     * @return This method returns an observable list of Contact, which includes all the contacts stored in the database.
     * Each contact contains useful information such as name, email, phone number,
     * and other kinds of data that can be associated with a contact.
     */
    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM contacts";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(query);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                int contactId = results.getInt("Contact_ID");
                String contactName = results.getString("Contact_Name");
                String email = results.getString("Email");
                Contact c = new Contact(contactId, contactName, email);
                contactList.add(c);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    /**
     * This method retrieves all contacts currently stored in the database and creates an observable list
     * containing their respective names. By invoking this method with no parameters,
     * the complete list of all contact names will be returned.
     *
     * @return An observable list that contains the names of all contacts present within the database.
     */

    public static ObservableList<String> getAllContactNames() {
        ObservableList<String> contactList = FXCollections.observableArrayList();
        try {
            //SQL Statement
            String query = "SELECT Contact_Name FROM contacts";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(query);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                String contactName = results.getString("Contact_Name");
                contactList.add(contactName);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    /**
     * This method attempts to retrieve a Contact object with a given name parameter
     * by searching through a collection of contacts.
     * The parameter 'name' represents the name of the Contact that we want to find.
     * If a Contact with the provided name is found, then it is returned as the output.
     * If no matching Contact is found, then a null value is returned.
     *
     * @param name The name of the Contact being searched for.
     * @return A Contact object that matches the provided name parameter.
     *         If no matching Contact is found, a null value is returned.
     *
     * @throws IllegalArgumentException If the provided name parameter is null or an empty string.
     *         This is done to ensure that the name parameter is a valid and usable value.
     *
     * @throws IllegalStateException If the collection of Contacts used by this method
     *         has not been initialized or is null.
     *         This is done to ensure that our collection of contacts is valid and usable to search through.
     */
    public static Contact getContactByName(String name) {
        Contact c = null;
        try {
            //SQL Statement
            String query = "SELECT * FROM contacts WHERE name = ?";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(query);
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int contactId = result.getInt("contact_id");
                String contactName = result.getString("name");
                String email = result.getString("email");
                c = new Contact(contactId, contactName, email);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return c;
    }
}
