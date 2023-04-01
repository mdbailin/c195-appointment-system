package controller;

import dbQueries.CountryQueries;
import dbQueries.CustomerQueries;
import dbQueries.FirstLevelDivisionQueries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;
import languages.LanguageManager;
import utilities.TimeManager;
import utilities.Validator;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;

/**
 * The Customer class is a fundamental component of our customer management system,
 * which assists users with creating and updating customer information. By utilizing this class,
 * users can smoothly interact with customer data that is stored within our application's database.
 * The primary responsibilities of this class include receiving customer data inputs from users,
 * validating the input data according to system requirements, and organizing customer information
 * in a clear and structured manner. Overall, the CustomerForm class contains crucial functionalities
 * that contribute significantly to the efficiency and accuracy of our customer management system's operation.
 */
public class Customers {

    public TextField customerIdField;
    public TextField nameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneField;
    public ComboBox<String> countryComboBox;
    public ComboBox<String> divisionComboBox;
    public Button saveButton;
    public Button cancelButton;
    public Label customerIdLabel;
    public Label nameLabel;
    public Label addressLabel;
    public Label postalCodeLabel;
    public Label phoneLabel;
    public Label countryLabel;
    public Label stateLabel;
    public HashMap<String, Integer> divisionIdHash;
    public HashMap<Integer, String> divisionNameHash;

