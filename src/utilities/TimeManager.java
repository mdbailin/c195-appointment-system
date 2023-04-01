package utilities;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * The TimeProcessor class is a powerful utility that allows users to perform various time-based tasks with ease.
 * It facilitates operations such as merging, comparing, formatting, and computing time differences.
 * Whether you need to calculate the duration between two time stamps or format a time value in a specific way,
 * this class is your go-to solution. With its intuitive interface and robust functionality,
 * you can manipulate time data in a simple and efficient manner.
 */
public abstract class TimeManager {
    // Define the date and time formats to be used for different purposes
    private static DateTimeFormatter SQL_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter REPORT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
    private static DateTimeFormatter LABEL_FORMAT = DateTimeFormatter.ofPattern(("HH:mm"));
    private static DateTimeFormatter DAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * This method takes in two arguments, a LocalDate object representing a date and a LocalTime object
     * representing a time, and combines them to create a new ZonedDateTime object. This new object
     * represents the given date and time in the UTC time zone. The UTC time zone is a universal
     * standard time zone that is used as a reference point for other time zones around the world.
     *
     * @param date A LocalDate object representing the date.
     * @param time A LocalTime object representing the time.
     * @return A ZonedDateTime object representing the combined date and time in UTC zone.
     */
    public static ZonedDateTime combineDateAndTime(LocalDate date, LocalTime time) {
        // If both date and time are valid
        if (date != null && time != null) {
            LocalDateTime combinedDateTime = LocalDateTime.of(date, time);
            ZonedDateTime utcDateTime = ZonedDateTime.of(combinedDateTime, ZoneId.of("America/New_York"));
            return ZonedDateTime.of(utcDateTime.toLocalDateTime(), ZoneId.of("UTC"));
        }
        // If either date or time is null, return the current time
        return ZonedDateTime.now();
    }

    /**
     * This method generates a LocalTime object by taking in two parameters specifying the hour and minute of the time.
     * It returns an instance of the LocalTime class, which has been initialized with the values
     * passed in for hour and minute. The LocalTime class is a part of the Java DateTime API and represents a
     * time-of-day, without any reference to a specific date or time zone.
     * @param hour An integer value representing the hour of the day. It must be in the range of 0 to 23,
     *             where 0 represents midnight and 23 represents 11:00 PM.
     * @param minute An integer value representing the minute of the hour. It must be in the range of 0 to 59,
     *               where 0 represents the start of any hour and 59 represents the end of any hour.
     * @return A new instance of the LocalTime class, initialized with the values specified for hour and minute.
     */
    public static LocalTime generateLocalTime(int hour, int minute) {
        return LocalTime.of(hour, minute, 0);
    }

    /**
     * This method takes in a ZonedDateTime object and creates a new ZonedDateTime object with the TimeZone that
     * matches the System default TimeZone. This is done through a conversion process that takes into account
     * the date, time, and time zone information of the input ZonedDateTime object.
     *
     * @param time The ZonedDateTime object that needs to be converted to a new ZonedDateTime object matching the
     *             System default TimeZone.
     * @return A new ZonedDateTime object that reflects the same date and time information as the input
     * ZonedDateTime object, but with the TimeZone component matching the System default TimeZone setting of the system.
     */
    public static ZonedDateTime toSystemTimeZone(ZonedDateTime time) {
        return ZonedDateTime.of(time.toLocalDateTime(), ZoneId.systemDefault());
    }

    /**
     * Converts a Timestamp object into a new ZonedDateTime object using system default TimeZone.
     * @param time The Timestamp object to be converted.
     * @return A ZonedDateTime object with TimeZone matching the System default TimeZone.
     */
    public static ZonedDateTime toSystemTimeZone(Timestamp time) {
        return ZonedDateTime.of(time.toLocalDateTime(), ZoneId.systemDefault());
    }

    /**
     * Converts a given ZonedDateTime object into a new ZonedDateTime object using EST TimeZone.
     * @param time The ZonedDateTime object to be converted.
     * @return A new ZonedDateTime object with TimeZone set to EST.
     */
    public static ZonedDateTime toESTTimeZone(ZonedDateTime time) {
        return ZonedDateTime.of(time.toLocalDateTime(), ZoneId.of("America/New_York"));
    }

    /**
     * Converts a given LocalTime object into a new LocalTime object in EST timezone.
     * @param time The LocalTime object to be converted to EST timezone.
     * @return A new LocalTime object in EST timezone.
     */
    public static LocalTime toESTTimeZone(LocalTime time) {
        ZoneId systemTimeZone = ZoneId.systemDefault();
        ZoneId estTimeZone = ZoneId.of("America/New_York");
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), time);
        ZonedDateTime systemDateTime = ZonedDateTime.of(today, systemTimeZone);
        ZonedDateTime estDateTime = systemDateTime.withZoneSameInstant(estTimeZone);
        return estDateTime.toLocalTime();
    }

    /**
     * Converts a given LocalTime to Eastern Standard Time (EST) and returns the resulting formatted label.
     * @param time The LocalTime to convert.
     * @return A String containing the time converted to EST in the format "(hh:mm:ss a) EST".
     */
    public static String convertToESTAndFormatLabel(LocalTime time) {
        ZoneId systemTimeZone = ZoneId.systemDefault();
        ZoneId estTimeZone = ZoneId.of("America/New_York");
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), time);
        ZonedDateTime systemZoneDateTime = ZonedDateTime.of(today, systemTimeZone);
        ZonedDateTime estZoneDateTime = systemZoneDateTime.withZoneSameInstant(estTimeZone);
        DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return " (" + labelFormatter.format(estZoneDateTime) + ") EST";
    }

    /**
     * Returns a Timestamp object representing the value of the current UTC time.
     * @return A Timestamp object representing the current UTC time.
     */
    public static Timestamp getTimestampForCurrentUTCTime() {
        return Timestamp.valueOf(SQL_FORMAT.format(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"))));
    }

    /**
     * Converts a given ZonedDateTime object into a new Timestamp object, formatted using SQL_FORMAT format.
     * @param time The ZonedDateTime object to be converted to Timestamp.
     * @return A Timestamp object generated from the given ZonedDateTime and SQL_FORMAT.
     */
    public static Timestamp toTimestampUsingSQLFormat(ZonedDateTime time) {
        return Timestamp.valueOf(SQL_FORMAT.format(time));
    }

    /**
     * Returns a String representing only the date part of a given ZonedDateTime object.
     * @param time The ZonedDateTime object to be formatted.
     * @return A String object representing the date part of the given ZonedDateTime object.
     */
    public static String getDateFromZonedDateTime(ZonedDateTime time) {
        return DAY_FORMAT.format(time);
    }

    /**
     * Returns a String representing only the time part of a given ZonedDateTime object.
     * @param time The ZonedDateTime object to be formatted.
     * @return A String object representing the time part of the given ZonedDateTime object.
     */
    public static String getTimeFromZonedDateTime(ZonedDateTime time) {
        return LABEL_FORMAT.format(time);
    }
}
