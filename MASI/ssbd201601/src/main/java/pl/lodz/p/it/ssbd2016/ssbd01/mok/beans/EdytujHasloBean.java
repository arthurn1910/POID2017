package pl.lodz.p.it.ssbd2016.ssbd01.mok.beans;

import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Obsługa zmiany hasła przez użytkownika i admina
 */
@Named
@RequestScoped
public class EdytujHasloBean {

    @Inject
    private UzytkownikSession uzytkownikSession;
    
    private String nowePowtorzoneHaslo;
    private String noweHaslo;
    private String stareHaslo;

    /**
     * Metoda sprawdzająca czy podane hasła są identyczne
     * @return  decyzja czy hasła są identyczne
     */
    public boolean checkPasswordMatching() {
        if (!(noweHaslo.equals(nowePowtorzoneHaslo))) {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", context.getViewRoot().getLocale());
            FacesMessage message = new FacesMessage(bundle.getString("walidacja.zlePotworzoneHaslo"));
            context.addMessage("form:powtorzoneHaslo", message);
            return false;
        }
        return true;
    }
    
    /**
     * Sprawdza dlugosc pol noweHaslo i nowePotworzoneHaslo w przypadku
     * edycji hasla przez admina
     * @return  true - jeśli hasłą mają conajmniej 8 znaków
     */
    public boolean sprawdzDlugoscHasla() {
        if (noweHaslo == null || noweHaslo.length() < 8) {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", context.getViewRoot().getLocale());
            FacesMessage message = new FacesMessage(bundle.getString("walidacja.zaKrotkieHaslo"));
            context.addMessage("form:noweHaslo", message);
            return false;             
        } else if (nowePowtorzoneHaslo == null || nowePowtorzoneHaslo.length() < 8) {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", context.getViewRoot().getLocale());
            FacesMessage message = new FacesMessage(bundle.getString("walidacja.zaKrotkieHaslo"));
            context.addMessage("form:powtorzoneHaslo", message);
            return false;             
        }
        return true;
    }
    
    /**
     * Sprawdza dlugosc pol stareHaslo, noweHaslo i nowePotworzoneHaslo w przypadku
     * edycji własnego hasla przez uzytkownika
     * @return  true - jeśli hasłą mają conajmniej 8 znaków
     */
    public boolean sprawdzDlugoscHaslaMojego() {
        if (stareHaslo == null || stareHaslo.length() < 8) {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", context.getViewRoot().getLocale());
            FacesMessage message = new FacesMessage(bundle.getString("walidacja.zaKrotkieHaslo"));
            context.addMessage("form:obecneHaslo", message);
            return false;            
        } else if (noweHaslo == null || noweHaslo.length() < 8) {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", context.getViewRoot().getLocale());
            FacesMessage message = new FacesMessage(bundle.getString("walidacja.zaKrotkieHaslo"));
            context.addMessage("form:noweHaslo", message);
            return false;             
        } else if (nowePowtorzoneHaslo == null || nowePowtorzoneHaslo.length() < 8) {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", context.getViewRoot().getLocale());
            FacesMessage message = new FacesMessage(bundle.getString("walidacja.zaKrotkieHaslo"));
            context.addMessage("form:powtorzoneHaslo", message);
            return false;             
        }
        return true;
    }
    
    /**
     * Handler dla przyciku potwierdź. Metoda zmienia hasło dla obecnie
     * zalogowanego użytkownika i przekierowuje do szczegółów danego konta
     * @return  przekierowanie do szczegółów konta 
     */
    public String zmienMojeHaslo()throws Exception{
        if (sprawdzDlugoscHaslaMojego() && checkPasswordMatching()) {
            uzytkownikSession.zmienMojeHaslo(noweHaslo, stareHaslo);
            return "wyswietlSzczegolySwojegoKonta";
        }
        return null;
    }

    /**
     * Handler dla przyciku potwierdź. Metoda zmienia hasło dla wybranego konta
     * i przekierowuje do szczegółów danego konta
     * @return  przekierowanie do szczegółów konta 
     */
    public String zmienHaslo() throws Exception{
        if (sprawdzDlugoscHasla() && checkPasswordMatching()) {
            uzytkownikSession.zmienHaslo(noweHaslo);
        return "wyswietlSzczegolyKonta";
        }
        return null;
    }

    // Gettery i Settery
    
    public String getStareHaslo() {
        return stareHaslo;
    }

    public void setStareHaslo(String stareHaslo) {
        this.stareHaslo = stareHaslo;
    }

    public String getNoweHaslo() {
        return noweHaslo;
    }

    public void setNoweHaslo(String noweHaslo) {
        this.noweHaslo = noweHaslo;
    }

    public String getNowePowtorzoneHaslo() {
        return nowePowtorzoneHaslo;
    }

    public void setNowePowtorzoneHaslo(String nowePowtorzoneHaslo) {
        this.nowePowtorzoneHaslo = nowePowtorzoneHaslo;
    }
}
