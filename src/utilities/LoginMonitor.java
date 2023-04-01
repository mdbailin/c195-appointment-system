package utilities;

import java.time.ZonedDateTime;

/**
 * This Helper class serves the purpose of recording login activity by capturing attempts to login to a system.
 * Whenever a user tries to log in, their password is checked for correctness.
 * If the password is valid, the attempt is recorded in a file named "login_activity.txt".
 * The purpose of recording these attempts is to keep a log for security purposes and
 * to monitor any unauthorized attempts to access the system.
 * The recorded entries for successful logins are marked with a tag, "CORRECT_PASSWORD",
 * indicating that no invalid credentials were used for that particular attempt.
 */
public abstract class LoginMonitor {
    /**
     * Records the login attempt details and returns a success flag.
     *
     * @param username The username entered.
     * @param password The password entered by the user.
     * @return A boolean indicating whether the attempt was successful.
     */
    public static boolean recordAttempt(String username, String password) {
        String date = TimeManager.getDateFromZonedDateTime(ZonedDateTime.now());
        String time = TimeManager.getTimeFromZonedDateTime(ZonedDateTime.now());
        String attemptDetails = "Date: " + date + "\nTime: " + time + "\nUsername: " + username +"\nPassword: "
                + password + "\n----------";
        boolean isSuccessful = password.equals("CORRECT_PASSWORD");
        if (isSuccessful) {
            ReportManager.writeReportToFile("login_activity.txt", "Login attempt successful: "
                    + attemptDetails);
        } else {
            ReportManager.writeReportToFile("login_activity.txt", "Login attempt failed: "
                    + attemptDetails);
        }
        return isSuccessful;
    }
}
