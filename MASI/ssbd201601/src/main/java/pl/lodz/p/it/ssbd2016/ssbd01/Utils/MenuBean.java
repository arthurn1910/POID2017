package pl.lodz.p.it.ssbd2016.ssbd01.Utils;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.PoziomDostepu;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.ssbd2016.ssbd01.mok.beans.UzytkownikSession;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.beans.OgloszenieSession;
import pl.lodz.p.it.ssbd2016.ssbd01.mos.beans.SpotkanieSession;

/**
 * Ziarno odpowiedzialne za wylogowanie/zakończenie sesji
 */
@Named
@RequestScoped
public class MenuBean {
    
    @Inject
    private UzytkownikSession uzytkownikSession;
    @Inject
    private OgloszenieSession ogloszenieSession;
    @Inject
    private SpotkanieSession spotkanieSession;

    String login;
    String poziomyDostepu;
    boolean zalogowany;
    boolean czyWyswietlicPotwierdzenie;
    
    /**
    * Metoda wywoływana zaraz po stworzeniu obiektu. Inicjalizuje pole
    * konto przez konto użytkownika obecnie zalogowanego
     * oraz procesuje dalej login użytkownika w celu zapisu jego IP
    */
    @PostConstruct
    public void initModel() {
        String login = getContext().getRemoteUser();
        if (login != null) {
            uzytkownikSession.zapiszIP(login);
            Konto konto = uzytkownikSession.znajdzPoLoginie(login);
            this.login = konto.getLogin();
            poziomyDostepu = "";
            for (PoziomDostepu poziom:konto.getPoziomDostepuCollection()) {
                if (poziom.getAktywny()) {
                    poziomyDostepu += poziom.getPoziom() + ", ";
                }
            }
            zalogowany = true;            
        } else {
            zalogowany = false;
        }
        czyWyswietlicPotwierdzenie = uzytkownikSession.isCzyWyswietlicPotwierdzenie();
        czyWyswietlicPotwierdzenie |= ogloszenieSession.isCzyWyswietlicPotwierdzenie();
        czyWyswietlicPotwierdzenie |= spotkanieSession.isCzyWyswietlicPotwierdzenie();
    }
    
    /**
     * Metoda zwracająca zewnętrzny kontekst
     * @return  zewnętrzny kontekst
     */
    public static ExternalContext getContext(){
        return FacesContext.getCurrentInstance().getExternalContext();
    }
    
    /**
     * Metoda unieważniająca obecną sesję
     * @return  przekierowanie do strony głównej
     */
    public String wyloguj() {
        getContext().invalidateSession();
        return "index";
    }
    
    public boolean czyWyswietlicPotwierdzenie() {        
        return czyWyswietlicPotwierdzenie;
    }
    
    public String getLogin() {
        return login;
    }

    public String getPoziomyDostepu() {
        return poziomyDostepu;
    }

    public boolean isZalogowany() {
        return zalogowany;
    }
}
