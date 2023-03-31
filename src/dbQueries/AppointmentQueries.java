package dbQueries;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import languages.LanguageManager;
import utilities.AlertManager;
import utilities.TimeManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * This class is responsible for executing database queries related to Appointment objects.
 */
public class AppointmentQueries {

    /**
     * Returns an ObservableList containing all Appointments from the database.
     *
     * @return An ObservableList containing all Appointments.
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ResultSet results = null;
        try {
            String query = "SELECT * FROM appointments";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(query);
            results = statement.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return processResults(results);
    }

    /**
     * Retrieves all Appointments from the database and sorts them by Type in ascending order.
     *
     * @return An ObservableList of Appointments sorted by their Type.
     */
    public static ObservableList<Appointment> getOrderedAppointments() throws SQLException {
        ResultSet results = null;
        try {
            String query = "SELECT * FROM appointments ORDER BY Type ASC";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(query);
            results = statement.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return processResults(results);
    }

    /**
     * Retrieves all Appointments except the one specified by the given ID.
     *
     * @param id The ID of the Appointment to exclude.
     * @return An ObservableList of Appointments.
     */
    public static ObservableList<Appointment> getAllAppointmentsExcept(int id) throws SQLException {
        ResultSet results = null;
        try {
            String query = "SELECT * FROM appointments WHERE Appointment_ID != ?";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(query);
            statement.setInt(1, id);
            results = statement.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return processResults(results);
    }

    /**
     * Retrieves all Appointments with the specified Customer_ID.
     *
     * @param id The Customer_ID to match.
     * @return An ObservableList of Appointments.
     */
    public static ObservableList<Appointment> getAppointmentsByCustomerId(int id) throws SQLException {
        ResultSet results = null;
        try {
            String query = "SELECT * FROM appointments WHERE Customer_ID = ?";
            PreparedStatement statement = JDBC.openConnection().prepareStatement(query);
            statement.setInt(1, id);
            results = statement.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return processResults(results);
    }

    /**
     * Processes the results of a database query and returns an ObservableList of Appointment objects.
     *
     * @param results The ResultSet to process.
     * @return An ObservableList of Appointment objects.
     */
    private static ObservableList<Appointment> processResults(ResultSet results) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        while (results.next()) {
            int appointmentId = results.getInt("Appointment_ID");
            int customerId = results.getInt("Customer_ID");
            String type = results.getString("Type");
            String location = results.getString("Location");
            Timestamp startTimestamp = results.getTimestamp("Start");
            Timestamp endTimestamp = results.getTimestamp("End");
            ZonedDateTime startTime = TimeManager.toSystemTimeZone(startTimestamp);
            ZonedDateTime endTime = TimeManager.toSystemTimeZone(endTimestamp);
            String createdBy = results.getString("Created_By");
            String lastUpdateBy = results.getString("Last_Update_By");
            appointments.add(new Appointment(appointmentId, customerId, type, location, startTime, endTime, createdBy, lastUpdateBy));
        }
        return appointments;
    }
}
    /**
     * Returns an ObservableList of all Appointments that contain a given Contact ID.
     * Used for generating the Contact report.
     * @param contactId The ID of the Contact to search for.
     * @return An ObservableList of all Appointments with the specified Contact ID.
     */
    public static ObservableList<Appointment> getAppointmentsByContact(int contactId) {
        ResultSet results = null;
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM APPOINTMENTS WHERE Contact_ID = ?";
            PreparedStatement statement = DBConnector.getConnection().prepareStatement(sql);
            statement.setInt(1, contactId);
            results = statement.executeQuery();
        }
        catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return process(results);
    }

    /**
     * Returns the next valid Appointment ID.
     * @return The next valid Appointment ID.
     * @throws SQLException If there was an error with the database query.
     */
    public static int getNextAppointmentId() throws SQLException {
        int nextId = -1;
        try {
            String sql = "SELECT MAX(Appointment_ID) AS max_app_id FROM APPOINTMENTS";
            PreparedStatement statement = DBConnector.getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                nextId = result.getInt("max_app_id") + 1;
            }
        }
        catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return nextId;
    }

    /**
     * Removes an appointment from the database.
     * @param appointmentId The ID of the appointment to remove.
     * @throws SQLException If there was an error with the database query.
     */
    public static void removeAppointment(int appointmentId) throws SQLException {
        try {
            String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
            PreparedStatement statement = DBConnector.getConnection().prepareStatement(sql);
            statement.setInt(1, appointmentId);
            statement.execute();
        }
        catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    /**
     * Adds an appointment to the database.
     * @param appointment The appointment to add to the database.
     * @throws SQLException If there was an error with the database query.
     */
    public static void addAppointment(Appointment appointment) throws SQLException {
        if (appointment != null) {
            try {
                String sql = "INSERT INTO APPOINTMENTS (Appointment_ID, Title, Description, Location, Type, Start, End, " +
                        "Customer_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, User_ID, Contact_ID) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement statement = DBConnector.getConnection().prepareStatement(sql);

                statement.setInt(1, appointment.getAppointmentId());
                statement.setString(2, appointment.getTitle());
                statement.setString(3, appointment.getDescription());
                statement.setString(4, appointment.getLocation());
                statement.setString(5, appointment.getType());
                statement.setTimestamp(6, TimeManager.timestamp(appointment.getStart()));
                statement.setTimestamp(7, TimeManager.timestamp(appointment.getEnd()));
                statement.setInt(8, appointment.getCustomerId());
                statement.setTimestamp(9, TimeManager.timestamp(appointment.getCreateDate()));
                statement.setString(10, "admin");
                statement.setTimestamp(11, appointment.getLastUpdate());
                statement.setString(12, "admin");
                statement.setInt(13, appointment.getUserId());
                statement.setInt(14, appointment.getContactId());
                statement.execute();
            }
            catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        }
    }
}
/**
 * Provides methods related to Appointments in the database.
 * */
