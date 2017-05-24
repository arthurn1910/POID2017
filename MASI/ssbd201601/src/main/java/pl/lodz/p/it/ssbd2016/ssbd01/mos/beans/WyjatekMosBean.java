package pl.lodz.p.it.ssbd2016.ssbd01.mos.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Obsługa widoku wyjatek.xhtml
 */
@Named
@RequestScoped
public class WyjatekMosBean {
    private String wiadomosc;
    @Inject
    private SpotkanieSession spotkanieSession;
    
    /**
     * Inicjalizujemy wiadomość na stronie
     */
    @PostConstruct
    public void initModel() {
        Exception ex = spotkanieSession.getException();
        
        if (ex != null) {
            this.wiadomosc = ex.getMessage();
            if(this.wiadomosc==null || "".equals(this.wiadomosc))
                this.wiadomosc="blad.brakUprawnien";
        }
    }

    public String getWiadomosc() {
        return wiadomosc;
    } 
}
