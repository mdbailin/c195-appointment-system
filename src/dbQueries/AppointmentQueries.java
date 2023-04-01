package dbQueries;

import helper.JDBC;
import javafx.collections.FXCollections;
import utilities.TimeManager;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import utilities.AlertManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import model.Appointment;
import languages.LanguageManager;
import java.sql.SQLException;
import java.time.ZonedDateTime;

/**
 * This class handles the execution of database queries specifically for Appointment objects.
 * It provides functionality for creating, deleting, updating, and retrieving Appointment data from a database.
 * This class serves as a bridge between the database and the rest of the application's logic for Appointment objects.
 * It interfaces with the database to execute and
 * retrieve query results that are used to populate and update Appointment models.
 */
public class AppointmentQueries {

    /**
     * Returns an ObservableList containing all Appointments retrieved from the database.
     * The method establishes a connection to the database, creates an SQL statement to retrieve all Appointment
     * records, executes the statement, and maps each record to a new Appointment object which is added to the
     * ObservableList. If there is an error executing the SQL statement, a SQLException is thrown.
     *
     * @return An ObservableList containing all Appointments.
     * @throws SQLException if there is an error executing the SQL statement.
     */
    public static ObservableList<Appointment> fetchAllAppointments() throws SQLException {
        ResultSet resultSet = null;
        try {
            //SQL Statement
            String sqlQuery = "SELECT * FROM appointments";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(sqlQuery);
            resultSet = statement.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return processResultsToObservableList(resultSet);
    }

    /**
     * This method is used to retrieve all the Appointments from the database,
     * and sort them based on the Type attribute.
     * The Appointments are sorted in ascending order based on the Type attribute.
     *
     * @return An ObservableList that contains all the Appointments sorted by Type in ascending order.
     * @throws SQLException Thrown whenever there is an error encountered during the retrieval of data.
     * This might be due to complications such as network unavailability or database unreachable.
     */
    public static ObservableList<Appointment> getOrderedAppointments() throws SQLException {
        ResultSet results = null;
        try {
            //SQL Statement
            String queryString = "SELECT * FROM appointments ORDER BY Type ASC";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(queryString);
            results = statement.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("An error occurred while retrieving the appointments from the database.");
        }
        return processResultsToObservableList(results);
    }

    /**
     * This method retrieves an ObservableList of all appointments in the system,
     * except for the one with the specified ID.
     * Exclusion of the specified appointment is determined by passing in its unique ID as a parameter to the method.
     *
     * @param appointmentId The unique ID of the appointment to be excluded from the ObservableList.
     * @return An ObservableList of all appointments (except for the one with the specified ID).
     * @throws SQLException If there is an error accessing the database.
     */
    public static ObservableList<Appointment> getAllAppointmentsExceptForOne(int appointmentId) throws SQLException {
        ResultSet results = null;
        try {
            //SQL Statement
            String query = "SELECT * FROM appointments WHERE Appointment_ID != ?";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(query);
            statement.setInt(1, appointmentId);
            results = statement.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return processResultsToObservableList(results);
    }

    /**
     * This method receives a ResultSet object, which is a database result set that contains appointment information,
     * and processes the data to create an ObservableList of Appointment objects.
     *
     * @param resultSet The ResultSet object to be processed.
     * @return An ObservableList of Appointment objects containing the appointment data processed from the ResultSet object.
     * @throws SQLException If there is an error executing the SQL query, such as a syntax error or a database connection issue.
     */
    private static ObservableList<Appointment> processResultsToObservableList(ResultSet resultSet) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        while (resultSet.next()) {
            int appointmentId = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            ZonedDateTime startDateTime = TimeManager.toSystemTimeZone(resultSet.getTimestamp("Start"));
            ZonedDateTime endDateTime = TimeManager.toSystemTimeZone(resultSet.getTimestamp("End"));
            String createdBy = resultSet.getString("Created_By");
            ZonedDateTime createDateTime = TimeManager.toSystemTimeZone(resultSet.getTimestamp("Create_Date"));
            Timestamp lastUpdateTimestamp = resultSet.getTimestamp("Last_Update");
            String lastUpdatedBy = resultSet.getString("Last_Updated_By");
            int customerId = resultSet.getInt("Customer_ID");
            int userId = resultSet.getInt("User_ID");
            int contactId = resultSet.getInt("Contact_ID");
            Appointment appointment = new Appointment(appointmentId, title, description, location, type, startDateTime, endDateTime,
                    customerId, userId, contactId, createDateTime, createdBy, lastUpdateTimestamp, lastUpdatedBy);
            appointments.add(appointment);
        }
        return appointments;
    }

    /**
     * Retrieves all Appointments that correspond to a given Contact ID. This method is specifically designed to be
     * called when generating Contact reports. It takes a Contact ID as a parameter and returns an ObservableList of
     * Appointment objects that contain the specified Contact ID. If there are no appointments matching the given ID,
     * an empty list will be returned.
     *
     * @param contactId The unique ID of the Contact to search for.
     * @return An ObservableList containing all Appointments that correspond to the given Contact ID.
     * @throws SQLException if an exception occurs during the SQL operation.
     */
    public static ObservableList<Appointment> getAppointmentsByContactId(int contactId) throws SQLException {
        ResultSet resultSet = null;
        try {
            //SQL Statement
            String sqlQuery = "SELECT * FROM APPOINTMENTS WHERE Contact_ID = ?";
            PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, contactId);
            resultSet = preparedStatement.executeQuery();
        }
        catch(SQLException exception) {
            exception.printStackTrace();
        }
        return processResultsToObservableList(resultSet);
    }

