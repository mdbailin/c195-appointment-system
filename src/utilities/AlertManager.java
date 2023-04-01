package utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import languages.LanguageManager;

/**
 * The Alerter class is a utility class designed for displaying alerts
 * and confirmation windows in a user-friendly manner.
 * This class is responsible for providing an interactive user interface,
 * which allows users to make decisions based on the presented information.
 * With this class, you can create alert and confirmation windows that display information,
 * such as messages, warnings, errors, and provide options for users to take actions.
 * The Alerter class is designed to assist applications in displaying helpful messages,
 * guiding users through complex processes, and providing them with clear feedback.
 */
public abstract class AlertManager {
    /**
     * Displays an alert with a custom message and title. Message and title are retrieved from the LanguageManager.
     * @param message - the desired message for the alert
     * @param title - the desired title for the alert window
     **/
    public static void showAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.NONE, LanguageManager.getTranslation(message), ButtonType.OK);
        alert.setTitle(LanguageManager.getTranslation(title));
        alert.setHeight(900);
        alert.show();
    }
    /**
     * Displays a confirmation window and returns true if user selects YES, false if NO or CANCEL is selected.
     * @param message - the desired message for the confirmation window to show
     * @return true if user selects YES, false if user selects NO or CANCEL
     **/
    public static boolean showConfirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, LanguageManager.getTranslation(message), ButtonType.YES,
                ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }
}
