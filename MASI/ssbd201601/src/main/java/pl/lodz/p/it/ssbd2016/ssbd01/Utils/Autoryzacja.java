package pl.lodz.p.it.ssbd2016.ssbd01.Utils;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.NamingException;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Klasa wykorzystana do identyfikacji roli użytkownika
 */
@Named
@RequestScoped
public class Autoryzacja {
    
    /**
     * Sprawdza czy użytkownik jest administratorem
     * @return
     * @throws WyjatekSystemu 
     */
    public boolean czyAdministrator() throws WyjatekSystemu, NamingException
    {
        PoziomDostepuManager tmp=new PoziomDostepuManager();
        return sprawdzRole(tmp.getPoziomyDostepu().get(0));
    }
    
    /**
     * Sprawdza czy użytkownik jest agentem
     * @return
     * @throws WyjatekSystemu 
     */
    public boolean czyAgent() throws WyjatekSystemu, NamingException
    {
        PoziomDostepuManager tmp=new PoziomDostepuManager();
        return sprawdzRole(tmp.getPoziomyDostepu().get(2));
    }
    
    /**
     * Sprawdza czy użytkownik jest klientem
     * @return
     * @throws WyjatekSystemu 
     */
    public boolean czyKlient() throws WyjatekSystemu, NamingException
    {
        PoziomDostepuManager tmp=new PoziomDostepuManager();
        return sprawdzRole(tmp.getPoziomyDostepu().get(3));
    }
    
    /**
     * Sprawdza czy użytkownik jest menadżerem
     * @return
     * @throws WyjatekSystemu 
     */
    public boolean czyMenadzer() throws WyjatekSystemu, NamingException
    {
        PoziomDostepuManager tmp=new PoziomDostepuManager();
        return sprawdzRole(tmp.getPoziomyDostepu().get(1));
    }
    
    /**
     * Metoda sprawdza czy użytkownik ma daną rolę
     * @return
     * @throws WyjatekSystemu 
     */
    private boolean sprawdzRole(String role) {
        if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role)) {
            return true;
        }
        return false;
    }
}
