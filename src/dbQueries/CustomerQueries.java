package dbQueries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import model.Customer;
import utilities.TimeManager;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class to handle all database transactions for Customer objects
 */
public class CustomerDBManager {

    /**
     * Gets all Customers from the database
     * @return An ObservableList of Customer objects
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM customers";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(sql);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                int customerId = results.getInt("customer_id");
                String customerName = results.getString("customer_name");
                String address = results.getString("address");
                String postalCode = results.getString("postal_code");
                String phone = results.getString("phone");
                ZonedDateTime createDate = TimeManager.toSystemTimeZone(results.getTimestamp("create_date"));
                String createdBy = results.getString("created_by");
                Timestamp lastUpdate = results.getTimestamp("last_update");
                String lastUpdatedBy = results.getString("last_updated_by");
                int divisionId = results.getInt("division_id");
                Customer customer = new Customer(customerId, customerName, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
                customerList.add(customer);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return customerList;
    }

    /**
     * Gets the next valid Customer_ID from the database
     * @return an integer value equal to current highest number of ID incremented by 1. Returns -1 if an error occurs
     */
    public static int getNextCustomerId() throws SQLException {
        int nextId = -1;

        try {
            String sql = "SELECT MAX(customer_id) as max_customer_id FROM customers";
            Statement statement = JDBC.openConnection().createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                nextId = result.getInt("max_customer_id") + 1;
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return nextId;
    }

    /**
     * Attempts to remove a Customer from the database
     * @param customerId The Customer_ID of the customer to be removed
     */
    public static void removeCustomer(int customerId) {
        try {
            String sql = "DELETE FROM customers WHERE customer_id = " + customerId;
            PreparedStatement statement = JDBC.openConnection().prepareStatement(sql);
            statement.execute();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    /**
     * Attempts to add a new Customer to the database
     * @param customer the new Customer object to be added
     * @throws SQLException if SQL error
     */
    public static void addCustomer(Customer customer) throws SQLException {
        if (customer != null) {
            try {
                String sql = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement statement = JDBC.openConnection().prepareStatement(sql);

                statement.setInt(1, customer.getId());
                statement.setString(2, customer.getName());
                statement.setString(3, customer.getAddress());
                statement.setString(4, customer.getPostalCode());
                statement.setString(5, customer.getPhone());
                statement.setTimestamp(6, TimeManager.toTimestampUsingSQLFormat(customer.getCreateDate()));
                statement.setString(7, "admin");
                statement.setTimestamp(8, customer.getLastUpdate());
                statement.setString(9, "admin");
                statement.setInt(10, customer.getDivisionId());
                statement.execute();
            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        }
    }

    /**
     * Updates the customer information in the database.
     * @param customer The Customer object containing updated information.
     * */
    public static void updateCustomerInfo(Customer customer) throws SQLException {
        try {
            String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                    "Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? " +
                    "WHERE Customer_ID = ?";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(sql);

            // set the parameters for the update statement
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getPostalCode());
            statement.setString(4, customer.getPhone());
            statement.setTimestamp(5, TimeManager.toTimestampUsingSQLFormat(customer.getCreateDate()));
            statement.setString(6, "admin");
            statement.setTimestamp(7, customer.getLastUpdate());
            statement.setString(8, "admin");
            statement.setInt(9, customer.getDivisionId());
            statement.setInt(10, customer.getId());

            // execute the update statement
            statement.executeUpdate();
        }
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}

