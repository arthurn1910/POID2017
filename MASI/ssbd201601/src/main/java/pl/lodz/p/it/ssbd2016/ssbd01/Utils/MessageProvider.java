package pl.lodz.p.it.ssbd2016.ssbd01.Utils;


import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by Kamil Rogowski on 28.04.2016.
 *
 * @author Kamil Rogowski
 */
public class MessageProvider {

    /**
     * pole przechowujące nazwę pliku z ustawieniami
     */
    private static final String MESSAGES_FILE = "i18n.messages";

    /**
     * pole przechowujące nazwę pliku słownika
     */
    private static final String SETTINGS_FILE = "settings";

    private MessageProvider() {

    }

    /**
     * metoda odpowiedzialna za pobieranie wartości z ustawień
     *
     * @param key klucz
     * @return wartość
     */
    public static String getConfig(String key) {

        return ResourceBundle.getBundle(SETTINGS_FILE).getString(key);
    }

    /**
     * metoda odpowiedzialna za pobieranie wiadomości ze słownika
     *
     * @param key klucz
     * @return wartość
     */
    public static String getMessage(String key) {
        String wynik;
        try {
            wynik = ResourceBundle.getBundle(MESSAGES_FILE).getString(key);
        } catch (MissingResourceException e) {
            wynik = key + "???";
        }
        return wynik;
    }
}