    /**
     * This method initializes the CustomerForm by setting the initial values for the various fields used in the form.
     * It also populates the ComboBoxes with the appropriate data required for the form.
     *
     * @throws SQLException if there is an error accessing the database during the initialization process.
     */
    @FXML
    private void initialize() throws SQLException {
        divisionIdHash = FirstLevelDivisionQueries.retrieveAllDivisionIDs();
        divisionNameHash = FirstLevelDivisionQueries.buildDivisionMap();
        countryComboBox.setItems(CountryQueries.getAllCountries());
        countryComboBox.getSelectionModel().selectFirst();
        divisionComboBox.setItems(FirstLevelDivisionQueries.getUSDivisions());
        divisionComboBox.getSelectionModel().selectFirst();
        nameLabel.setText(LanguageManager.getTranslation("Customer_Name"));
        addressLabel.setText(LanguageManager.getTranslation("Address"));
        postalCodeLabel.setText(LanguageManager.getTranslation("Postal_Code"));
        phoneLabel.setText(LanguageManager.getTranslation("Phone_Number"));
        countryLabel.setText(LanguageManager.getTranslation("Country"));
        stateLabel.setText(LanguageManager.getTranslation("State"));
        saveButton.setText(LanguageManager.getTranslation("Save"));
        cancelButton.setText(LanguageManager.getTranslation("Cancel"));

        // if there is a selected Customer, populate the fields with its data
        if (Scheduler.selectedCustomer != null) {
            customerIdField.setText(String.valueOf(Scheduler.selectedCustomer.getCustomer_ID()));
            nameField.setText(Scheduler.selectedCustomer.getCustomer_Name());
            addressField.setText(Scheduler.selectedCustomer.getAddress());
            postalCodeField.setText(Scheduler.selectedCustomer.getPostal_Code());
            phoneField.setText(Scheduler.selectedCustomer.getPhone());
            // get country & division info
            setComboBoxFromDivisionId(Scheduler.selectedCustomer.getDivision_ID());
        } else {
            // if there is no selected Customer, populate the Customer ID field with the next available ID
            try {
                customerIdField.setText(String.valueOf(CustomerQueries.getNextCustomerId()));
            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        }
    }

    /**
     * Saves the customer object to the database.
     *
     * @param actionEvent The ActionEvent for the button press.
     * @throws SQLException If there is an error executing the SQL query.
     */
    public void saveCustomerToDatabase(ActionEvent actionEvent) throws SQLException {
        if (validateCustomerForms()) {
            if (Scheduler.selectedCustomer != null) {
                // Update the existing customer information.
                CustomerQueries.updateCustomerInfo(createCustomer());
            } else { // Add the new customer to the database.
                addCustomerToDatabase();
            }
            // Clear selected appointment and customer data.
            Scheduler.selectedAppointment = null;
            Scheduler.selectedCustomer = null;
            // Close the current window.
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Closes the customer form without saving any changes to the database.
     *
     * @param event The action event triggered by cancel button.
     */
    public void onCancelButton(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scheduler.selectedCustomer = null;
        currentStage.close();
    }

    /**
     * Tries to add a new Customer instance to the database.
     *
     * @throws SQLException if there is any error in the SQL query.
     */
    public void addCustomerToDatabase() throws SQLException {
        try {
            CustomerQueries.addCustomer(createCustomer());
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Creates a Customer object from the input fields.
     *
     * @return A Customer object with values from the input fields.
     **/
    public Customer createCustomer() {
        int customerId = Integer.parseInt(customerIdField.getText());
        String name = nameField.getText();
        String address = addressField.getText();
        String postalCode = postalCodeField.getText();
        String phone = phoneField.getText();
        ZonedDateTime createDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"));
        String createdBy = "admin";
        Timestamp lastUpdate = TimeManager.getTimestampForCurrentUTCTime();
        String lastUpdatedBy = "admin";
        int divisionId = getDivisionIdFromName(divisionComboBox.getValue());
        return new Customer(
                customerId,
                name,
                address,
                postalCode,
                phone,
                createDate,
                createdBy,
                lastUpdate,
                lastUpdatedBy,
                divisionId
        );
    }

    /**
     * Validates the form fields for a Customer.
     * This method validates the name field, address field, postal code field, and phone field with the help of a Validator
     * utility class. If any field fails validation, the method returns false. Otherwise, it returns true.
     *
     * @return true if all form fields pass validation, false if any field fails validation.
     */
    public boolean validateCustomerForms() {
        boolean isNameValid = Validator.validateName(nameField.getText());
        boolean isAddressValid = Validator.validateAddress(addressField.getText());
        boolean isPostalCodeValid = Validator.isPostalCode(postalCodeField.getText());
        boolean isPhoneValid = Validator.isPhone(phoneField.getText());
        boolean[] inputs = {isNameValid, isAddressValid, isPostalCodeValid, isPhoneValid};
        for (boolean input : inputs) {
            if (!input) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates the First Level Division ComboBox based on the selected country.
     *
     */
    public void onCountryComboBox() {
        if (countryComboBox.getSelectionModel().getSelectedIndex() == 0) {
            divisionComboBox.setItems(FirstLevelDivisionQueries.getUSDivisions());
        }
        else if (countryComboBox.getSelectionModel().getSelectedIndex() == 1) {
            divisionComboBox.setItems(FirstLevelDivisionQueries.retrieveUKDivisions());
        }
        else if (countryComboBox.getSelectionModel().getSelectedIndex() == 2) {
            divisionComboBox.setItems(FirstLevelDivisionQueries.getCADivisions());
        }
        divisionComboBox.getSelectionModel().selectFirst();
    }


    /**
     * Sets the contents of a ComboBox based on a given Division ID.
     * This method selects the appropriate country and populates the ComboBox with
     * the appropriate First Level Divisions, based on the given Division ID.
     *
     * @param divisionId The Division ID of the selected First Level Division.
     */
    public void setComboBoxFromDivisionId(int divisionId) {
        if (divisionId >= 1 && divisionId <= 54) {
            countryComboBox.getSelectionModel().select("U.S");
            divisionComboBox.setItems(FirstLevelDivisionQueries.getUSDivisions());
            divisionComboBox.getSelectionModel().select(getDivisionNameFromId(divisionId));
        } else if (divisionId >= 60 && divisionId <= 72) {
            countryComboBox.getSelectionModel().select("Canada");
            divisionComboBox.setItems(FirstLevelDivisionQueries.getCADivisions());
            divisionComboBox.getSelectionModel().select(getDivisionNameFromId(divisionId));
        } else if (divisionId >= 101 && divisionId <= 104) {
            countryComboBox.getSelectionModel().select("UK");
            divisionComboBox.setItems(FirstLevelDivisionQueries.retrieveUKDivisions());
            divisionComboBox.getSelectionModel().select(getDivisionNameFromId(divisionId));
        }
    }

    /**
     * Retrieves the ID associated with a given division name.
     *
     * @param divisionName The name of the division to search for in the hash table.
     * @return The integer value of the Division ID stored at the given division name.
     */
    public int getDivisionIdFromName(String divisionName) {
        return divisionIdHash.get(divisionName);
    }

    /**
     * Retrieves the name of the division associated with the given ID.
     *
     * @param divisionId An integer representing the ID of the division.
     * @return A string representing the name of the associated division, or null if the ID is not found.
     */
    public String getDivisionNameFromId(int divisionId) {
        return divisionNameHash.get(divisionId);
    }
}
