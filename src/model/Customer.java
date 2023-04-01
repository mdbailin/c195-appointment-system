package model;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * This class is a container for customer data, and it serves as an access point for retrieving and manipulating that data.
 * It acts as a centralized repository that stores all relevant customer data in a structured manner,
 * such as personal information, order history, and preferences.
 * With this class, developers can efficiently manage and make modifications to customer data in
 * a systematic way, thus improving the overall performance and ease of use of the application.
 */
public class Customer extends DBDateObj {
    // The unique identification number of a customer.
    private int Customer_ID;
    // The Customer_Name of the customer.
    private String Customer_Name;
    // The Address of the customer.
    private String Address;
    // The postal code of the customer.
    private String Postal_Code;
    // The Phone number of the customer.
    private String Phone;
    // The ID of the region or division where the customer is located.
    private int Division_ID;

    /**
     * Constructs a new Customer object with the given information.
     *
     * @param id The unique identification number of the customer.
     * @param name The Customer_Name of the customer.
     * @param address The Address of the customer.
     * @param postalCode The postal code of the customer.
     * @param phone The Phone number of the customer.
     * @param createDate The date and time when the customer was created.
     * @param createdBy The individual ID who created the customer.
     * @param lastUpdate The date and time when the customer was last updated.
     * @param lastUpdatedBy The individual ID who last updated the customer.
     * @param divisionId The ID of the region or division where the customer is located.
     */
    public Customer(int id, String name, String address, String postalCode, String phone,
                    ZonedDateTime createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy,
                    int divisionId)
    {
        super(createDate, createdBy, lastUpdate, lastUpdatedBy);
        this.Customer_ID = id;
        this.Customer_Name = name;
        this.Address = address;
        this.Postal_Code = postalCode;
        this.Phone = phone;
        this.Division_ID = divisionId;
    }

    /**
     * This method retrieves and returns the unique identification number that is associated with a particular customer.
     * The identification number serves as a unique identifier that distinguishes this customer from all other customers
     * in the database or system. When called, this method retrieves the identification number and returns it to
     * the calling program or module for further processing or storage. By using this method,
     * it becomes easy to identify and work with specific customers in the system
     * with a high degree of accuracy and efficiency.
     */
    public int getCustomer_ID() {
        return this.Customer_ID;
    }

    /**
     * This method is used to set the unique identification number of a customer.
     * The unique identification number is an important attribute of a customer and helps to
     * differentiate one customer from another. The identification number could be a
     * combination of numbers, letters or symbols.
     *
     * @param customer_ID This parameter represents the unique identification number of the customer.
     *                   It could be any string value that conforms to the format provided for customer identification.
     */
    public void setCustomer_ID(int customer_ID) {
        this.Customer_ID = customer_ID;
    }

    /**
     * Returns the Customer_Name of the customer.
     *
     * @return The Customer_Name of the customer.
     */
    public String getCustomer_Name() {
        return this.Customer_Name;
    }

    /**
     * Sets the Customer_Name of the customer.
     *
     * @param customer_Name The Customer_Name of the customer.
     */
    public void setCustomer_Name(String customer_Name) {
        this.Customer_Name = customer_Name;
    }

    /**
     * Returns the Address of the customer.
     *
     * @return The Address of the customer.
     */
    public String getAddress() {
        return this.Address;
    }

    /**
     * Sets the Address of the customer.
     *
     * @param address The Address of the customer.
     */
    public void setAddress(String address) {
        this.Address = address;
    }

    /**
     * Returns the postal code of the customer.
     *
     * @return The postal code of the customer.
     */
    public String getPostal_Code() {
        return this.Postal_Code;
    }

    /**
     * Sets the postal code of the customer.
     *
     * @param postal_Code The postal code of the customer.
     */
    public void setPostal_Code(String postal_Code) {
        this.Postal_Code = postal_Code;
    }

    /**
     * Returns the Phone number of the customer.
     *
     * @return The Phone number of the customer.
     */
    public String getPhone() {
        return this.Phone;
    }

    /**
     * Sets the Phone number of the customer.
     *
     * @param phone The Phone number of the customer.
     */
    public void setPhone(String phone) {
        this.Phone = phone;
    }

    /**
     * Returns the region or division ID of the customer.
     *
     * @return The region or division ID of the customer.
     */
    public int getDivision_ID() {
        return this.Division_ID;
    }

    /**
     * Sets the region or division ID of the customer.
     *
     * @param division_ID The region or division ID of the customer.
     */
    public void setDivision_ID(int division_ID) {
        this.Division_ID = division_ID;
    }
}
