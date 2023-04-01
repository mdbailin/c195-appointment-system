/* Defines a class to contain Country data, inherits from DBDateObj. */

package model;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * This class serves as a data storage and retrieval mechanism for storing and retrieving country-related data.
 * It contains essential country attributes such as the country ID and name, as well as metadata related to
 * creation and updates of these attributes. By utilizing this class, users can ensure a comprehensive and
 * organized approach to managing country data in their applications.
 */
public class Country extends DBDateObj {
    /**
     The identifier is stored in the database as an INT(10) data type with a primary key (PK) constraint.
     This identifier allows for efficient and accurate referencing of the country in question.
     */
    private int id;

    /**
     It specifies that the name is stored as a VARCHAR(50) data type in a database.
     The purpose of this detail is to assist developers in understanding the data type
     requirements and limitations associated with the Country name field.
     */
    private String name;

    /**
     * Constructor for Country.
     * @param id The unique identifier of the Country.
     * @param name The name of the Country.
     * @param createDate The date the Country was created.
     * @param createdBy The user who created the Country.
     * @param lastUpdate The date the Country was last updated.
     * @param lastUpdatedBy The user who last updated the Country.
     */
    public Country(int id, String name, ZonedDateTime createDate, String createdBy, Timestamp lastUpdate,
                   String lastUpdatedBy)
    {
        super(createDate, createdBy, lastUpdate, lastUpdatedBy);
        this.id = id;
        this.name = name;
    }

    /**
     * This method retrieves the unique identifier associated with a Country object.
     * It returns a value that serves as a globally unique identifier for that Country.
     *
     * @return A string representing the unique identifier of the Country object.
     *         This identifier can be used to distinguish this Country from other Countries
     *         in a system or database.
     */
    public int getId() {
        return this.id;
    }

    /**
     * This method sets the unique identifier of a country. The unique identifier provided as input parameter is used to uniquely identify a country.
     *
     * @param id The unique identifier of the Country.
     *            This parameter is mandatory and should be a non-null integer value representing the unique identifier of the country.
     *
     * @return This method does not return any value.
     *
     * @throws  IllegalArgumentException If the input parameter is null or negative, then an IllegalArgumentException is thrown.
     *           In such cases, the caller must handle this exception to ensure that the system gracefully handles this scenario.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * This method fetches the name of a particular Country object.
     * When called, it returns the string value representing the name of the Country.
     *
     * @return The name of the Country as a String object.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the Country.
     *
     * This method allows the user to set the name of a given Country object to a specified value.
     *
     * @param name The name of the Country to set. This value should be a String denoting the name of the country.
     */
    public void setName(String name) {
        this.name = name;
    }
}
