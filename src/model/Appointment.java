//Package containing Appointment class
package model;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * The Appointment class is used to store and access data about appointments.
 * It serves as a representation of an appointment in a scheduling system.
 * This class provides methods for creating and manipulating appointments,
 * storing their relevant attributes, and retrieving information about them.
 */
public class Appointment extends DBDateObj {
    /**
     * This field represents the unique identifier of an appointment within a system.
     * It is used to differentiate one appointment from another and is of type INT(10).
     * This field serves as the primary key in the database table and is critical
     * for efficient data retrieval and management.
     */
    private int Appointment_ID;

    /**
     * This Javadoc provides a description of the Appointment Title field.
     * The Appointment Title field is expected to be a string that has a maximum length of 50 characters.
     * This field is used to specify the name or title of the appointment that is being scheduled.
     * It is important to provide a clear and concise title so that others
     * can quickly identify the purpose of the appointment. The title should reflect the subject of the meeting
     * or appointment, and should be descriptive enough to provide an accurate representation of
     * what the appointment entails. Please note that this field is stored as a VARCHAR in the database.
     */
    private String Title;

    /**
     * The Appointment description is a field in a database table that is defined using the VARCHAR(50) data type.
     * The VARCHAR(50) data type constrains the maximum length of the description to 50 characters.
     */
    private String description;

    /**
    Location is a string value represented by VARCHAR(50) data type.
     In other words, the location field is capable of storing characters up to a maximum length of 50 characters.
     This information can be useful for developers who are working on creating or modifying
     software using this codebase as it provides important details about the data type and size of the location field.
     */
    private String location;

    /**
     * String type is stored as a VARCHAR(50) in a
     * database or other data storage system. This field represents the nature or category of the
     * appointment being scheduled or viewed. Appointment type could include a variety of information
     * such as a medical appointment, business meeting, or personal event.
     */
    private String type;

    /**
     * The data type being used to represent it is DATETIME, which refers to a composite data type that includes
     * both date and time elements. In other words, this appointment start time is defined with both the specific
     * date and precise time of day on which it is scheduled to begin.
     */
    private ZonedDateTime start;

    /**
     * The data type of this field is DATETIME,
     * which represents a combination of date and time. This field specifies the exact time
     * at which the appointment will conclude.
     */
    private ZonedDateTime end;

    /**
     This Javadoc provides information about the "customer ID" attribute that is associated with a
     particular appointment. This attribute is represented as an integer data type with 10 digits and
     functions as a foreign key within the system. In other words, the customer ID refers to a unique
     identifier that is linked with a corresponding customer record in the database. This allows the
     system to efficiently access and retrieve all relevant customer information for any given appointment.
     */
    private int Customer_ID;

    /**
     * This field identifies the user associated with a specific appointment.
     * The value of this field is an integer with a maximum length of 10 digits, and it is used as a foreign key
     * to establish a relationship between the appointment and the user account. Essentially, this field helps to
     * link a particular appointment to a specific user in the database who is scheduled to participate in the
     * appointment session.
     */
    private int User_ID;

    /**
     * This field specifies the unique identifier assigned to the individual who has scheduled the appointment.
     * The type of this field is 10-digit integer and it represents a foreign key to another entity.
     * The foreign key is used to establish a relationship with the entity that contains more detailed
     * information about the contact. For instance, the contact entity may contain additional fields such as
     * the contact's name, phone number, and email address, which can be accessed by using the
     * foreign key to retrieve the related information.
     */
    private int Contact_ID;

    /**
     * Constructs a new Appointment object with the provided details.
     *
     * @param id - The unique ID of the appointment. This is a required parameter and must be unique in the system.
     * @param title - A short title or subject describing the appointment. This is a required parameter.
     * @param description - A brief description of the appointment. This parameter is optional.
     * @param location - The location where the appointment will take place. This parameter is optional.
     * @param type - The type of the appointment, e.g. Meeting, Phone Call, etc. This parameter is optional.
     * @param start - The starting date and time of the appointment. This is a required parameter and must be
     *              provided in the format "yyyy-MM-dd HH:mm".
     * @param end - The ending date and time of the appointment. This is a required parameter and must be
     *            provided in the format "yyyy-MM-dd HH:mm".
     * @param customerId - The unique ID of the customer associated with the appointment. This parameter is optional.
     * @param userId - The unique ID of the user who scheduled the appointment. This is a required parameter.
     * @param contactId - The unique ID of the contact associated with the appointment. This parameter is optional.
     * @param createDate - The date and time when the appointment was created. This parameter is automatically set to
     *                   the current date and time when the appointment is created.
     * @param createdBy - The name of the individual who created the appointment. This parameter is automatically set
     *                  to the name of the user who created the appointment.
     * @param lastUpdate - The date and time when the appointment was last updated. This parameter is automatically set
     *                   to the current date and time when the appointment is updated.
     * @param lastUpdatedBy - The name of the individual who last updated the appointment.
     *                      This parameter is automatically
     *                      set to the name of the user who last updated the appointment.
     *
     */
    public Appointment(int id, String title, String description, String location, String type,
                       ZonedDateTime start, ZonedDateTime end, int customerId, int userId, int contactId,
                       ZonedDateTime createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy)
    {
        super(createDate, createdBy, lastUpdate, lastUpdatedBy);
        this.Appointment_ID = id;
        this.Title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.Customer_ID = customerId;
        this.User_ID = userId;
        this.Contact_ID = contactId;
    }

