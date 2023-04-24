package controller;

import dbQueries.CustomerQueries;
import dbQueries.AppointmentQueries;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import utilities.TimeManager;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import languages.LanguageManager;
import model.Appointment;
import java.time.LocalDate;
import model.Customer;
import utilities.AlertManager;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * This class is the core controller for the Schedule.fxml GUI file, responsible for handling user interaction with the
 * Appointment and Customer TableView components in a scheduling application.
 * As the primary hub for all scheduling functionalities,
 * this class plays an indispensable role in the application's architecture.
 *
 * Through this class, users can add, delete, modify, and view appointments and customer data within the TableView
 * components of the app. This class serves as an intermediary between the Model and View components,
 * enabling seamless data flow and synchronization between them.
 * It responds to user actions and updates the TableView data when new appointments or
 * customer records are added or edited.
 *
 * Furthermore, this controller provides a centralized location for managing different scheduling app functionalities,
 * such as scheduling appointments, checking availability, and sending notifications.
 * The class implements various methods that adhere to industry best practices,
 * such as defensive programming, error handling, and object-oriented design principles.
 *
 * Overall, this class is an essential component of any scheduling application,
 * providing a rich user experience and intuitive performance.
 * Its well-structured code and comprehensive documentation make it easy to maintain and extend,
 * ensuring the longevity and robustness of the application.
 */
public class Scheduler {
    public Button addAppointmentButton;
    public Button updateAppointmentButton;
    public Button deleteAppointmentButton;
    public RadioButton appointmentByMonthRadioButton;
    public RadioButton appointmentByWeekRadioButton;
    public Label timeZoneDescriptionLabel;
    public Label timeZoneLabel;
    public Label serverTimeZoneDescriptionLabel;
    public Label serverTimeZoneLabel;
    public ToggleButton toggleViewButton;
    public boolean viewingAppointments = true;
    public TableView<Appointment> selectionView;
    public TableView<Customer> custTableView;
    public TableColumn<Appointment, Integer> appointmentIdCol_a;
    public TableColumn<Appointment, String> titleColumn_a;
    public TableColumn<Appointment, String> descriptionColumn_a;
    public TableColumn<Appointment, String> locationColumn_a;
    public TableColumn<Appointment, String> contactColumn_a;
    public TableColumn<Appointment, String> typeColumn_a;
    public TableColumn<Appointment, ZonedDateTime> startColumn_a;
    public TableColumn<Appointment, ZonedDateTime> endColumn_a;
    public TableColumn<Appointment, Integer> customerIdColumn_a;
    public TableColumn<Appointment, Integer> userIdColumn_a;
    public TableColumn<Customer, Integer> customerIdColumn_c;
    public TableColumn<Customer, String> customerNameColumn_c;
    public TableColumn<Customer, String> addressColumn_c;
    public TableColumn<Customer, Integer> postalCodeColumn_c;
    public TableColumn<Customer, Integer> phoneColumn_c;
    public TableColumn<Customer, ZonedDateTime> createDateColumn_c;
    public TableColumn<Customer, String> createdByColumn_c;
    public TableColumn<Customer, Timestamp> lastUpdateColumn_c;
    public TableColumn<Customer, String> lastUpdatedByColumn_c;
    public TableColumn<Customer, Integer> divisionIdColumn_c;
    public Button logOutButton;
    public Button reportsButton;
    public Rectangle appointmentRectangle;
    public Button dismissRectangleButton;
    public Label rectangleLabel;
    public RadioButton allAppointmentsRadio;
    Scene loginWindowScene;
    Stage loginWindowStage = new Stage();
    Scene appointmentFormScene;
    Stage appointmentFormStage = new Stage();
    Scene customerFormScene;
    Stage customerFormStage = new Stage();
    static ToggleGroup radioToggle = new ToggleGroup();
    public static Appointment selectedAppointment = null;
    public static Customer selectedCustomer = null;
    public static ZonedDateTime selectedDate = null;
    public ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    public ObservableList<Appointment> alertList = FXCollections.observableArrayList();
    public ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private boolean monthSort = true;
    private boolean viewAll = true;