public class AppointmentManager {

    /**
     * Attempts to update an Appointment.
     * @param appointment The Appointment which the user desires to update.
     * */
    public static void updateAppointment(Appointment appointment) {
        try {
            String updateAppointmentQuery = "UPDATE APPOINTMENTS SET " +
                    "Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                    "Customer_ID = ?, Created_By = ?, Last_Updated_By = ?, User_ID = ?, Contact_ID = ? " +
                    "WHERE Appointment_ID = ?";
            PreparedStatement statement = DBConnector.getConnection().prepareStatement(updateAppointmentQuery);

            statement.setString(1, appointment.getTitle());
            statement.setString(2, appointment.getDescription());
            statement.setString(3, appointment.getLocation());
            statement.setString(4, appointment.getType());
            statement.setTimestamp(5, TimeManager.timestamp(appointment.getStart()));
            statement.setTimestamp(6, TimeManager.timestamp(appointment.getEnd()));
            statement.setInt(7, appointment.getCustomerId());
            statement.setString(8, "admin");
            statement.setString(9, "admin");
            statement.setInt(10, appointment.getUserId());
            statement.setInt(11, appointment.getContactId());
            statement.setInt(12, appointment.getAppointmentId());
            statement.executeUpdate();
        }
        catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    /**
     * Processes a ResultSet into a list of Appointments.
     * @param appointmentResultSet ResultSet generated from querying for Appointments in the database.
     * @return ObservableList of Appointments created from the ResultSet.
     * */
    private static ObservableList<Appointment> processResultSet(ResultSet appointmentResultSet) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            while (appointmentResultSet.next()) {
                int appointmentId = appointmentResultSet.getInt("Appointment_ID");
                String title = appointmentResultSet.getString("Title");
                String description = appointmentResultSet.getString("Description");
                String location = appointmentResultSet.getString("Location");
                String type = appointmentResultSet.getString("Type");
                ZonedDateTime start = TimeManager.toLocal(appointmentResultSet.getTimestamp("Start"));
                ZonedDateTime end = TimeManager.toLocal(appointmentResultSet.getTimestamp("End"));
                String createdBy = appointmentResultSet.getString("Created_By");
                ZonedDateTime createDate = TimeManager.toLocal(appointmentResultSet.getTimestamp("Create_Date"));
                Timestamp lastUpdate = appointmentResultSet.getTimestamp("Last_Update");
                String lastUpdatedBy = appointmentResultSet.getString("Last_Updated_By");
                int customerId = appointmentResultSet.getInt("Customer_ID");
                int userId = appointmentResultSet.getInt("User_ID");
                int contactId = appointmentResultSet.getInt("Contact_ID");
                Appointment appointment = new Appointment(
                        appointmentId, title, description, location, type, start, end,
                        customerId, userId, contactId, createDate, createdBy, lastUpdate, lastUpdatedBy
                );
                appointmentList.add(appointment);
            }
        }
        catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return appointmentList;
    }

    /**
     * Attempts to remove all Appointments associated with a specified customerId.
     * The user is then notified if the Appointments were removed or if they were not.
     * @param appointments An ObservableList containing all Appointments to check.
     * @param customerId The Customer_ID of the customer to be removed.
     * */
    public static void removeAppointmentsByCustomerId(ObservableList<Appointment> appointments, int customerId) {
        boolean success = false;
        try {
            for (Appointment appointment : appointments) {
                if (appointment.getCustomerId() == customerId) {
                    removeAppointment(appointment.getAppointmentId());
                }
            }
            success = true;
        }
        catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        if (success) {
            Alerter.alert(LanguageManager.getLocalString("Removal_Successful"),
                    LanguageManager.getLocalString("Success"));
        }
        else {
            Alerter.alert(LanguageManager.getLocalString("Removal_Unsuccessful"),
                    LanguageManager.getLocalString("Error"));
        }
    }

    /**
     * Attempts to remove the Appointment with the specified appointmentId.
     * @param appointmentId The ID of the Appointment to be removed.
     * */
    private static void removeAppointment(int appointmentId) throws SQLException {
        String deleteAppointmentQuery = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement statement = DBConnector.getConnection().prepareStatement(deleteAppointmentQuery);
        statement.setInt(1, appointmentId);
        statement.executeUpdate();
    }

}
