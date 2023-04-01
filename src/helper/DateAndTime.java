package helper;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * The DateAndTime class is responsible for managing date and time information across
 * different time zones used in the application.
 * It provides various methods that allow users to easily convert date and time information
 * between different time zones, as well as perform other necessary operations related to managing time information.
 * This class serves as a central location for managing all date and time calculations,
 * ensuring consistency and accuracy across the application.
 * By using the DateAndTime class, developers can avoid common mistakes associated with managing date and time data,
 * and ensure a smooth and reliable user experience.
 */

public class DateAndTime {

    public static Timestamp getCurrentTimeStamp() {
        ZoneId zoneid = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now(zoneid);
        Timestamp currentTimestamp = Timestamp.valueOf(localDateTime);
        return currentTimestamp;
    }

    public static LocalDate getCurrentLocalDate() {
        LocalDate currentDate = LocalDate.now();
        return currentDate;
    }
}