    /**
     * This is the default constructor for the Appointment class.
     * It creates a new instance of the Appointment class without any parameters or arguments.
     * This constructor is used when no other constructor has been explicitly called or when
     * there is no need to set any specific values or parameters during the creation of the
     * Appointment object. It initializes all the properties of the Appointment class to their default values,
     * such as null or zero, depending on the data type of each property.
     * This constructor provides a basic starting point for the Appointment class,
     * allowing it to be instantiated in a default state that can be customized later
     * through other methods or constructors..
     * */
    public Appointment() {
        this(-1, null, null, null, "null", null,
                null, -1, -1, -1, null, null,
                null, null);
    }

    /**
     * This method retrieves the appointment ID of the current appointment.
     *
     * @return The appointment ID of the current appointment as an integer.
     */
    public int getAppointment_ID() {
        // Return the appointment ID of the current appointment
        return this.Appointment_ID;
    }

    /**

     Sets the ID of the appointment to the specified value.
     @param appointment_ID An integer representing the ID of the appointment.
     Must be greater than or equal to zero.
     @throws IllegalArgumentException If the appointment ID is negative. */
    public void setAppointment_ID(int appointment_ID) throws IllegalArgumentException {
        if (appointment_ID < 0) {
            throw new IllegalArgumentException("Appointment ID cannot be negative.");
        }
        this.Appointment_ID = appointment_ID;
    }
    /**
     * Returns the title of an object.
     *
     * This method retrieves and returns the title of the calling object as a String.
     * The title represents the name or description of the object and is stored in the instance variable 'Title'.
     *
     * @return a String representing the title of the object.
     */
    public String getTitle() {
        return this.Title;
    }
    /**

     Sets the title of the current object to the specified title.
     @param title the new title for the object
     */
    public void setTitle(String title) {
        this.Title = title;
    }
    /**

     Returns the description of the object as a string.
     @return a string representation of the description of the object.
     */
    public String getDescription() {
        return this.description;
    }

    /**

     Sets the description of this object. The description provides a summary or explanation of the object's purpose or behavior.
     @param description the new description to set for this object
     @throws IllegalArgumentException if the description parameter is null */
    public void setDescription(String description) {
        this.description = description;
    }
    /**

     Returns the location associated with this object.
     @return the location as a String */
    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    /**

     Sets the location of a particular object.
     */
    public String getType() {
        return this.type;
    }
    /**

     Sets the type of the object.
     @param type a string representing the type of the object */
    public void setType(String type) {
        this.type = type;
    }
    /**

     Returns the starting date and time of an event as a ZonedDateTime object.
     @return the starting date and time of the event in the form of a ZonedDateTime object
     @see java.time.ZonedDateTime */
    public ZonedDateTime getStart() {
        return this.start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }
    /**
     * Sets the start date and time for an event in ZonedDateTime format.
     */
    public ZonedDateTime getEnd() {
        return this.end;
    }
    /**

     Sets the end date and time of an event to the specified ZonedDateTime object.
     @param end the ZonedDateTime object representing the end date and time of the event */
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }
    /**

     Returns the unique ID of the customer associated with this instance.
     @return An integer representing the customer ID. */
    public int getCustomer_ID() {
        return this.Customer_ID;
    }
    /**

     Sets the ID of the customer associated with this object.
     @param customer_ID the unique identifier for the customer */
    public void setCustomer_ID(int customer_ID) {
        this.Customer_ID = customer_ID;
    }
    /**

     Returns the user ID for this particular user.
     @return an integer value representing the user ID */
    public int getUser_ID() {
        return this.User_ID;
    }
    /**

     Sets the user ID of the current user.
     @param user_ID An integer representing the user ID to be set. */
    public void setUser_ID(int user_ID) {
        this.User_ID = user_ID;
    }
    /**

     Returns the contact ID of this object.
     @return an integer representing the Contact_ID of the object */
    public int getContact_ID() {
        return this.Contact_ID;
    }
    /**

     Sets the contact ID for this object to the provided value.
     @param contact_ID the new contact ID for this object */
    public void setContact_ID(int contact_ID) {
        this.Contact_ID = contact_ID;
    }
}