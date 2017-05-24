package pl.lodz.p.it.ssbd2016.ssbd01.mok.managers;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.HistoriaLogowania;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Kamil Rogowski on 30.04.2016.
 *
 * @author Kamil Rogowski
 *         API dla serwisu przetwarzającego IP z requestu i wiązanie go z kontem
 */

@Local
public interface AdresIpServiceLocal {
    /**
     * Pobiera adres IP z requestu
     *
     * @return
     */
    String getClientIpAddress();

    /**
     * Przetwarza login i IP
     *
     * @param login konta
     */
    void processIpAdress(String login);

    /**
     * zwraca historię logowań użytkowniów
     *
     * @return lista logowań
     */
    List<HistoriaLogowania> pobierzHistorieLogowanUzytkownikow();
}
