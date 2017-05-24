package pl.lodz.p.it.ssbd2016.ssbd01.moo.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Obsługa widoku wyjatek.xhtml
 */
@Named
@RequestScoped
public class WyjatekMooBean {
    private String wiadomosc;
    @Inject
    private OgloszenieSession ogloszenieSession;
    
    /**
     * Inicjalizujemy wiadomość na stronie
     */
    @PostConstruct
    public void initModel() {
        Exception ex = ogloszenieSession.getException();
        
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
