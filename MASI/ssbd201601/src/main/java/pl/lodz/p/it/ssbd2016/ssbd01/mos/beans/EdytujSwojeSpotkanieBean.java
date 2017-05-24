package pl.lodz.p.it.ssbd2016.ssbd01.mos.beans;

import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Spotkanie;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Obsługuje widok edycji danych
 */
@Named
@RequestScoped
public class EdytujSwojeSpotkanieBean {
    @Inject
    private SpotkanieSession spotkanieSession;
    
    private Spotkanie spotkanie;
    /**
     * Zapisuje spotkanie po edycji
     */
    public void zapiszSwojeSpotkaniePoEdycji() {
        spotkanieSession.zapiszSpotkaniePoEdycji(spotkanie);
        
    }
    
    /**
     * pobiera spotkanie do edycji
     * @return 
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu 
     * @throws java.io.IOException 
     * @throws java.lang.ClassNotFoundException 
     */
    public Spotkanie getSpotkanieEdytuj() throws WyjatekSystemu, IOException, ClassNotFoundException {
        return spotkanieSession.pobierzSpotkanieDoEdycji(spotkanie);
    }
}
