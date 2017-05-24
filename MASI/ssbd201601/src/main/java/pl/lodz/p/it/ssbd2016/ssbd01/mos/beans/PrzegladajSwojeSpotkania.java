package pl.lodz.p.it.ssbd2016.ssbd01.mos.beans;

import pl.lodz.p.it.ssbd2016.ssbd01.Utils.Autoryzacja;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Spotkanie;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Obsługuje widok przeglądania swoich spotkań
 */
@Named
@RequestScoped
public class PrzegladajSwojeSpotkania implements Serializable {

    @Inject
    private SpotkanieSession spotkanieSession;
    private List<Spotkanie> spotkania;

    public Date getTenDzien() {
        return tenDzien;
    }

    private Date tenDzien = new Date();

    /**
     * Metoda wywoływana zaraz po stworzeniu obiektu. Inicjalizuje pole
     * konto przez konto użytkownika obecnie zalogowanego
     */

    /**
     * Inicjalizuje listę spotkań dla swojego konta
     */
    @PostConstruct
    public void init() {
        final Autoryzacja autoryzacja = new Autoryzacja();

        try {
            if (autoryzacja.czyKlient()) {
                spotkania = spotkanieSession.pobierzSwojeSpotkania();

            } else spotkania = spotkanieSession.pobierzSpotkaniaAgenta();
        } catch (NamingException ex) {
            spotkanieSession.setException(ex);

        } catch (WyjatekSystemu ex) {
            Logger lg = Logger.getLogger("javax.enterprice.system.conteiner.web.faces");
            lg.log(Level.SEVERE, this.getClass() + ": Wystąpił wyjątek: ", ex);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            try {
                externalContext.redirect(origRequest.getContextPath() + "/wyjatki/wyjatek.xhtml");
            } catch (IOException ex1) {
                Logger.getLogger(PrzegladajSwojeSpotkania.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }


    /**
     * Anuluje spotkania powiązane z kontem MOS.3, Kamil Rogowski
     *  @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     *  @return wyswietlMojeSpotkania ta sama strona
     */
    public String anulujSpotkanie(Spotkanie wybraneSpotkanie) throws WyjatekSystemu {

        spotkanieSession.anulujSpotkanie(wybraneSpotkanie);
        return "wyswietlMojeSpotkania";
    }

    public List<Spotkanie> getSpotkania() {
        return spotkania;
    }
}