    /**
     * This method fetches the next available Appointment ID from the
     * database by executing an SQL query and returns it.
     *
     * @return The next available Appointment ID as an integer.
     * @throws SQLException If there was an error while executing the SQL query,
     * in which case a SQLException will be thrown.
     * Specifically, this exception will be thrown if
     * there is any issue with the syntax of the SQL query, if there is an issue
     * with the connection or if there is an issue with the database schema.
     */
    public static int getNextAppointmentId() throws SQLException {
        int nextAppointmentId = -1;
        try {
            //SQL Statement
            String sqlQuery = "SELECT MAX(Appointment_ID) AS max_app_id FROM APPOINTMENTS";
            PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                nextAppointmentId = resultSet.getInt("max_app_id") + 1;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return nextAppointmentId;
    }

    /**
     * Adds an appointment to the database. This method takes an instance of the Appointment class as a
     * parameter and saves it to the database.
     * This method may throw a SQLException if there's any problem while executing database queries during
     * the process of inserting the appointment into the database.
     *
     * @param appointment An instance of the class Appointment, containing the details of
     *                    the appointment to be added to the database.
     * @throws SQLException If there's an issue with the database query
     */
    public static void addAppointment(Appointment appointment) throws SQLException {
        if (appointment != null) {
            try {
                String query = "INSERT INTO APPOINTMENTS (Appointment_ID, Title, " +
                        "Description, Location, Type, Start, End, " +
                        "Customer_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, User_ID, Contact_ID) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                PreparedStatement statement = JDBC.openConnection().prepareStatement(query);
                statement.setInt(1, appointment.getAppointment_ID());
                statement.setString(2, appointment.getTitle());
                statement.setString(3, appointment.getDescription());
                statement.setString(4, appointment.getLocation());
                statement.setString(5, appointment.getType());
                statement.setTimestamp(6, TimeManager.toTimestampUsingSQLFormat(appointment.getStart()));
                statement.setTimestamp(7, TimeManager.toTimestampUsingSQLFormat(appointment.getEnd()));
                statement.setInt(8, appointment.getAppointment_ID());
                statement.setTimestamp(9, TimeManager.toTimestampUsingSQLFormat(appointment.getCreate_Date()));
                statement.setString(10, "admin");
                statement.setTimestamp(11, appointment.getLast_Update());
                statement.setString(12, "admin");
                statement.setInt(13, appointment.getUser_ID());
                statement.setInt(14, appointment.getContact_ID());
                statement.execute();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method updates an existing appointment in the database with the newly provided appointment object.
     *
     * @param appointment The appointment object containing the updated information to be saved in the database.
     *
     * @throws Exception if there was an issue writing the appointment to the database.
     * @throws IllegalArgumentException if the provided appointment object is null.
     *
     * This method will attempt to update the appointment information in the database with what
     * is provided in the appointment object parameter.
     * If there are any issues writing to the database, an exception will be thrown.
     * If the provided appointment object is null, an IllegalArgumentException will be thrown.
     *
     * @return Returns nothing.
     */
    public static void updateAppointment(Appointment appointment) {
        try {
            String sql = "UPDATE APPOINTMENTS SET " +
                    "Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                    "Customer_ID = ?, Created_By = ?, Last_Updated_By = ?, User_ID = ?, Contact_ID = ? " +
                    "WHERE Appointment_ID = ?";
            PreparedStatement pstmt = JDBC.openConnection().prepareStatement(sql);

            pstmt.setString(1, appointment.getTitle());
            pstmt.setString(2, appointment.getDescription());
            pstmt.setString(3, appointment.getLocation());
            pstmt.setString(4, appointment.getType());
            pstmt.setTimestamp(5, TimeManager.toTimestampUsingSQLFormat(appointment.getStart()));
            pstmt.setTimestamp(6, TimeManager.toTimestampUsingSQLFormat(appointment.getEnd()));
            pstmt.setInt(7, appointment.getCustomer_ID());
            pstmt.setString(8, "admin");
            pstmt.setString(9, "admin");
            pstmt.setInt(10, appointment.getUser_ID());
            pstmt.setInt(11, appointment.getContact_ID());
            pstmt.setInt(12, appointment.getAppointment_ID());
            pstmt.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method attempts to remove all appointments with a matching customerId and
     * displays appropriate feedback to the user.
     * If the customer has appointments and they are successfully removed, a success message is displayed to the user.
     * If there are no appointments for the customer, a message is displayed informing the user of this.
     * If the customer ID is invalid or the removal fails for any other reason, an error message is displayed instead.
     * @param appointments The list of appointments to be checked for removal.
     * @param customerId The ID of the customer whose appointments need to be removed.
     * @throws IllegalArgumentException if customerId is null or empty
     * */
    public static void removeAppointmentsByCustomerId(ObservableList<Appointment> appointments, int customerId) {
        boolean success = false;
        try {
            for (Appointment appointment : appointments) {
                if (appointment.getCustomer_ID() == customerId) {
                    removeAppointment(appointment.getAppointment_ID());
                }
            }
            success = true;
        } catch (SQLException exception) {
            // Log the SQLException instead of printing the stack trace
            exception.printStackTrace();
        } finally {
            String successMessage = LanguageManager.getTranslation("Removal_Successful");
            String errorMessage = LanguageManager.getTranslation("Removal_Unsuccessful");
            String titleSuccess = LanguageManager.getTranslation("Success");
            String titleError = LanguageManager.getTranslation("Error");
            AlertManager.showAlert(success ? successMessage : errorMessage, success ? titleSuccess : titleError);
        }
    }

    /**
     * Removes an appointment from the database, with the specified unique identifier.
     * This method takes in an ID corresponding to the appointment to be removed. This
     * method permanently deletes the appointment from the database, and cannot be undone.
     * Therefore, it should be used with caution. The method throws a SQLException if
     * there is an issue accessing the database, for example if the connection has been lost,
     * or if there is a problem executing the removal query.
     *
     * @param appointmentId The unique identifier of the appointment to be removed.
     *
     * @throws SQLException if there is an error accessing the database
     */
    public static void removeAppointment(int appointmentId) throws SQLException {
        // Prepare SQL statement to delete the appointment
        String deleteAppointmentQuery = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement deleteStatement = JDBC.openConnection().prepareStatement(deleteAppointmentQuery);

        // Set parameter values for the prepared statement
        deleteStatement.setInt(1, appointmentId);

        // Execute the SQL statement
        deleteStatement.executeUpdate();
    }

}
