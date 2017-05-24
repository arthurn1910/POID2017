package pl.lodz.p.it.ssbd2016.ssbd01.moo.managers;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;

import javax.ejb.Local;


/**
 * Interfejs dla OgloszenieManager
 */
@Local
public interface OgloszenieManagerLocal {

    /**
     * Metoda dodaje ogloszenie do ulubionych dla obecnie zalogowanego użytkownika
     * @param ogloszenie ogłoszenie, które ma być dodane
     */
     void dodajDoUlubionych(Ogloszenie ogloszenie);

    /**
     * Metoda usuwa ogloszenie z ulubionych dla obecnie zalogowanego użytkownika
     * @param ogloszenie ogłoszenie, które ma być usunięte
     */
    public void usunZUlubionych(Ogloszenie ogloszenie);    
    /***
     * Metoda przydzielajaca ogenta do ogłoszenia
     * @param rowData
     * @param agent 
     */
    public void przydzielAgenta(Ogloszenie rowData, Konto agent);

    /**
     * Metoda odpowiedzialna za przeliczanie agregatu, gdzie agregatem jest srednia cena nieruchomosci
     */
    void przeliczAgregat();
}
