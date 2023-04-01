package dbQueries;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utilities.AlertManager;
import utilities.LoginMonitor;
import utilities.TimeManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * This class performs all queries related to user objects in the database.
 */
public class UserQueries {

    /**
     * A utility method to retrieve a list of all users from the database.
     *
     * @return an ObservableList of User objects representing each user in the database.
     * @throws SQLException if an error occurs while retrieving the users.
     */
    public static ObservableList<User> getAllUsers() throws SQLException {
        ObservableList<User> userList = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT * FROM USERS";
            PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(sqlQuery);
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                int userId = results.getInt("User_ID");
                String username = results.getString("User_Name");
                String password = results.getString("Password");
                ZonedDateTime createDate = TimeManager.toSystemTimeZone(results.getTimestamp("Create_Date"));
                String createdBy = results.getString("Created_By");
                Timestamp lastUpdate = results.getTimestamp("Last_Update");
                String lastUpdatedBy = results.getString("Last_Updated_By");
                User user = new User(userId, username, password, createDate, createdBy, lastUpdate, lastUpdatedBy);
                userList.add(user);
            }
        }
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return userList;
    }

    /**
     * Validates user credentials by attempting to authenticate with the given username and password.
     * Each attempt is logged by the LoginMonitor.
     *
     * @param username the username to validate
     * @param password the password to validate
     * @return true if the specified user is validated; false if either of the credentials are incorrect or if an exception occurs
     */
    public static boolean validateCredentials(String username, String password) {
        //Get current time to log the timestamp for the login attempt
        ZonedDateTime time = ZonedDateTime.now();

        try {
            //Construct the SQL query to validate the user's credentials
            String sql = "SELECT * FROM USERS WHERE USER_NAME = '" + username + "' AND password = '" + password + "'";
            //Open a JDBC connection to execute the query
            PreparedStatement statement = JDBC.openConnection().prepareStatement(sql);
            //Execute the query and fetch the results
            ResultSet results = statement.executeQuery();
            results.next();
            //Check if the fetched results match the provided credentials
            if (results.getString("User_Name").equals(username) && results.getString("Password").equals(password)) {
                //If the credentials match, record a successful attempt and return success
                LoginMonitor.recordAttempt(username, "SUCCESSFUL");
                return true;
            }
        } catch (SQLException exception) {
            //If an exception occurs, record the failed attempt and return failure
            LoginMonitor.recordAttempt(username, "FAILED");
        }
        //In case of failed login attempt, show an alert and return failure
        AlertManager.showAlert("Login Failed", "Incorrect credentials");
        return false;
    }
}
