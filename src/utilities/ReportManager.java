package utilities;

import dbQueries.AppointmentQueries;
import dbQueries.ContactQueries;
import dbQueries.CustomerQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.Month;

/**
 * The ReportManager class acts as a central hub for generating and writing detailed reports to a txt file.
 * This class encapsulates the complex functionality required for report generation,
 * making it easy to integrate reports into your project.
 *
 * The ReportManager provides an intuitive API for generating detailed reports that include
 * important information, summaries, and insights about specific geographic or demographic data.
 * With the ReportManager, you can encapsulate the complexities of report generation and focus on
 * delivering timely and accurate reports for your clients or stakeholders.
 */
public abstract class ReportManager {
    /**
     * This method generates a report that provides detailed information about the number of customer appointments,
     * which are categorized by type and month. The output format of this report is a formatted string.
     * The string contains a summary of the appointments by type, along with their corresponding month.
     * Detailed information about each appointment, such as its duration or start time, is not included in the report.
     *
     * @return A formatted string, which includes the total number of
     * customer appointments for each type, grouped by month.
     */
    public static String countAppointmentsByTypeAndMonth() {
        int typeCount = 1;
        StringBuilder appointmentReport = new StringBuilder("");
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            appointments = AppointmentQueries.getOrderedAppointments();
        } catch (SQLException e) {
            System.err.println("Error in retrieving the appointments.");
        }
        for (int month = 1; month <= 12; month++) {
            appointmentReport.append("|----").append(Month.of(month).name()).append("----|");
            for (int i = 0; i < appointments.size(); i++) {
                Appointment currentAppointment = appointments.get(i);
                if (currentAppointment.getStart().getMonthValue() == month) {
                    int nextAppointmentIndex = i + 1;
                    if (nextAppointmentIndex >= appointments.size()) {
                        nextAppointmentIndex = i;
                    }
                    Appointment nextAppointment = appointments.get(nextAppointmentIndex);
                    if (currentAppointment.getType().equals(nextAppointment.getType()) &&
                            nextAppointment.getStart().getMonthValue() == month) {
                        typeCount++;
                    } else {
                        appointmentReport.append("\n").append(currentAppointment.getType()).append(" appointments: ")
                                .append(typeCount);
                        typeCount = 1;
                    }
                }
            }
            appointmentReport.append("\n>\n");
        }
        return appointmentReport.toString();
    }

    /**
     * Retrieves all the appointments scheduled for each contact, and returns them in a formatted string
     * including Appointment ID, Title, Type, Description, Start and End date/time and customer ID.
     *
     * @return A string representing the schedules for each contact.
     * @throws SQLException if an error occurs while retrieving data from the database.
     */
    public static String contactSchedule() throws SQLException {
        int appointmentIndex = 1;
        StringBuilder scheduleString = new StringBuilder("");
        ObservableList<Appointment> appointments;
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        try {
            contacts = ContactQueries.getAllContacts();
        } catch (SQLException exception) { /* do nothing */ }
        for (Contact contact : contacts) {
            appointmentIndex = 1;
            appointments = AppointmentQueries.getAppointmentsByContactId(contact.getId());
            scheduleString.append("Contact: ").append(contact.getName()).append("\n");
            for (Appointment appointment : appointments) {
                scheduleString.append("|Appointment ").append(appointmentIndex).append("|\nAppointment_ID: ")
                        .append(appointment.getAppointment_ID()).append("\nTitle: ").append(appointment.getTitle())
                        .append("\nType: ").append(appointment.getType()).append("\nDescription: ")
                        .append(appointment.getDescription()).append("\nStart: ")
                        .append(TimeManager.toESTTimeZone(appointment.getStart())).append("\nEnd: ")
                        .append(TimeManager.toESTTimeZone(appointment.getEnd())).append("\nCustomer ID: ")
                        .append(appointment.getCustomer_ID()).append("\n\n");
                appointmentIndex += 1;
            }
        }
        return scheduleString.toString();
    }

    /**
     * This method generates a report of customers and their contact information,
     * sorted by their division IDs.
     *
     * @return A string constructed with customer data
     * The filtered lists are created using lambdas to set predicates.
     */
    public static String customersByDivision() {
        StringBuilder report = new StringBuilder("");
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            customerList = CustomerQueries.getAllCustomers();
        } catch (SQLException ignored) {
        }
        // FilteredList for US customers
        FilteredList<Customer> customersUS = new FilteredList<>(customerList);
        customersUS.setPredicate(c -> {
            int divisionId = c.getDivision_ID();
            return divisionId >= 1 && divisionId <= 54;
        });
        // FilteredList for Canadian customers
        FilteredList<Customer> customersCA = new FilteredList<>(customerList);
        customersCA.setPredicate(c -> {
            int divisionId = c.getDivision_ID();
            return divisionId >= 60 && divisionId <= 72;
        });
        // FilteredList for UK customers
        FilteredList<Customer> customersUK = new FilteredList<>(customerList);
        customersUK.setPredicate(c -> {
            int divisionId = c.getDivision_ID();
            return divisionId >= 101 && divisionId <= 104;
        });
        report.append("|-----US Customers-----|\n");
        for (Customer c : customersUS) {
            report.append("Name: ").append(c.getCustomer_Name()).append("\nAddress: ")
                    .append(c.getAddress()).append("\nPhone: ").append(c.getPhone()).append("\n~~~~~~~~~~~~~~~~\n");
        }
        report.append("|-----CA Customers-----|\n");
        for (Customer c : customersCA) {
            report.append("Name: ").append(c.getCustomer_Name()).append("\nAddress: ")
                    .append(c.getAddress()).append("\nPhone: ").append(c.getPhone()).append("\n~~~~~~~~~~~~~~~~\n");
        }
        report.append("|-----UK Customers-----|\n");
        for (Customer c : customersUK) {
            report.append("Name: ").append(c.getCustomer_Name()).append("\nAddress: ")
                    .append(c.getAddress()).append("\nPhone: ").append(c.getPhone()).append("\n~~~~~~~~~~~~~~~~\n");
        }
        return report.toString();
    }

    /**
     * This method performs the task of writing or appending a report to a specified file. It takes two parameters:
     * the name of the file and the content of the report. If the file already exists,
     * the report will be appended to it, otherwise a new file with the given
     * name will be created and the report will be written to it.
     *
     * @param reportName A string representing the name of the file where the report will be written or appended.
     *                   This parameter
     *                   cannot be null or empty.
     * @param report     A string containing the report text that will be written or appended to the specified file.
     *                   This parameter
     *                   cannot be null or empty.
     *
     * @throws IllegalArgumentException If the file name or report content is null or empty.
     */
    public static void writeReportToFile (String reportName, String report) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        PrintWriter printWriter = null;

        try {
            fileWriter = new FileWriter(reportName, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);
            printWriter.println(report);
            printWriter.flush();
        } catch (IOException e) {
            System.out.println("An error occurred while writing report to file: " + e.getMessage());
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                System.out.println("An error occurred while closing the file: " + e.getMessage());
            }
        }
    }
}