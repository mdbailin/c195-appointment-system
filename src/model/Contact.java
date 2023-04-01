// This class holds data related to a Contact
package model;
/**
 * This is a Java class that facilitates the storing and accessing of contact data.
 * It provides suitable methods, data structures, and interfaces for creating and managing contacts.
 * With this class, you can create new contacts, edit existing ones, or delete them as well.
 * The contact data stored can include the person's name, phone number, email address,
 * and any other relevant contact information. Additionally, the class allows for easy access to the stored data,
 * making it simple to retrieve and manipulate contact information whenever needed.
 * This class aims to streamline the process of managing contacts,
 * improving the efficiency and organization of contact information.
 * */

public class Contact {
    /**
     * The Contact_ID is a primary key column within the database, and is represented by an
     * integer datatype with a maximum size of 10 digits. This key is used to uniquely identify a
     * specific Contact record within the table, which can be used for various purposes throughout an
     * application's functionality. Additionally, the Contact_ID is used to link a Contact record to other
     * related records within the database where required. By providing clarity on the nature and
     * functionality of this key, developers can build more powerful and efficient applications
     * that utilize the Contact_ID effectively within their database queries and management.
     * Database column name: Contact_ID
     * Type: INT(10) (PK)
     */
    private int id;
    /**
     * The column represents the name of the individual contact.
     * The data type of this column is VARCHAR(50) which means it can store a maximum of 50 characters.
     *
     * This information can be helpful when working with the Contact table in a database.
     * It ensures that any reference or manipulation of the Contact_Name column is done
     * with a clear understanding of what the column represents and its data type limitations.
     */
    private String name;
    /**
     * This is a documentation comment for the purpose of describing the email address of a Contact.
     * The email address is a data field that is stored in a database column with the name "Email"
     * and a maximum character length of 50 (VARCHAR(50)).
     * This field is used to represent the email address associated with a specific contact in a database.
     * This information is provided to assist other developers who may be reviewing or working with
     * this code to understand the purpose of this particular data field.
     */
    private String email;

    /**
     * This method creates a new instance of the Contact class with the provided parameters. The ID of the Contact
     * should be a unique identifier that can be used to distinguish it from other contacts. The name of the Contact
     * should be a human-readable string that represents the name of the person or entity associated with the contact.
     * The email address of the Contact should be a valid and unique email address that can be
     * used to contact the person or entity associated with the contact.
     *
     * @param id The unique identifier of the Contact
     * @param name The name of the person or entity associated with the Contact
     * @param email The email address of the person or entity associated with the Contact
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    /**

     Returns the ID associated with this object.

     This method retrieves and returns the unique identifier (ID) associated with the current object.

     The ID is an integer value that is used to uniquely identify objects of this type.

     @return an integer representing the ID of the object.

     @throws NullPointerException if this object has not been initialized or constructed.

     @throws IllegalStateException if the ID associated with this object is not valid.
     */
    public int getId() {
        return this.id;
    }
    /**

     Sets the unique identifier for this object.
     @param id the new identifier to assign to this object
     */
    public void setId(int id) {
        this.id = id;
    }
    /**

     Returns the name of this object as a string.
     @return a string representing the name of this object */
    public String getName() {
        return this.name;
    }
    /**

     Sets the name for this object.
     This method sets the name for the object by assigning the provided {@code name} parameter to the object's
     internal name field. The provided {@code name} parameter must not be null or empty, or else an IllegalArgumentException
     will be thrown. If the provided {@code name} parameter is valid, then it will replace the object's current name value.
     @param name the name of the object to be set
     @throws IllegalArgumentException if the provided {@code name} parameter is null or empty */

    public void setName(String name) {
        this.name = name;
    }
    /**

     Returns the email address associated with this object.
     @return the email address string that has been assigned to this object. */
    public String getEmail() {
        return this.email;
    }
    /**

     Sets the email address for this object.

     This method sets the email address for this object to the specified email address.

     The email address must be a valid email address and is checked to ensure that it conforms

     to basic email address formatting rules.

     @param email The email address to set for this object.

     @throws IllegalArgumentException If the specified email address is null or does not conform

     to basic email address formatting rules. */
    public void setEmail(String email) {
        this.email = email;
    }
}
