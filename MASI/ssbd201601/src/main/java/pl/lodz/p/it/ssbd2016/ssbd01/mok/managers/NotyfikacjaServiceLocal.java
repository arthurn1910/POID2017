package pl.lodz.p.it.ssbd2016.ssbd01.mok.managers;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;

import javax.ejb.Local;
import javax.mail.MessagingException;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Interfejs serwisu odpwoedzialnego za komunikaty mailowe
 * Created by Kamil Rogowski on 28.04.2016.
 *
 * @author Kamil Rogowski
 */
@Local
public interface NotyfikacjaServiceLocal {

    /**
     * Metoda odpowiedzialna za wysłanie powiadomienia o zarejstrowaniu konta
     *
     * @param konto
     */
    void wyslijPowiadomienieRejestracji(Konto konto) throws MessagingException;

    /**
     * metoda odpowiedzialna za wysłania powiadomienia o zablokowaniu konta
     *
     * @param konto
     */
    void wyslijPowiadomienieZablokowaniaKonta(Konto konto) throws MessagingException;

    /**
     * metoda odpowiedzialna za wysłania powiadomienia o aktywacji konta
     *
     * @param konto
     */
    void wyslijPowiadomienieAktywacjiKonta(Konto konto) throws MessagingException;

}
