/**
 * This is a base class that includes functionality for tracking the creation date, who created the object,
 * the last time the object was updated, and who last updated the object.
 * */
package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class TrackedDBObject {
    /**
     * The date the object is created in the database.
     * The database column is 'Create_Date DATETIME'.
     */
    private ZonedDateTime createDate;
    /**
     * The creator of the object.
     * The database column is 'Created_By VARCHAR(50)'.
     */
    private String createdBy;
    /**
     * The time the object was last updated in the database.
     * The database column is 'Last_Update TIMESTAMP'.
     * */
    private Timestamp;
}