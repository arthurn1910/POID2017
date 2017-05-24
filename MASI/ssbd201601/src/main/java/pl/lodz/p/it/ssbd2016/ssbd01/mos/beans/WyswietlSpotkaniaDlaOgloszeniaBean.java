package pl.lodz.p.it.ssbd2016.ssbd01.mos.beans;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Spotkanie;
import pl.lodz.p.it.ssbd2016.ssbd01.mok.beans.HistoriaLogowaniaRaportBean;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.beans.OgloszenieSession;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Obsługuje widok spotkań dla ogłoszenia
 */
@Named
@RequestScoped
public class WyswietlSpotkaniaDlaOgloszeniaBean {


    private Ogloszenie ogloszenie;
    /**
     * Lista spotkan dla ogloszenia
     */
    private List<Spotkanie> spotkania;

    @Inject
    private SpotkanieSession spotkanieSession;

    @Inject
    private OgloszenieSession ogloszenieSession;

    /**
     * inicjalizacja modelu danych, MOS.5, Kamil Rogowski
     */
    @PostConstruct
    public void init() {

        try {
            ogloszenie = ogloszenieSession.getOgloszenieDoWyswietlenia();
            spotkania = spotkanieSession.pobierzSpotkaniaDlaOgloszenia(ogloszenie);
        } catch (WyjatekSystemu ex) {
            Logger lg = Logger.getLogger("javax.enterprice.system.conteiner.web.faces");
            ex.setMessage("blad.NullPointerException");
            spotkanieSession.setException(ex);
            lg.log(Level.SEVERE, this.getClass() + ": Wystąpił wyjątek: ", ex);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            try {
                externalContext.redirect(origRequest.getContextPath() + "/wyjatki/wyjatekMOS.xhtml");
            } catch (IOException ex1) {
                Logger.getLogger(HistoriaLogowaniaRaportBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    public Ogloszenie getOgloszenie() {
        return ogloszenie;
    }

    public void setOgloszenie(Ogloszenie ogloszenie) {
        this.ogloszenie = ogloszenie;
    }

    public List<Spotkanie> getSpotkania() {
        return spotkania;
    }

    public void setSpotkania(List<Spotkanie> spotkania) {
        this.spotkania = spotkania;
    }
}