package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * The Base Class serves as a fundamental building block that provides essential functionality for
 * tracking crucial information about an object. Specifically, this class maintains record of
 * the creation time, creator of the object, the last time the object was updated, and who last updated the object.
 * This facilitates easy and efficient management of object-specific metadata that can be useful for various
 * purposes such as auditing, analysis or reporting.
 * */
public class DBDateObj {
    /**
     * This documentation describes the attribute that holds the date and time when the object was
     * initially created and saved in the database. The corresponding database column in which
     * this value is stored is named 'Create_Date' and has a data type of DATETIME.
     */
    private ZonedDateTime Create_Date;
    /**
     * This documentation refers to the field "Created_By" present in the database table.
     * The "Created_By" field holds the name of the user who created the object.
     * This field is of type VARCHAR(50).
     * By accessing this field, you can retrieve information about the creator of the object.
     * Please note that this field is read-only, and should not be modified directly.
     */
    private String Created_By;
    /**
     * This method returns the timestamp value indicating the last time at which the object was updated in the database.
     * It should be noted that this particular value is retrieved from the database column named 'Last_Update'
     * which stores timestamp values.
     * This method is useful for applications that require up-to-date data in order to function efficiently.
     */
    private Timestamp Last_Update;
    /**
     * This field represents the individual who last updated the specific object in the database.
     * The information is stored in a database column called 'Last_Updated_By', which is defined as a
     * variable character data type with a maximum length of 50 characters. This data can be used to
     * track the most recent modifications made to the object,
     * as well as the individual responsible for these modifications.
     */
    private String Last_Updated_By;

    /**
     This function creates a new DBDateObj object with the parameters provided.
     The createDate parameter denotes the date when the object was created,
     while the createdBy parameter specifies the identity of the person who created it.
     The lastUpdate parameter represents the time when the object was last updated,
     while the lastUpdatedBy parameter specifies the identity of the person who last updated it.
     This constructor is used to initialize the values of a
     DBDateObj object with meaningful information about its creation and update history.
     * */
    public DBDateObj(ZonedDateTime createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.Create_Date = createDate;
        this.Created_By = createdBy;
        this.Last_Update = lastUpdate;
        this.Last_Updated_By = lastUpdatedBy;
    }
    /**
     * This method returns a ZonedDateTime object that represents the date and time when the object was created.
     * A ZonedDateTime object provides complete date and time information,
     * including the time zone and the offset from UTC.
     *
     * @return ZonedDateTime the creation date of the object.
     * */
    public ZonedDateTime getCreate_Date() {
        return this.Create_Date;
    }
    /**
     * The setCreationDateToCurrentDateAndTime() method updates the creation date of the
     * object to the current date and time. This function is called when a new object is initialized or created,
     * and it provides an accurate representation of when the object was created. By calling this method,
     * you can ensure that the creation date is always up-to-date and reflects the current time.
     *
     * This method can be useful in documenting objects, tracking their age, and determining usage patterns.
     * Additionally, it can serve as a reference point for future updates or modifications to the object.
     */
    public void setCreateDate() {
        this.Create_Date = ZonedDateTime.now();
    }
    /**
     * This method is used to obtain information about the creator of the object.
     * When called, it returns a string indicating the name of the creator who initiated the
     * creation of the object. The returned value is of type String.
     * */
    public String getCreated_By() {
        return this.Created_By;
    }
    /**
     * This method sets the creator of the object to the specified value of the "creator" parameter.
     * The "creator" parameter is of type "Object" and represents the entity that created the object.
     * This can be helpful for tracking the source of the object or for maintaining proper ownership of the
     * object. This method takes one parameter, "creator", which is the creator of the object.
     * */
    public void setCreated_By(String creator) {
        this.Created_By = creator;
    }
    /**
     * This method helps retrieve the time when the object was last updated.
     * It returns a Timestamp object that represents the specific time when the update happened.
     * The returned Timestamp value can be used to track changes made to the object,
     * and to ensure that users are working with the most up-to-date information at all times.
     *
     * @return Timestamp representing the most recent time the object was updated.
     * */
    public Timestamp getLast_Update() {
        return this.Last_Update;
    }
    /**
     * This method updates the timestamp of an object with the current date and time.
     * The timestamp represents the last time the object was modified,
     * and is useful for tracking changes and determining the age of an object.
     * This method can be used to mark an object as recently updated, ensuring that it reflects the latest changes made.
     */
    public void setLastUpdate() {
        this.Last_Update = Timestamp.valueOf(LocalDateTime.now());
    }
    /**
     * This method retrieves and returns the name of the person who last updated the object.
     * The returned value is a String type, containing the name of the last person who made changes
     * to the object represented by this instance of the class.
     *
     * @return String value representing the name of the person who last updated the object.
     * If no updates have been made to the object, then the returned value would be an empty String.
     */
    public String getLast_Updated_By() {
        return this.Last_Updated_By;
    }
    /**
     * This method updates the attribute "last person who updated the object"
     * with the value passed in the "updater" parameter.
     * It takes in a string value that represents the name of the person who
     * performed the last update on the object.
     * This information is useful in tracking and auditing changes to the object.
     * @param updater A string value containing the name of the person who performed the last update on the object.
     */
    public void setLast_Updated_By(String updater) {
        this.Last_Updated_By = updater;
    }
}