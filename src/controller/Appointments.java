package controller;

import dbQueries.AppointmentQueries;
import dbQueries.ContactQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import languages.LanguageManager;
import utilities.TimeManager;
import utilities.Validator;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.ZonedDateTime;

/**
 * AppointmentForm is the controller for AppointmentForm.fxml.
 * Responsible for adding and modifying Appointments.
 * */
public class Appointments {
    public Spinner<LocalTime> startTimeSpinner;
    public Spinner<LocalTime> endTimeSpinner;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public TextField appointmentIdField;
    public TextField locationField;
    public TextField titleField;
    public TextField descriptionField;
    public ComboBox<String> contactComboBox;
    public TextField typeField;
    public TextField userIdField;
    public TextField customerIdField;
    public Button saveButton;
    public Button cancelButton;
    public Label startDateLabel;
    public Label startTimeLabel;
    public Label endDateLabel;
    public Label endTimeLabel;
    public Label titleLabel;
    public Label descriptionLabel;
    public Label contactLabel;
    public Label typeLabel;
    public Label userIdLabel;
    public Label customerIdLabel;
    public Label locationLabel;
    @FXML
    public Label appointmentIdLabel;

    /**
     * Initializes the AppointmentForm by setting up all the graphical user interface
     * elements necessary for proper functioning.
     *
     * This method also performs a check for whether there is a currently selected Appointment,
     * and if so, populates the
     * corresponding fields accordingly. This is a crucial aspect of the
     * Appointment's presentation layer, as it ensures
     * that the user is presented with accurate and up-to-date information at all times.
     *
     * By executing this method, the AppointmentForm is now fully equipped to
     * handle user interactions, and retrieve, update,
     * or create Appointments within the application's domain.
     */
    @FXML
    private void initialize() {
        /**
         * Initializes the appointment form by setting up the UI elements with appropriate labels and values.
         * If a selected appointment exists, the fields are pre-filled with its data.
         * Otherwise, the appointment ID field is set to the next available ID.
         */

        ObservableList<LocalTime> hours = generateHours();

        SpinnerValueFactory<LocalTime> startTimeValueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(hours);
        SpinnerValueFactory<LocalTime> endTimeValueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(hours);
        startTimeSpinner.setValueFactory(startTimeValueFactory);
        endTimeSpinner.setValueFactory(endTimeValueFactory);

        startTimeValueFactory.setValue(LocalTime.of(LocalTime.now().getHour(), 0));
        endTimeValueFactory.setValue(LocalTime.of(LocalTime.now().getHour(), 0));

        contactComboBox.setItems(ContactQueries.getAllContactNames());

        startDateLabel.setText(LanguageManager.getTranslation("Start_Date"));
        startTimeLabel.setText(LanguageManager.getTranslation("Start_Time") +
                TimeManager.convertToESTAndFormatLabel(startTimeSpinner.getValue()));
        endDateLabel.setText(LanguageManager.getTranslation("End_Date"));
        endTimeLabel.setText(LanguageManager.getTranslation("End_Time") +
                TimeManager.convertToESTAndFormatLabel(startTimeSpinner.getValue()));
        titleLabel.setText(LanguageManager.getTranslation("Title"));
        descriptionLabel.setText(LanguageManager.getTranslation("Description"));
        contactLabel.setText(LanguageManager.getTranslation("Contact"));
        typeLabel.setText(LanguageManager.getTranslation("Type"));
        userIdLabel.setText(LanguageManager.getTranslation("User_ID"));
        customerIdLabel.setText(LanguageManager.getTranslation("Customer_ID"));
        locationLabel.setText(LanguageManager.getTranslation("Location"));
        appointmentIdLabel.setText(LanguageManager.getTranslation("Appointment_ID"));
        saveButton.setText(LanguageManager.getTranslation("Save"));
        cancelButton.setText(LanguageManager.getTranslation("Cancel"));

        if (Scheduler.selectedAppointment != null) {
            LocalDate startDate = TimeManager.toESTTimeZone(Scheduler.selectedAppointment.getStart()).toLocalDate();
            LocalTime startTime = TimeManager.toESTTimeZone(Scheduler.selectedAppointment.getStart()).toLocalTime();
            LocalDate endDate = TimeManager.toESTTimeZone(Scheduler.selectedAppointment.getEnd()).toLocalDate();
            LocalTime endTime = TimeManager.toESTTimeZone(Scheduler.selectedAppointment.getEnd()).toLocalTime();

            startDatePicker.setValue(startDate);
            startTimeValueFactory.setValue(startTime);
            startTimeLabel.setText(LanguageManager.getTranslation("Start_Time") +
                    TimeManager.convertToESTAndFormatLabel(startTimeSpinner.getValue()));

            endDatePicker.setValue(endDate);
            endTimeValueFactory.setValue(endTime);
            endTimeLabel.setText(LanguageManager.getTranslation("End_Time") +
                    TimeManager.convertToESTAndFormatLabel(endTimeSpinner.getValue()));

            contactComboBox.getSelectionModel().selectFirst();
            contactComboBox.getSelectionModel().select(Scheduler.selectedAppointment.getContact_ID() - 1);

            titleField.setText(Scheduler.selectedAppointment.getTitle());
            descriptionField.setText(Scheduler.selectedAppointment.getDescription());
            typeField.setText(Scheduler.selectedAppointment.getType());
            userIdField.setText(String.valueOf(Scheduler.selectedAppointment.getUser_ID()));
            customerIdField.setText(String.valueOf(Scheduler.selectedAppointment.getCustomer_ID()));
            locationField.setText(Scheduler.selectedAppointment.getLocation());
            appointmentIdField.setText(String.valueOf(Scheduler.selectedAppointment.getAppointment_ID()));
        } else {
            contactComboBox.getSelectionModel().selectFirst();
            try {
                appointmentIdField.setText(String.valueOf(AppointmentQueries.getNextAppointmentId()));
            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        }
    }


    /**
     * Saves or modifies an Appointment, then closes the window.
     *
     * @param actionEvent generated from clicking the button.
     */
    public void onSaveButton(ActionEvent actionEvent) throws SQLException {
        if (validateFields()) {
            if (Scheduler.selectedAppointment != null) {
                AppointmentQueries.updateAppointment(createAppointment());
            } else {
                addAppointment();
            }
            Scheduler.selectedAppointment = null;
            Scheduler.selectedCustomer = null;
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Closes the AppointmentForm without saving anything to the database.
     */
    public void onCancelButton(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scheduler.selectedAppointment = null;
        stage.close();
    }

    /**
     * Creates and adds an appointment to the database.
     */
    public void addAppointment() throws SQLException {
        AppointmentQueries.addAppointment(createAppointment());
    }

    /**
     * Creates an Appointment object from the AppointmentForm fields.
     * Must be called after validation.
     *
     * @return Appointment created from AppointmentForm fields.
     */
    public Appointment createAppointment() {
        int appointmentId = Integer.parseInt(appointmentIdField.getText());
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        ZonedDateTime start = readDatePicker(0);
        ZonedDateTime end = readDatePicker(1);
        int customerId = Integer.parseInt(customerIdField.getText());
        int userId = Integer.parseInt(userIdField.getText());
        int contactId = contactComboBox.getSelectionModel().getSelectedIndex() + 1;
        ZonedDateTime createDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"));
        String createdBy = "admin";
        Timestamp lastUpdate = TimeManager.getTimestampForCurrentUTCTime();
        String lastUpdatedBy = "admin";
        return new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId,
                contactId, createDate, createdBy, lastUpdate, lastUpdatedBy);
    }

    /**
     * Used to call validation methods on the Appointment form fields.
     *
     * @return true if all inputs are validated, false if any inputs are not validated.
     */
    public boolean validateFields() {
        boolean titleInput = Validator.validateVarcharFifty("Title", titleField.getText());
        boolean descriptionInput = Validator.validateVarcharFifty("Description", descriptionField.getText());
        boolean locationInput = Validator.validateVarcharFifty("Location", locationField.getText());
        boolean typeInput = Validator.validateVarcharFifty("Type", typeField.getText());
        boolean userIdInputInt = Validator.isInt(userIdField.getText());
        boolean userIdInput = false;
        if (userIdInputInt) {
            userIdInput = Validator.isUserId(Integer.parseInt(userIdField.getText()));
        }
        boolean customerIdInputInt = Validator.isInt(customerIdField.getText());
        boolean customerIdInput = false;
        if (customerIdInputInt) {
            customerIdInput = Validator.isCustomerId(Integer.parseInt(customerIdField.getText()));
        }
        boolean datesInput = Validator.isTimeValid(readDatePicker(0), readDatePicker(1),
                TimeManager.toESTTimeZone(startTimeSpinner.getValue()), TimeManager.toESTTimeZone(endTimeSpinner.getValue()));
        boolean dateAvailable = false;
        if (customerIdInput) {
            dateAvailable = Validator.isAppointmentAvailable(readDatePicker(0), readDatePicker(1),
                    Integer.parseInt(appointmentIdField.getText()));
        }
        boolean[] inputs = {titleInput, descriptionInput, locationInput, typeInput, userIdInputInt, userIdInput,
                customerIdInputInt, customerIdInput, datesInput, dateAvailable};
        for (boolean b : inputs) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    /**
     * Uses TimeManager to combine time and date components.
     *
     * @param picker is either 0 for Start or 1 for End.
     * @return ZonedDateTime created from combining the date and time picker values.
     */
    private ZonedDateTime readDatePicker(int picker) {
        LocalDate date;
        LocalTime time;
        if (picker == 0) {
            date = startDatePicker.getValue();
            time = startTimeSpinner.getValue();
        } else {
            date = endDatePicker.getValue();
            time = endTimeSpinner.getValue();
        }
        return TimeManager.combineDateAndTime(date, time);
    }

    /**
     * Generates an ObservableList of LocalTime objects representing the hours from 0000 to 2300, with a 30-minute interval.
     *
     * @return the ObservableList of LocalTime objects
     */
    private ObservableList<LocalTime> generateHours() {
        ObservableList<LocalTime> hoursList = FXCollections.observableArrayList();
        for (int hour = 0; hour < 24; hour++) {
            for (int minuteInterval = 0; minuteInterval <= 1; minuteInterval++) {
                hoursList.add(TimeManager.generateLocalTime(hour, minuteInterval * 30));
            }
        }
        return hoursList;
    }

    /**
     * Updates the text of startTimeLabel with the selected hour in EST time format.
     *
     *
     */
    public void onStartSpinnerClick(MouseEvent mouseEvent) {
        String startLabelText = LanguageManager.getTranslation("Start_Time") +
                TimeManager.convertToESTAndFormatLabel((LocalTime) startTimeSpinner.getValue());
        startTimeLabel.setText(startLabelText);
    }

    /**
     * Updates the label showing the end time with the selected hour in Eastern Standard Time.
     *
     *
     */
    public void onEndSpinnerClick() {
        String formattedTime = TimeManager.convertToESTAndFormatLabel(endTimeSpinner.getValue());
        String label = LanguageManager.getTranslation("End_Time") + formattedTime;
        endTimeLabel.setText(label);
    }


}
