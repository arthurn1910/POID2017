package pl.lodz.p.it.ssbd2016.ssbd01.mos.managers;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Spotkanie;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

import javax.ejb.Local;
import java.util.List;

/**
 * Interfejs dla menadżera spotkań
 */
@Local
public interface SpotkanieManagerLocal {

    /**
     * Pobiera listę spotkań dla ogłosznia MOS.5, Kamil Rogowski
     *
     * @param ogloszenie ogłoszenie dla którego szukamy spotkań
     * @return lista spotkań
     */
    List<Spotkanie> pobierzSpotkaniaDlaOgloszenia(Ogloszenie ogloszenie);

    /**
     * Dodaje spotkanie
     * @param spotkanie spotkanie jakie ma zostac dodane
     * @param login
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    void rezerwujSpotkanie(Spotkanie spotkanie, String login) throws WyjatekSystemu;

    /**
     * edytuje nasze spotkanie
     * @param spotkanie
     */
    void edytujSwojeSpotkanie(Spotkanie spotkanie);

    /**
     * Anuluje spotkanie powiązane z kontem, stworzona dla MOS.3, Kamil Rogowski
     * @param spotkanieDoAnulowania spotkanie do anulowania
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    void anulujSpotkanie(Spotkanie spotkanieDoAnulowania) throws WyjatekSystemu;
}