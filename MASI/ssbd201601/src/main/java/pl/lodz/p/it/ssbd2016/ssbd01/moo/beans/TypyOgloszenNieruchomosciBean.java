package pl.lodz.p.it.ssbd2016.ssbd01.moo.beans;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypOgloszenia;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by Kamil Rogowski on 26.05.2016.
 * Zawiera obsługę typów ogłoszenia i typów nieruchomości dla formularza dodaj ogłoszenia
 */
@RequestScoped
@Named
public class TypyOgloszenNieruchomosciBean {

    @Inject
    private OgloszenieSession ogloszenieSession;
    /**
     * Lista wszystkich typoów ogłoszeń w postaci kluczy do słownika
     */
    private List<TypOgloszenia> typyOgloszen;
    /**
     * Lista wszystkich typoów nieruchomości w postaci kluczy do słownika
     */
    private List<TypNieruchomosci> typyNieruchomosci;

    /**
     * Metoda pobiera wszystkie klucze do typow ogloszen i typow nieruchomosci
     */
    @PostConstruct
    public void init() {
        typyOgloszen = ogloszenieSession.pobierzTypyOgloszen();
        typyNieruchomosci = ogloszenieSession.pobierzTypyNieruchomosci();
    }

    public List<TypOgloszenia> getTypyOgloszen() {
        return typyOgloszen;
    }

    public void setTypyOgloszen(List<TypOgloszenia> typyOgloszen) {
        this.typyOgloszen = typyOgloszen;
    }

    public List<TypNieruchomosci> getTypyNieruchomosci() {
        return typyNieruchomosci;
    }

    public void setTypyNieruchomosci(List<TypNieruchomosci> typyNieruchomosci) {
        this.typyNieruchomosci = typyNieruchomosci;
    }
}