    /**
     * This method initializes the Schedule object by performing the following actions:
     * - Translating all the necessary text.
     * - Filling the TableView with the required data.
     *
     * Additionally, this method checks whether there are any appointments scheduled to occur within the next 15 minutes.
     *
     * This initialization process ensures that the Schedule object is fully prepared to display accurate and up-to-date
     * information to the user. By filling the TableView with the required data and checking for upcoming appointments,
     * the user can quickly and easily view their schedule and stay on top of their appointments without missing any
     * important meetings or events.
     */
    @FXML
    private void initialize() throws SQLException {
        // Set up the toggle group
        appointmentByMonthRadioButton.setToggleGroup(radioToggle);
        appointmentByWeekRadioButton.setToggleGroup(radioToggle);
        allAppointmentsRadio.setToggleGroup(radioToggle);
        allAppointmentsRadio.setSelected(true);

        //Set the TableViews
        reloadTables();

        // Translate the window text
        addAppointmentButton.setText(LanguageManager.getTranslation("Add"));
        updateAppointmentButton.setText(LanguageManager.getTranslation("Update"));
        deleteAppointmentButton.setText(LanguageManager.getTranslation("Delete"));
        appointmentByMonthRadioButton.setText(LanguageManager.getTranslation("Appointments_This_Month"));
        appointmentByWeekRadioButton.setText(LanguageManager.getTranslation("Appointments_This_Week"));
        allAppointmentsRadio.setText(LanguageManager.getTranslation("All_Appointments"));
        toggleViewButton.setText(LanguageManager.getTranslation("View_Customers"));
        timeZoneDescriptionLabel.setText(LanguageManager.getTranslation("Time_Zone"));
        timeZoneLabel.setText(ZoneId.systemDefault().toString());
        serverTimeZoneDescriptionLabel.setText(LanguageManager.getTranslation("Server_Time_Zone"));
        serverTimeZoneLabel.setText(ZoneId.of("UTC").toString());
        logOutButton.setText(LanguageManager.getTranslation("Log_Out"));
        appointmentIdCol_a.setText(LanguageManager.getTranslation("Appointment_ID"));
        titleColumn_a.setText(LanguageManager.getTranslation("Title"));
        reportsButton.setText(LanguageManager.getTranslation("Reports"));
        dismissRectangleButton.setText(LanguageManager.getTranslation("Acknowledge"));
        // Translate Appointment cols
        descriptionColumn_a.setText(LanguageManager.getTranslation("Description"));
        locationColumn_a.setText(LanguageManager.getTranslation("Location"));
        contactColumn_a.setText(LanguageManager.getTranslation("Contact"));
        typeColumn_a.setText(LanguageManager.getTranslation("Type"));
        startColumn_a.setText(LanguageManager.getTranslation("Start"));
        endColumn_a.setText(LanguageManager.getTranslation("End"));
        customerIdColumn_a.setText(LanguageManager.getTranslation("Customer_ID"));
        userIdColumn_a.setText(LanguageManager.getTranslation("User_ID"));
        // Translate Customer cols
        customerIdColumn_c.setText(LanguageManager.getTranslation("Customer_ID"));
        customerNameColumn_c.setText(LanguageManager.getTranslation("Customer_Name"));
        addressColumn_c.setText(LanguageManager.getTranslation("Address"));
        postalCodeColumn_c.setText(LanguageManager.getTranslation("Postal_Code"));
        phoneColumn_c.setText(LanguageManager.getTranslation("Phone"));
        createDateColumn_c.setText(LanguageManager.getTranslation("Create_Date"));
        createdByColumn_c.setText(LanguageManager.getTranslation("Created_By"));
        lastUpdateColumn_c.setText(LanguageManager.getTranslation("Last_Update"));
        lastUpdatedByColumn_c.setText(LanguageManager.getTranslation("Last_Updated_By"));
        divisionIdColumn_c.setText(LanguageManager.getTranslation("Division_ID"));
        // Initialize the Appointment TableView
        appointmentIdCol_a.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        titleColumn_a.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionColumn_a.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationColumn_a.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn_a.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startColumn_a.setCellValueFactory(new PropertyValueFactory<>("Start"));
        startColumn_a.setCellFactory(column -> {
            TableCell<Appointment, ZonedDateTime> cell = new TableCell<>()
            {
                DateTimeFormatter colFormat = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy HH:mm");

            @Override
            protected void updateItem(ZonedDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(colFormat.format(item));
                }
            }
        };
            return cell;
        });
        endColumn_a.setCellValueFactory(new PropertyValueFactory<>("end"));
        endColumn_a.setCellFactory(column -> { TableCell<Appointment, ZonedDateTime> cell = new TableCell<>() {
            DateTimeFormatter colFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm z");
            @Override
            protected void updateItem(ZonedDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText("End Time: " + colFormat.format(item));
                }
            }
        };
            return cell;
        }
        );
        customerIdColumn_a.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        userIdColumn_a.setCellValueFactory(new PropertyValueFactory<>("User_ID"));
        contactColumn_a.setCellValueFactory(new PropertyValueFactory<>("Contact_ID"));
        // Initialize the Customer TableView
        customerIdColumn_c.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        customerNameColumn_c.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        addressColumn_c.setCellValueFactory(new PropertyValueFactory<>("Address"));
        postalCodeColumn_c.setCellValueFactory(new PropertyValueFactory<>("Postal_Code"));
        phoneColumn_c.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        createDateColumn_c.setCellValueFactory(new PropertyValueFactory<>("Create_Date"));
        createdByColumn_c.setCellValueFactory(new PropertyValueFactory<>("Created_By"));
        lastUpdateColumn_c.setCellValueFactory(new PropertyValueFactory<>("Last_Update"));
        lastUpdatedByColumn_c.setCellValueFactory(new PropertyValueFactory<>("Last_Updated_By"));
        divisionIdColumn_c.setCellValueFactory(new PropertyValueFactory<>("Division_ID"));

        checkAppointments();
        displayAlertForAppointmentsWithin15();
    }

    /**
     * Closes the Schedule window.
     *
     * @param event the ActionEvent generated when the Schedule window is closed
     */
    public void closeScheduleWindow(ActionEvent event) {
        Stage scheduleWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scheduleWindow.close();
    }

    /**
     * Sets the selected Appointment and nullifies the selected Customer.
     */
    public void setSelectedAppointment() {
        selectedCustomer = null;
        selectedAppointment = (Appointment) selectionView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            selectedDate = selectedAppointment.getStart();
        }
    }

    /**
     * Sets the selected customer from the customer table view.
     */
    public void setSelectedCustomer() {
        // Clear the appointments and date selection
        selectedAppointment = null;
        selectedDate = null;

        // Set the selected customer
        selectedCustomer = (Customer) custTableView.getSelectionModel().getSelectedItem();
    }

    /**
     * Displays either the Appointment Form View or the Customer Form View, depending on the current view state.
     *
     * @throws IOException if there is an error loading the FXML file.
     */
    public void onAddButton() throws IOException, SQLException {
        // Reset the appointment and customer selection
        selectedAppointment = null;
        selectedCustomer = null;

        // Set the FXML file loader and form title based on the current view
        FXMLLoader loader;
        String title;

        if (viewingAppointments) {
            loader = new FXMLLoader(getClass().getResource("/view/Appointments.fxml"));
            title = LanguageManager.getTranslation("Add_Appointment");
        } else {
            loader = new FXMLLoader(getClass().getResource("/view/Customers.fxml"));
            title = LanguageManager.getTranslation("Add_Customer");
        }

        // Load the FXML file and create a new form stage
        Parent root = loader.load();
        Stage formStage = new Stage();

        // Set the form scene, title, and other properties
        formStage.setScene(new Scene(root));
        formStage.setTitle(title);
        formStage.setResizable(false);
        formStage.showAndWait();

        // Refresh the data tables after the form is closed
        reloadTables();
    }

    /**
     * This function is called when the user clicks the "Update" button. It opens the appropriate form for either
     * editing an appointment or a customer, depending on which one is selected.
     *
     * @throws IOException if the FXML file for the selected form cannot be found or loaded
     */
    public void onUpdateButton() throws IOException, SQLException {
        if (selectedAppointment != null || selectedCustomer != null) {
            FXMLLoader formLoader;
            if (viewingAppointments) {
                formLoader = new FXMLLoader(getClass().getResource("/view/Appointments.fxml"));
                Parent root = formLoader.load();
                appointmentFormScene = new Scene(root);
                appointmentFormStage.setScene(appointmentFormScene);
                appointmentFormStage.setTitle(LanguageManager.getTranslation("Update_Appointment"));
                appointmentFormStage.setResizable(false);
                appointmentFormStage.showAndWait();
            } else {
                formLoader = new FXMLLoader(getClass().getResource("/view/Customers.fxml"));
                Parent root = formLoader.load();
                customerFormScene = new Scene(root);
                customerFormStage.setScene(customerFormScene);
                customerFormStage.setTitle(LanguageManager.getTranslation("Update_Customer"));
                customerFormStage.setResizable(false);
                customerFormStage.showAndWait();
            }
            reloadTables();
        } else {
            AlertManager.showAlert("Make a Selection", "Please select an item to update.");
        }

    }

    /**
     * Deletes the selected appointment when the user presses the deleteAppointmentButton.
     * @throws SQLException when an error occurs while deleting the appointment or customer from the database.
     */
    public void onDeleteButton() throws SQLException {
        if (selectedAppointment != null || selectedCustomer != null) {
            if (viewingAppointments && selectedAppointment != null) {
                if (AlertManager.showConfirm("Delete_Appointment")) {
                    AppointmentQueries.removeAppointment(selectedAppointment.getAppointment_ID());
                    AlertManager.showAlert(LanguageManager.getTranslation("Appointment_ID") + ": " +
                                    selectedAppointment.getAppointment_ID() + "\n" +
                                    LanguageManager.getTranslation("Appointment_Type") + selectedAppointment.getType(),
                            LanguageManager.getTranslation("Appointment_Deleted"));
                }
            } else {
                if (selectedCustomer != null) {
                    if (AlertManager.showConfirm("Delete_Customer")) {
                        AppointmentQueries.removeAppointmentsByCustomerId(AppointmentQueries.fetchAllAppointments(), selectedCustomer.getCustomer_ID());
                        CustomerQueries.removeCustomer(selectedCustomer.getCustomer_ID());
                    }
                }
            }
            reloadTables();
            selectedCustomer = null;
            selectedAppointment = null;
        } else {
            AlertManager.showAlert(LanguageManager.getTranslation("Please_Make_A_Selection_To_Delete"),
                    LanguageManager.getTranslation("Delete_Error"));
        }
    }

    /**
     * Toggles between the Appointment TableView and the Customer TableView.
     * Deselects any currently selected customer or appointment.
     * Updates the UI to display the correct TableView and related controls.
     * Reloads the tables with updated data.
     */
    public void onToggleTableView() throws SQLException {
        selectedCustomer = null;
        selectedAppointment = null;
        if (viewingAppointments) {
            toggleViewButton.setText(LanguageManager.getTranslation("View_Appointments"));
            selectionView.setDisable(true);
            selectionView.setVisible(false);
            custTableView.setDisable(false);
            custTableView.setVisible(true);
            appointmentByWeekRadioButton.setDisable(true);
            appointmentByMonthRadioButton.setDisable(true);
            appointmentByWeekRadioButton.setVisible(false);
            appointmentByMonthRadioButton.setVisible(false);
            viewingAppointments = false;
        } else {
            toggleViewButton.setText(LanguageManager.getTranslation("View_Customers"));
            selectionView.setDisable(false);
            selectionView.setVisible(true);
            custTableView.setDisable(true);
            custTableView.setVisible(false);
            appointmentByWeekRadioButton.setDisable(false);
            appointmentByMonthRadioButton.setDisable(false);
            appointmentByWeekRadioButton.setVisible(true);
            appointmentByMonthRadioButton.setVisible(true);
            viewingAppointments = true;
        }
        reloadTables();
    }

    /**
     * Finds and sets the selected appointment from the TableView based on user click.
     */
    public void onAppointmentClicked() {
        setSelectedAppointment();
    }

    /**
     * When a customer row is clicked in a TableView, this method selects the corresponding customer.
     */
    public void onCustomerClick() {
        setSelectedCustomer();
    }

    /**
     * Opens the Logout screen and closes the Schedule.
     *
     * @param actionEvent the action event that triggered the method
     * @throws IOException if an input/output error occurs while loading or opening the screen
     */
    public void onlogoutbutton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent root = loader.load();
        loginWindowScene = new Scene(root);
        loginWindowStage.setScene(loginWindowScene);
        loginWindowStage.setTitle(LanguageManager.getTranslation("Logout"));
        loginWindowStage.setResizable(false);
        loginWindowStage.show();
        closeScheduleWindow(actionEvent);
    }

    /**
     * Reloads TableViews after updating customer and appointment records.
     * Updates the customerList and appointmentList by calling the getAllCustomers and getAllAppointments methods
     * in the CustomerQueries and AppointmentQueries classes respectively.
     * Displays customers in the custTableView and appointments in the selectionView based on the user selection.
     * If the user has selected to sort appointments by month, the appointments are filtered using the monthFilter method and displayed in the selectionView.
     * If the user has selected to sort appointments by week, the appointments are filtered using the weekFilter method and displayed in the selectionView.
     * If the user has selected to view all appointments, all the appointments are displayed in the selectionView.
     * @throws SQLException if there is an error while retrieving customer and appointment records from the database.
     */
    public void reloadTables() throws SQLException {
        customerList = CustomerQueries.getAllCustomers();
        appointmentList = AppointmentQueries.fetchAllAppointments();

        custTableView.setItems(customerList);

        ObservableList<Appointment> displayedAppointments;
        selectionView.setItems(appointmentList);

        if (monthSort) {
            displayedAppointments = FXCollections.observableArrayList(filterAppointmentsByMonth());
            selectionView.setItems(displayedAppointments);
        } else {
            displayedAppointments = FXCollections.observableArrayList(weekFilter());
            selectionView.setItems(displayedAppointments);

        }

        if (viewAll) {
            selectionView.setItems(appointmentList);
        }

    }

    public void displayAlertForAppointmentsWithin15() {
        String alertText = null;
        try {
            alertList = AppointmentQueries.fetchAllAppointmentsWithin15Minutes();
            System.out.println(alertList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            for(Appointment appointment: alertList)
            {
                alertText += appointment.getAppointment_ID() + " ";
                alertText += appointment.getStart();
            }
            if(alertList.size() != 0){
                AlertManager.showAlert("You have appointments within the next 15 minutes:\n " + alertText,
                        "Appointments Pending");
            } else {
                AlertManager.showAlert("No appointments coming up!",
                        "No Appointments!");
            }
        }
    }

    /**
     * LAMBDA EXPRESSION
     * Returns a FilteredList of appointments from the given list, filtered by comparing
     * their month value to the month value of the system default date.
     * This lambda expression makes sense because a lambda is used to create a new filtered list by month.
     *
     * @return A FilteredList of appointments filtered by month value.
     */
    private FilteredList<Appointment> filterAppointmentsByMonth() {
        return new FilteredList<>(appointmentList,
                appointment -> appointment.getStart().getMonthValue() == ZonedDateTime.now(ZoneId.systemDefault()).getMonthValue());
    }
    /**
     * LAMBDA EXPRESSION
     * Returns a FilteredList of Appointments from the appointmentList ObservableList.
     * The predicate is implemented as a lambda expression. Appointments are filtered by comparing their
     * ALIGNED_WEEK_OF_YEAR value to the ALIGNED_WEEK_OF_YEAR value of the system's default date.
     * This lambda expression makes sense because a lambda is used to create a new filtered list by week.
     * @return FilteredList generated by passing in appointmentList and a predicate.
     * */
    private FilteredList<Appointment> weekFilter() {
        return new FilteredList<>(appointmentList,
                p -> p.getStart().get(ChronoField.ALIGNED_WEEK_OF_YEAR) == ZonedDateTime.now(ZoneId.systemDefault()).get(ChronoField.ALIGNED_WEEK_OF_YEAR));
    }

    /**
     * This method toggles between sorting appointments by week and by month.
     * */
    public void toggleBetweenMonthAndWeek() throws SQLException {
        viewAll = false;
        if (monthSort) {
            monthSort = false;
        }
        else {
            monthSort = true;
        }
        reloadTables();
    }

    /**
     * This method calls toggleBetweenMonthAndWeek() when the month radio button is selected.  */
    public void onMonthToggle() throws SQLException {
        toggleBetweenMonthAndWeek();
    }

    /**
     * his method calls toggleBetweenMonthAndWeek() when the week radio is selected.  */
    public void onWeekToggle() throws SQLException {
        toggleBetweenMonthAndWeek();
    }
    /**
     * This method allows the user to change viewAll to true when selected.
     * */
    public void onAllAppointmentsRadio() throws SQLException {
        viewAll = true;
        reloadTables();
    }

    /**
     * This method checks for any appointments that are scheduled within 15 minutes of the current local time.
     * @return true if there are appointments within 15 minutes, false otherwise
     * */
    private boolean checkAppointments() {
        // set initial alert status to false
        boolean alert = false;

        // set time delta to 15 minutes in milliseconds
        int FIFTEEN_MINUTES = 900000;

        // get all appointments
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            appointments = AppointmentQueries.fetchAllAppointments();
        } catch (SQLException sqlE) {
            // handle exception
        }

        // get current local time and date
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        // loop through all appointments and check if they're scheduled in the next 15 minutes
        for (Appointment a : appointments) {
            if (a.getStart().toLocalDate().equals(today)) {
                LocalTime aTime = a.getStart().toLocalTime();
                if (aTime.compareTo(now) <= FIFTEEN_MINUTES && aTime.compareTo(now) > 0) {
                    showAppointmentWarning(true, a.getAppointment_ID(), a.getStart());
                    alert = true;
                }
            }
        }

        // Show warning if there are no appointments within 15 mins
        if (!alert) {
            showAppointmentWarning(false, -1, ZonedDateTime.now());
        }

        return alert;
    }

    /**
     * Displays the Reports scene when the Reports button is clicked.
     * @throws IOException if the Reports.fxml file cannot be loaded.
     */
    public void onReportsButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Reports.fxml"));
        Parent root = loader.load();
        Scene reportScene = new Scene(root);
        Stage reportStage = new Stage();
        reportStage.setScene(reportScene);
        reportStage.setTitle(LanguageManager.getTranslation("Reports"));
        reportStage.setResizable(false);
        reportStage.show();
    }

    /**
     * Hides the Appointment warning banner after the user presses the Acknowledge button.  */
    public void onDismissRectangleButton() {
        hideAppointmentWarning();
    }

    /**
     * Updates the warning banner that is shown to the user upon login.
     * @param isImminent A boolean value that represents whether there is an appointment within 15 minutes.
     * @param appointmentID The unique identifier for the appointment.
     * @param appointmentStart The starting date and time of the appointment.
     */
    private void showAppointmentWarning(boolean isImminent, int appointmentID, ZonedDateTime appointmentStart) {
        if (isImminent) {
            appointmentRectangle.setFill(Paint.valueOf("#d71b4a"));
            String message = "" + LanguageManager.getTranslation("Appointment_ID") + ": " + appointmentID + " " +
                    LanguageManager.getTranslation("Begins_In_15") + " " +
                    LanguageManager.getTranslation("Date") +
                    ": " + TimeManager.getDateFromZonedDateTime(appointmentStart) + ", " +
                    LanguageManager.getTranslation("Time") +
                    ": " + TimeManager.getTimeFromZonedDateTime(appointmentStart);
            rectangleLabel.setText(message);
        }
        else {
            appointmentRectangle.setFill(Paint.valueOf("#1c7fd6"));
            rectangleLabel.setText(LanguageManager.getTranslation("No_Appointments_Scheduled_Within_15_Minutes"));
        }
        rectangleLabel.setVisible(true);
        rectangleLabel.setDisable(false);
        appointmentRectangle.setVisible(true);
        appointmentRectangle.setDisable(false);
        dismissRectangleButton.setVisible(true);
        dismissRectangleButton.setDisable(false);
    }

    /**
     * Hides the Appointment warning banner by setting its visibility to false and disabling it.
     */
    private void hideAppointmentWarning() {
        rectangleLabel.setVisible(false);
        rectangleLabel.setDisable(true);
        appointmentRectangle.setVisible(false);
        appointmentRectangle.setDisable(true);
        dismissRectangleButton.setVisible(false);
        dismissRectangleButton.setDisable(true);
    }
}