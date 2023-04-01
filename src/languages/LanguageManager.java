package languages;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The TranslationHelper class serves as a valuable tool for enabling the efficient translation of text-based
 * resources within an application. This class achieves this goal by utilizing the Resource Bundle 'messages'
 * to provide a centralized location for storing and accessing key-value pairs for all translated texts.
 * The TranslationHelper thereby simplifies the process of internationalizing an application,
 * and enables developers to easily make their software accessible to users speaking different languages. */

public abstract class LanguageManager {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("languages/lang", Locale.getDefault());
    private static boolean translationExists = false;

    /**
     * Attempts to translate the given string using the available language resources.
     *
     * If a translation is found for the input string, it returns the translated string;
     * otherwise, it returns the original string.
     *
     * @param text The text to be translated.
     * @return String The translated text, if available; otherwise, returns the original text.
     */
    public static String getTranslation(String text) {
        try {
            resourceBundle.getString(text);
            translationExists = true;
        } catch (MissingResourceException mre) {
            translationExists = false;
        }
        if (translationExists) {
            return resourceBundle.getString(text);
        } else {
            return text;
        }
    }
}
