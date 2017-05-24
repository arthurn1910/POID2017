package pl.lodz.p.it.ssbd2016.ssbd01.mok.beans;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.PoziomDostepu;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;


/**
 * Klasa ta jest wykorzystywana do wyświetlania informacji o obecnie zalogowanym
 * uzytkowniku, oraz umożliwia edycję hasła i danych. 
 */
@Named
@RequestScoped
public class WyswietlSzczegolySwojegoKontaBean {
    
    @Inject
    private UzytkownikSession uzytkownikSession;
    
    private Konto konto;
        
    /**
    * Metoda wywoływana zaraz po stworzeniu obiektu. Inicjalizuje pole
    * konto przez konto użytkownika obecnie zalogowanego
    */
    @PostConstruct
    public void initModel(){
        try{
            konto = uzytkownikSession.getSwojeKonto();
        }catch(WyjatekSystemu ex){
            uzytkownikSession.setException(ex);
            Logger lg=Logger.getLogger("javax.enterprice.system.conteiner.web.faces");
            lg.log(Level.SEVERE, this.getClass()+": Wystąpił wyjątek: ",ex);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            try {
                externalContext.redirect(origRequest.getContextPath() + "/wyjatki/wyjatek.xhtml");
            } catch (IOException ex1) {
                Logger.getLogger(HistoriaLogowaniaRaportBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
    /***
     * Metoda tworząca string złożony z nazw poziomów dostępu danego użytkownika
     * @return  string zawierający poziomy dostępu oddzielone przecinkiem
     */
    public String pobierzPoziomy(){
        String poziomy = "";
        
        for (PoziomDostepu poziom:konto.getPoziomDostepuCollection()) {
            if (poziom.getAktywny()) {
                poziomy += poziom.getPoziom() + ", ";
            }
        }
        
        return poziomy;
    }
    
    /**
     * Handler przyciksu edytuj dane w widoku. Ustawia konto obecnie zalogowanego
     * użytkownika do edycji i przechodzi do odpowiendiej strony z edycją
     * @return      przekierowanie do strony z edycją
     * @throws java.lang.Exception
     */
    public String edytujSwojeKonto() throws Exception{
        uzytkownikSession.pobierzKontoDoEdycji(konto);
        return "edytujDane";
    }
    
    /**
     * Handler przyciksu edytuj hasło w widoku. Ustawia konto obecnie zalogowanego
     * użytkownika do edycji i przechodzi do odpowiendiej strony z edycją
     * @return      przekierowanie do strony z edycją
     * @throws java.lang.Exception
     */
    public String edytujSwojeHasloKonta() throws Exception{
        uzytkownikSession.pobierzKontoDoEdycji(konto);
        return "edytujHaslo";
    }
    
    // Gettery i Settery
    
    public Konto getKonto() {
        return konto;
    }
}
