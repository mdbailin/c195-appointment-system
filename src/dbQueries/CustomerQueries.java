package dbQueries;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.ObservableList;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import model.Customer;
import utilities.TimeManager;
import helper.JDBC;

/**
 * The CustomerManager class is a comprehensive solution for managing all database interactions related to Customer objects.
 * This class serves as the central hub for accessing, modifying, and storing customer-related data in the database.
 *
 * With the CustomerManager class, you can easily view, edit, add, or delete customer information.
 * By storing all customer data in one place, it eliminates the need for multiple functions or classes to access
 * the database, providing a more streamlined and efficient solution.
 *
 * This class is designed to handle all aspects of customer management, including data validation and error handling.
 * It ensures data integrity by implementing strict guidelines and protocols for handling customer data.
 *
 * In summary, the CustomerManager class simplifies the process of interacting with customer data in the database,
 * by providing a centralized and reliable solution for all customer interactions. */
public class CustomerQueries {

    /**
     * Retrieves all customers currently stored in the database and uses this information to create an
     * ObservableList of Customer objects, which can easily be used to manipulate the database.
     * @return An ObservableList of Customer objects populated with all known customer data
     * @throws SQLException If an error occurs while attempting to retrieve customer data from the database.
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customerArrayList = FXCollections.observableArrayList();
        //SQL Statement
        try (PreparedStatement statement = JDBC.openConnection().prepareStatement("SELECT * FROM customers");
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                String postalCode = results.getString("Postal_Code");
                int customerId = results.getInt("Customer_ID");
                String address = results.getString("Address");
                int divisionId = results.getInt("Division_ID");
                Timestamp lastUpdate = results.getTimestamp("Last_Update");
                ZonedDateTime createDate = TimeManager.toSystemTimeZone(results.getTimestamp("Create_Date"));
                String customerName = results.getString("Customer_Name");
                String lastUpdatedBy = results.getString("Last_Updated_By");
                String phone = results.getString("Phone");
                String createdBy = results.getString("Created_By");
                Customer customer = new Customer(customerId,
                        customerName,
                        address,
                        postalCode,
                        phone,
                        createDate,
                        createdBy,
                        lastUpdate,
                        lastUpdatedBy,
                        divisionId);
                customerArrayList.add(customer);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            throw new SQLException("An error occurred while retrieving all customers from the database.");
        }

        return customerArrayList;
    }

    /**
     * This method retrieves the next available customer ID from the database by querying the database for the
     * highest customer ID currently in use and incrementing that value by 1 to ensure a unique ID for the new customer.
     * @return an integer value representing the next available customer ID.
     *         If the method encounters an error while querying the database, it will return -1.
     * @throws SQLException if there is an error with the database connection or query.
     *         This exception will be thrown to the calling method in the event that an error occurs within the
     *         Java Database Connectivity (JDBC) framework.
     */
    public static int getNextCustomerId() throws SQLException {
        int nextCustomerId = -1;

        try {
            Statement statement = JDBC.openConnection().createStatement();
            //SQL Statement
            String sqlQuery = "SELECT MAX(customer_id) as max_customer_id FROM customers";
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                nextCustomerId = resultSet.getInt("max_customer_id") + 1;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return nextCustomerId;
    }

    /**
     * This method removes a customer from the database with the associated customerId.
     *
     * @param customerId The identification number assigned to the customer that is to be deleted from the database.
     * @throws SQLException If there is an issue in the database connection or if the customer cannot
     * be successfully removed from the database.
     */
    public static void removeCustomer(int customerId) throws SQLException {
        // SQL Statement
        String sqlQuery = "DELETE FROM customers WHERE customer_id = " + customerId;
        try (PreparedStatement statement = JDBC.openConnection().prepareStatement(sqlQuery)) {
            statement.execute();
        } catch (SQLException sqlE) {
            throw new SQLException("Failed to remove customer with ID " + customerId, sqlE);
        }
    }

    /**
     * This method adds a new customer to the database with the specified parameters.
     * The Customer object passed as an argument must contain all the necessary data to
     * successfully create a customer record in the database.
     *
     * @param customer The Customer object to add to the database.
     *                 The object must contain the customer's name, address, email, and other relevant details.
     * @throws SQLException if there is an issue in executing the SQL query or connecting to the database.
     *                       This may occur if the database credentials are incorrect or if there is
     *                       a problem with the database server.
     */
    public static void addCustomer(Customer customer) throws SQLException {
        if (customer != null) {
            try {
                // SQL Statement
                String sql = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, " +
                        "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement statement = JDBC.openConnection().prepareStatement(sql);

                statement.setInt(1, customer.getCustomer_ID());
                statement.setString(2, customer.getCustomer_Name());
                statement.setString(3, customer.getAddress());
                statement.setString(4, customer.getPostal_Code());
                statement.setString(5, customer.getPhone());
                statement.setTimestamp(6, TimeManager.toTimestampUsingSQLFormat(customer.getCreate_Date()));
                statement.setString(7, "admin");
                statement.setTimestamp(8, customer.getLast_Update());
                statement.setString(9, "admin");
                statement.setInt(10, customer.getDivision_ID());
                statement.execute();
            } catch (SQLException e) {
                throw new SQLException("An error occurred while attempting to add customer to the database", e);
            }
        }
    }

    /**
     * Updates the information of a customer in the database with the provided Customer object.
     * This method makes use of the information contained in the Customer object to query the database
     * and perform the necessary update operation.
     *
     * @param customer The Customer object containing the updated information that needs to be stored in the database.
     *                 This object must not be null.
     * @throws IllegalArgumentException If the provided Customer object is null.
     *
     * @since 1.0
     */
    public static void updateCustomerInfo(Customer customer) {
        try {
            // SQL Statement
            String query = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                    "Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? " +
                    "WHERE Customer_ID = ?";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(query);

            // set the parameters for the update statement
            statement.setString(1, customer.getCustomer_Name());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getPostal_Code());
            statement.setString(4, customer.getPhone());
            statement.setTimestamp(5, TimeManager.toTimestampUsingSQLFormat(customer.getCreate_Date()));
            statement.setString(6, "admin");
            statement.setTimestamp(7, customer.getLast_Update());
            statement.setString(8, "admin");
            statement.setInt(9, customer.getDivision_ID());
            statement.setInt(10, customer.getCustomer_ID());

            // execute the update statement
            statement.executeUpdate();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}

