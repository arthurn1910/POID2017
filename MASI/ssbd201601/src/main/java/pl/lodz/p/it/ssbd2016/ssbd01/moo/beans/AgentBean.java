package pl.lodz.p.it.ssbd2016.ssbd01.moo.beans;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.mok.beans.HistoriaLogowaniaRaportBean;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 *Klasa AgentBean obsługuje widok dla przydziel i zmień agenta
 * @author java
 */
@Named
@RequestScoped
public class AgentBean {
    
    @Inject
    private OgloszenieSession ogloszenieSession;
    private Ogloszenie ogloszenie;
    
    
    private DataModel<Konto> agenci;

    /**
     * Metoda inizjalizująca dostępne poziomy dostępu i konta, dla którego
     * będą modyfikowane poziomy dostępu
     */
    @PostConstruct
    private void initModel(){
        try {
            agenci = new ListDataModel<Konto>(ogloszenieSession.getAgenci());
            ogloszenie = ogloszenieSession.getOgloszenieDoWyswietlenia();
        } catch (NamingException | WyjatekSystemu ex) {
            Logger lg=Logger.getLogger("javax.enterprice.system.conteiner.web.faces");
            lg.log(Level.SEVERE, this.getClass()+": Wystąpił wyjątek: ",ex);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            try {
                externalContext.redirect(origRequest.getContextPath() + "/wyjatki/wyjatekMOO.xhtml");
            } catch (IOException ex1) {
                Logger.getLogger(HistoriaLogowaniaRaportBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
   /***
     * Metoda wywołuje metodę przydzielAgentaDoOgloszenia w OgloszenieSession przekazując jej parametry Ogloszenie, Konto
     * Stowrzył Radosław Pawlaczyk
     * MOO 6
     * MOO 7
     */
    public String przydzielAgenta() {
        ogloszenieSession.przydzielAgentaDoOgloszenia(ogloszenie, agenci.getRowData());
        initModel();
        return "wyswietlOgloszenia";
    }
    
    /**
     * Metoda dla widoku definiująca czy ogloszenie posiada danego agenta. 
     * Zmienia dostępne klawisze między dołącz i odłącz
     * @return  decyzcja czy konto posiada aktywny poziom dostępu
     */
    public boolean czyPosiadaAgenta(){
        return ogloszenieSession.czyPosiadaAgenta(ogloszenie, agenci.getRowData());
    }
    
    /**
     * Metoda dla widoku definiująca czy ogloszenie posiada jakiegos agenta. 
     * Zmienia dostępne klawisze między dołącz i odłącz
     * @return  decyzcja czy konto posiada aktywny poziom dostępu
     */
    public boolean czyPosiadaJakiegosAgenta(){
        return ogloszenieSession.czyPosiadaJakiegosAgenta(ogloszenie);
    }
    
    // Gettery i Settery    
    
    public DataModel<Konto> getAgenci() {
        return agenci;
    }    
}
