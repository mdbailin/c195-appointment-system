package utilities;

import dbQueries.AppointmentQueries;
import dbQueries.CustomerQueries;
import dbQueries.UserQueries;
import exceptions.AppointmentOverlap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import model.User;
import languages.LanguageManager;

import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Validator class is a utility class that provides various methods to validate text input against
 * common MySQL data types. This class can be used for validating input fields in
 * applications where data is stored in MySQL databases.
 *
 * This class includes the following data types:
 * 1. VARCHAR(50) - text input of up to 50 characters
 * 2. VARCHAR(100) - text input of up to 100 characters
 * 3. INT(10) - numeric input of up to 10 digits
 * 4. Email - email input validation
 * 5. Phone - phone number input validation
 * 6. Postal Code - postal code input validation
 *
 * The Validator class provides a convenient way to check if a user's input is
 * valid and appropriate for the data type it is being compared against.
 *
 * Use this class to ensure that your application's data inputs are accurate and
 * remain consistent with the data types it needs to store in the database.
 */
 public abstract class Validator {
    private static String nameRegex = "[a-zA-Z]+\s[a-zA-Z]+";
    private static String addressRegex = "^(\\d+) ?([A-Za-z](?= ))? (.*?) ([^ ]+?) ?((?<= )APT)? ?((?<= )\\d*)?$";
    private static String intTenRegex = "[0-9]{1,10}";
    private static String emailRegex = "\"(^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$){3,50}\"";
    private static String phoneRegex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
    private static String postalRegex = "^\\d{5}-\\d{4}|\\d{5}|[A-Z]\\d[A-Z] \\d[A-Z]\\d$" + // US and Canada https://regexlib.com/REDetails.aspx?regexp_id=23
            "(([A-Z]{1,2}[0-9][0-9A-Z]?)\\ ([0-9][A-Z]{2}))|(GIR\\ 0AA)"; // UK https://regexlib.com/REDetails.aspx?regexp_id=1295


    /**
     * This method checks if a given input String is between 1 and 50 characters in length.
     * If the input is outside of this range, it will trigger a warning message to the user
     * indicating that the input is not valid. To customize the warning message, this method
     * takes the optional parameter "fieldName", which should be set to the name of the
     * input field being validated.
     *
     * @param fieldName (optional) The name of the field to be validated. Used in the error message.
     * @param input     The String value to be validated.
     * @return true if the input meets the length requirements, false if it does not.
     **/
    public static boolean validateVarcharFifty(String fieldName, String input) {
        Pattern pattern = Pattern.compile("^.{1,50}$");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return true;
        } else {
            AlertManager.showAlert(fieldName + " must contain between 1 and 50 characters.", "Invalid entry");
            return false;
        }
    }

    /**
     * This method verifies if a given user input is valid with a length between 1 and 100 characters.
     * It will also provide a warning message if it does not meet the criteria.
     *
     * @param fieldName A String field name used in the warning message to specify the field causing the validation issue.
     * @param input     A String value to be validated.
     * @return A boolean indicating whether the input is valid or not. Returns true if the input is valid
     * and false if the input does not meet the specified validation criteria.
     **/
    public static boolean validateVarcharHundred(String fieldName, String input) {
        Pattern pattern = Pattern.compile("^.{1,100}$");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return true;
        } else {
            AlertManager.showAlert(fieldName + " must contain between 1 and 100 characters.", "Invalid entry");
            return false;
        }
    }

    /**
     * This method validates if the input string represents a name consisting of at least two
     * characters separated by a space.
     * A valid name should contain only alphabetic characters, and the first letter of each name should be capitalized.
     *
     * @param value The String value to be validated.
     * @return true if the input represents a valid name, false if it does not meet the validation criteria.
     */
    public static boolean validateName(String value) {
        Pattern pattern = Pattern.compile(nameRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            return true;
        } else {
            System.out.println(value);
            AlertManager.showAlert("Names must consist of at least two characters separated by a space.", "Invalid entry");
            return false;
        }
    }

    /**
     * This method is responsible for validating addresses that conform to a standard format commonly used in the US,
     * Canada, and the UK. Specifically, it is suitable for validating addresses that follow the format of
     * "123 ABC Street, CityName" for US and Canadian addresses, or "123 ABC Street, BoroughName, CityName"
     * for UK addresses. The input string passed to this method is the address to be validated,
     * and the return value is either true or false, indicating whether the input has successfully
     * passed validation or not, respectively.
     *
     * @param input The String value representing the address to be validated.
     * @return A boolean value, true if the input address is valid and false if it is not.
     */
    public static boolean validateAddress(String input) {
        Pattern pattern = Pattern.compile(addressRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return true;
        } else {
            AlertManager.showAlert("Addresses must be in the format of 123 ABC Street, CityName or\n" + "" +
                    "123 ABC Street, BoroughName, CityName.", "Invalid entry");
            return false;
        }
    }

    /**
     * This method verifies whether or not the given input string contains a valid sequence of
     * digits ranging from 1 to 10. If the input is invalid, the user is warned accordingly by
     * displaying an appropriate error message.
     *
     * @param fieldName A string specifying the name of the field being validated.
     *                  This is used in the error message to provide context for the user.
     * @param input A string representing the user's input that needs to be validated.
     * @return This method returns true if the input is valid, meaning it contains a sequence of
     * digits ranging from 1 to 10 characters in length. If the input is invalid, this method returns false.
     */
    public static boolean validateIntTen(String fieldName, String input) {
        Pattern pattern = Pattern.compile(intTenRegex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return true;
        } else {
            AlertManager.showAlert(fieldName + " must consist of 10 or less numbers.", "Invalid entry");
            return false;
        }
    }

    /**
     * This method verifies whether a given input string represents a valid email address or not.
     * If the input string is a valid email address, the method returns true; otherwise, it returns false.
     * If the input string is not a valid email, this method issues a warning to the user.
     *
     * @param input A string representing the email address to be validated.
     * @return A boolean value indicating whether the input string is a valid email address or not.
     * If the input string is a valid email address, the method returns true; otherwise, it returns false.
     */
    public static boolean validateEmail(String input) {
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return true;
        } else {
            AlertManager.showAlert("Invalid email address entered.", "Invalid entry");
            return false;
        }
    }


    /**
     * This method validates phone numbers which can be stored in a database as a VARCHAR(50) datatype.
     * It checks whether the input follows the appropriate formatting for both US and international phone numbers.
     *
     * @param value A String representing the phone number that needs to be validated.
     *
     * @return boolean A boolean value indicating whether the input follows the proper formatting for a phone number.
     * A return value of true signifies that the input is validated,
     * while a return value of false signifies that it is not.
     */
    public static boolean isPhone(String value) {
        String phoneRegex = "\\d{3}-\\d{3}-\\d{4}";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            return true;
        } else {
            AlertManager.showAlert("Phone must contain a valid phone number, using dashes.", "Invalid entry");
            return false;
        }
    }

    /**
     * This method is responsible for validating postal codes for the United States, the United Kingdom, and Canada.
     * It takes in a String value as a parameter and checks whether it meets the requirements for
     * postal codes in these countries. The method returns true if the input is validated and false otherwise.
     *
     * @param value The String value to be validated.
     * @return A boolean indicating whether or not the input parameter meets postal code requirements for the
     * specified countries. Returns true if the input is validated, and false if it does not meet the criteria.
     */
    public static boolean isPostalCode(String value) {
        String postalRegex = "^(^\\d{5}(-\\d{4})?$|^[A-Z]{1,2}\\d[A-Z\\d]? \\d[A-Z]{2}$|^[A-Z]\\d[A-Z] \\d[A-Z]\\d)$";
        Pattern pattern = Pattern.compile(postalRegex);
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            return true;
        } else {
            AlertManager.showAlert("Postal code must contain a valid postal code for US, Uk or CA.", "Invalid entry");
            return false;
        }
    }

    /**
     * This method validates an input value against a given list of User objects and their corresponding User IDs.
     *
     * @param value The integer value to be subjected to validation.
     * @return Returns a boolean value reflecting whether the input value is valid according to the given
     *         set of User objects and their User IDs. A value of 'true' indicates that the input value is
     *         validated, while 'false' indicates that the input value does not match any of the User IDs in
     *         the provided list and is therefore considered invalid.
     */
    public static boolean isUserId(int value) {
        ObservableList<User> users = FXCollections.observableArrayList();
        List<Integer> userIds = new ArrayList<>();
        try {
            users = UserQueries.getAllUsers();
        } catch (SQLException ignored) {
        }
        users.forEach(user -> userIds.add(user.getUserID()));
        if (userIds.contains(value)) {
            return true;
        }
        AlertManager.showAlert("User_ID entry does not exist in the database.", "Invalid entry");
        return false;
    }

    /**
     * This method is used to validate an input value against a predefined list of customers
     * and their respective customer IDs.
     *
     * @param value - An integer value that needs to be validated against the customer list.
     * @return A boolean value, where 'true' represents that the input value is validated against the list, and 'false'
     * suggests that it is not a valid value.
     *
     * This method helps in ensuring that the input value is within the range of customer IDs present in the customer
     * list, which in turn provides data integrity and security checks within the customer management system.
     */
    public static boolean isCustomerId(int value) {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        List<Integer> customerIds = new ArrayList<>();
        try {
            customers = CustomerQueries.getAllCustomers();
        } catch (SQLException ignored) {
        }
        customers.forEach(customer -> customerIds.add(customer.getCustomer_ID()));
        if (customerIds.contains(value)) {
            return true;
        }
        AlertManager.showAlert("Customer_ID entry does not exist in the database.", "Invalid entry");
        return false;
    }

    /**
     * This method checks whether the input string value is a valid integer or not.
     *
     * @param value The String value to be validated.
     * @return A boolean value of true if the input string value is valid and can be converted to an integer,
     * or false if it is not valid and cannot be converted.
     *
     * This method takes in a string value parameter and proceeds to evaluate it to determine whether
     * it can be converted to a valid integer. If the string can be converted to an integer without any issues,
     * the method will return true, indicating that the input is valid. However, if the string cannot be
     * converted to a valid integer, the method will return false, signaling that the input is invalid.
     */
    public static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            AlertManager.showAlert("User_ID must be an integer.", "Invalid entry");
            return false;
        }
        return true;
    }

    /**
     * This method checks whether a specific time and date are available for an appointment.
     * It takes in three parameters:
     * the start date and time of the appointment, the end date and time of the appointment,
     * and the unique ID of the appointment.
     * The unique ID is essential to avoid comparison with itself.
     * If the requested date and time are available, the method will
     * return true. However, if there is a scheduling conflict,
     * where the time slot is already booked for another appointment,
     * the method will throw an AppointmentOverlap exception.
     *
     * @param dateStart     The start date and time of the appointment.
     * @param dateEnd       The end date and time of the appointment.
     * @param appointmentID The unique ID of the appointment used to avoid comparison with itself.
     *
     * @return Returns true if the requested time and date are available.
     *
     * @throws AppointmentOverlap If there is a scheduling conflict where the
     * requested time and date are already booked for
     *                            another appointment in the system.
     */
    public static boolean isAppointmentAvailable(ZonedDateTime dateStart, ZonedDateTime dateEnd, int appointmentID) throws AppointmentOverlap {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        try {
            allAppointments = AppointmentQueries.getAllAppointmentsExceptForOne(appointmentID);
        } catch (SQLException e) {
        }
        for (Appointment a : allAppointments) {
            if (checkIfTimeIsBetween(TimeManager.toESTTimeZone(dateStart), TimeManager.toESTTimeZone(dateEnd), TimeManager.toESTTimeZone(a.getStart()), TimeManager.toESTTimeZone(a.getEnd()))) {
                AlertManager.showAlert(LanguageManager.getTranslation("Check_Date") + "\n" +
                        LanguageManager.getTranslation("Overlap") + a.getTitle() + ".", "Invalid_Entry");
                throw new AppointmentOverlap(new RuntimeException());
            }
        }
        return true;
    }

    /**
     * Determines whether the start date and time of an appointment occurs before the end time.
     * In order to check this, the method takes in two arguments:
     *
     * @param startTime The start date and time of the appointment
     * @param endTime   The ending date and time of the appointment
     *
     * The method considers the validity of these times and returns a boolean indicating whether the start time precedes the end time.
     * If the given arguments do not represent valid times or the start time is after the end time, the method will return false.
     * If the times are valid and the start time comes before the end time, then the method will return true.
     */
    public static boolean isTimeValid(ZonedDateTime startTime, ZonedDateTime endTime, LocalTime startEST,
                                      LocalTime endEST) {
        if (startTime.isBefore(endTime)) {
            if (startEST.getHour() >= 8 && endEST.getHour() <= 22) {
                return true;
            } else {
                AlertManager.showAlert("Please schedule the appointment between 0800 and 2200 EST.", "Schedule Error");
            }
        } else {
            AlertManager.showAlert("Start and end times are incompatible.", "Schedule Error");
        }
        return false;
    }

    /**
     * This method checks whether a given start/end date is within the bounds of another start/end date.
     * Specifically, it compares whether the given start date and end date fall within another set of start
     * and end dates. The method takes 4 inputs, 2 pairs of start and end date.
     *
     * Comparing between them, this method returns a boolean value of True if the start and end dates fall
     * between another set of start and end dates.

     * @param startDate  The start date of the initial range being compared.
     * @param endDate    The end date of the initial range being compared.
     * @param startDate2 The start date of the target range being compared against.
     * @param endDate2   The end date of the target range being compared against.
     *
     * @return a boolean value of True, if the start and end dates of initial range fall between the target range.
     * Otherwise, it returns False.
     */
    public static boolean checkIfTimeIsBetween(ZonedDateTime startDate, ZonedDateTime endDate, ZonedDateTime startDate2, ZonedDateTime endDate2) {
        if (startDate.isAfter(startDate2) && startDate.isBefore(endDate2)) {
            return true;
        }
        if (endDate.isAfter(startDate2) && endDate.isBefore(endDate2)) {
            return true;
        }
        if (startDate.isEqual(startDate2) || startDate.isEqual(endDate2)) {
            return true;
        }
        if (endDate.equals(endDate2) || endDate.equals(startDate2)) {
            return true;
        }
        return false;
    }
}