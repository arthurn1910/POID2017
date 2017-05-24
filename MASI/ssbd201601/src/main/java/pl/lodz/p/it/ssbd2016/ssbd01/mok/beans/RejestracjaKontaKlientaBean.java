package pl.lodz.p.it.ssbd2016.ssbd01.mok.beans;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Ziarno umożliwiające tworzenie nowych kont dla klientów
 */
@Named
@RequestScoped  
public class RejestracjaKontaKlientaBean {
    @Inject
    private UzytkownikSession uzytkownikSession;
    
    private Konto konto = new Konto();
    private String potorzoneHaslo;
    
    /**
     * Handler dla przycisku rejestruj. Metoda tworzy nowe konto klienta 
     * @return 
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    public String rejestrujKontoKlienta() throws WyjatekSystemu, NoSuchAlgorithmException, UnsupportedEncodingException, NamingException, MessagingException{
        if (checkPasswordMatching()) {    
            uzytkownikSession.rejestrujKontoKlienta(konto);
            return "index";
        }
        return null;
    }
    
    /**
     * Metoda sprawdzająca czy podane hasła są identyczne
     * @return  decyzja czy hasła są identyczne
     */
    public boolean checkPasswordMatching(){
        if (!(konto.getHaslo().equals(potorzoneHaslo))) {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", context.getViewRoot().getLocale());
            FacesMessage message = new FacesMessage(bundle.getString("walidacja.zlePotworzoneHaslo"));
            context.addMessage("form:hasloPotworzone", message);
            return false;
        }
        return true;
    }
    
    // Gettery i Settery
        
    public Konto getKonto() {
        return konto;
    }

    public String getPotorzoneHaslo() {
        return potorzoneHaslo;
    }

    public void setPotorzoneHaslo(String potorzoneHaslo) {
        this.potorzoneHaslo = potorzoneHaslo;
    }
}
