// This Java model is responsible for handling User data, inheriting TrackedDBObject class
package model;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * The User class serves as a data structure that holds substantial information about a particular User.
 * This class not only contains the User data but also provides access to it for subsequent retrieval and modification.
 * Every data field in this class is paired with its corresponding data type as it exists in the associated database.
 * By creating objects of this User class and populating them with necessary User details,
 * the User data can be efficiently managed and manipulated throughout an application.
 * */
public class User extends DBDateObj {
    /**
     * This javadoc describes the user ID attribute of a database table,
     * which is stored as an INT(10) type and serves as the primary key (PK) of the table.
     * The user ID attribute is used to uniquely identify each user record in the table and is
     * essential for performing various operations, such as retrieving or updating user information.
     * By documenting this attribute, developers can gain a better understanding of the data structure
     * and ensure that they are utilizing this attribute correctly within their code.
     * */
    private int userID;
    /**
     * This Javadoc describes the "User_Name" field, which is a column in the database table that represents a
     * user account. The data type of this field is VARCHAR, which means it can store text strings up to
     * 50 characters in length. Additionally, this field is defined as UNIQUE, meaning that each value
     * entered into it must be unique across all entries in the table. This field is essential in
     * identifying unique users in the system, and is typically used as a key attribute when
     * executing queries on this table.
     * */
    private String userName;
    /**
     * This Javadoc documents the `password` property of a User object.
     * The `password` property contains a user's password as plain text.
     *
     */
    private String password;

    /**
     This is a constructor method for creating instances of the User class.
     It requires several parameters to fully describe the user being created.
     The first parameter is the 'userID' which represents the unique ID for this User.
     It is important for identifying this User and linking them to any associated data.
     The second parameter is 'userName' which represents the chosen username for this User.
     This value is used to authenticate the User when they log in and access their account.
     The third parameter is 'password', which represents the chosen password for this User.
     This value is also used to authenticate the User when they log in and access their account.
     It is important that passwords be kept secure and private.
     The fourth parameter is 'createDate',
     which represents the date that this User account was created. This value can be used for record-keeping purposes.
     The fifth parameter is 'createdBy', which represents the person or process responsible
     for creating this User account. The sixth parameter is 'lastUpdate',
     which represents the date that the User account was last updated. The seventh parameter is 'lastUpdatedBy',
     which represents the person or process responsible for updating this User account.
     Overall, this constructor creates an instance of the User class with several important
     parameters to fully describe the User and their account information.
     * */
    public User(int userID, String userName, String password, ZonedDateTime createDate, String createdBy,
                Timestamp lastUpdate, String lastUpdatedBy)
    {
        super(createDate, createdBy, lastUpdate, lastUpdatedBy);
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }

    /**
     This method is a getter that retrieves and returns the user ID associated with an object instance.
     @return The user ID which is a unique identifier assigned to a user within a system or application */
    public int getUserID() {
        return this.userID;
    }

    /**
     This method is a setter for a user ID property. It takes in a parameter called userID,
     which represents the ID of a user, and assigns it to the user ID property.
     The purpose of this method is to update the user ID property with the provided value.
     It is used when creating or updating a user object.
     * */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     This is a JavaDoc comment that defines a method called "getUserName".
     This method serves as a getter for the username. It allows access to the value of the username property,
     which is stored within the corresponding object. The comment is clear and concise, providing
     a brief explanation of the purpose of the getter method.
     * */
    public String getUserName() {
        return this.userName;
    }

    /**
     * This method sets the username for a particular action or functionality. The parameter "userName" is
     * passed as an argument to this method, which the method uses to update the corresponding username value.
     * This method is designed to be a setter, meaning it modifies the internal state of the object.
     * It takes in a single parameter, "userName", which is of type String,
     * and represents the new username that is being set.
     * */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method is a getter for retrieving the password associated with a user account.
     * It returns the password as a string value.
     * The password is an encrypted code that allows a user access to their account and verifies their identity.
     * */
    public String getPassword() {
        return this.password;
    }

    /**
     * This method sets the user's password as specified by the parameter.
     *
     * @param password The password to set for the user's account.
     *                 This parameter must be a String object.
     *                 The method will assign this string as the password for the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
