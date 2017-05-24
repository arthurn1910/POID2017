package pl.lodz.p.it.ssbd2016.ssbd01.mok.beans;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.PoziomDostepu;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Klasa ta jest wykorzystywana do wyświetlania informacji o wybranym uzytkowniku 
 * oraz zablokowania, odblokowania i potwierdzenia jego konta
 */
@Named
@RequestScoped
public class WyswietlSzczegolyKontaBean {
    
    @Inject
    private UzytkownikSession uzytkownikSession;
    
    private Konto konto;
        
    /**
    * Metoda wywoływana zaraz po stworzeniu obiektu. Inicjalizuje pole
    * konto przez konto użytkownika którego chcemy wyświetlić
    */
    @PostConstruct
    public void initModel(){
        try {
            konto = uzytkownikSession.getWybraneKonto();
        } catch (WyjatekSystemu ex) {
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
    
    /**
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
     * Handler przycisku zablokuj w widoku. Blokuje wybrane konto oraz odświeża widok
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws javax.mail.MessagingException
     */
    public void zablokuj() throws WyjatekSystemu, MessagingException{
        uzytkownikSession.zablokujKonto(konto);
        initModel();
    }
    
    /**
     * Handler przycisku odblokuj w widoku. Odblokowuje wybrane konto oraz odświeża widok
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws javax.mail.MessagingException
     */
    public void odblokuj() throws WyjatekSystemu, MessagingException{
        uzytkownikSession.odblokujKonto(konto);
        initModel();
    }
    
    /**
     * Handler przycisku potwierdź w widoku. Potwierdza wybrane konto oraz odświeża widok
     */
    public String potwierdz(){
        uzytkownikSession.potwierdzKonto(konto);
        return "wyswietlSzczegolyKonta";
    }
    
    /**
     * Handler przycisku modyfkuj poziomy dostępu w widoku. Przekierowuje na widok edycji
     * @return      przekierowanie do widoku edycji
     */
    public String modyfikujPoziomyDostepu() {
        return "modyfikujPoziomyDostepu";
    }
    
    /**
     * Handler przyciksu edytuj dane w widoku. Pobiera wybrane konto do edycji
     * i przechodzi do odpowiendiej strony z edycją
     * @return      przekierowanie do strony z edycją
     * @throws java.io.IOException
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws java.lang.ClassNotFoundException
     */
    public String edytujKonto() throws IOException, WyjatekSystemu, ClassNotFoundException{
        uzytkownikSession.pobierzKontoDoEdycji(konto);
        return "edytujDaneKonta";
    }
    
    /**
     * Handler przyciksu edytuj hasło w widoku. Pobiera wybrane konto do edycji
     * i przechodzi do odpowiendiej strony z edycją
     * @return      przekierowanie do strony z edycją
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public String edytujHasloKonta() throws WyjatekSystemu, IOException, ClassNotFoundException{
        uzytkownikSession.pobierzKontoDoEdycji(konto);
        return "edytujHasloKonta";
    }
        
    // Gettery i Settery
    
    public Konto getKonto() {
        return konto;
    }
}
