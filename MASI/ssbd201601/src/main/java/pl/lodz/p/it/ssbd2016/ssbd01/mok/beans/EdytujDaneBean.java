package pl.lodz.p.it.ssbd2016.ssbd01.mok.beans;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Obsługa zmiany danych przez admina i użytkownika
 */
@Named
@RequestScoped
public class EdytujDaneBean {

    @Inject
    private UzytkownikSession uzytkownikSession;
    
    /**
     * Handler dla przyciku potwierdź. Metoda zmienia dane dla obecnie
     * zalogowanego użytkownika i przekierowuje do szczegółów danego konta
     * @return  przekierowanie do szczegółów konta 
     * @throws java.lang.Exception 
     */
    public String zapiszSwojeKontoPoEdycji() throws Exception{
        uzytkownikSession.zapiszSwojeKontoPoEdycji();
        return "wyswietlSzczegolySwojegoKonta";
    }
    
    /**
     * Handler dla przyciku potwierdź. Metoda zmienia hasło dla wybranego konta
     * i przekierowuje do szczegółów danego konta
     * @return  przekierowanie do szczegółów konta
     * @throws java.lang.Exception
     */
    public String zapiszKontoPoEdycji() throws Exception{
        uzytkownikSession.zapiszKontoPoEdycji();
        return "wyswietlSzczegolyKonta";
    }
    
    // Gettery i Settery
    
    public Konto getKontoEdytuj() throws WyjatekSystemu {
        return uzytkownikSession.getKontoEdytuj();
    }
}