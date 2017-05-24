package pl.lodz.p.it.ssbd2016.ssbd01.moo.beans;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.mok.beans.HistoriaLogowaniaRaportBean;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;
   
/**
 * Ziarno odpowiedzialne za prezentacje informacji o ogłoszeniu. Umożliwia
 * manipulowanie ogloszeniem osobom upoważnionym.
 */
@Named
@RequestScoped
public class WyswietlSzczegolyOgloszeniaBean {
    
    @Inject
    private OgloszenieSession ogloszenieSession;
    
    private Ogloszenie ogloszenie; 
    List<Konto> listaAgentów;   
    int licznik=0, licznik2=0;
    

    public Ogloszenie getOgloszenie() throws WyjatekSystemu, IOException{
        if(ogloszenieSession.getOgloszenieDoWyswietlenia()==null){
            ogloszenieSession.setException(new WyjatekSystemu("blad.NullPointerException", "MOO"));
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            if(licznik==0){
                externalContext.redirect(origRequest.getContextPath() + "/wyjatki/wyjatekMOO.xhtml");  
                licznik++;
            }
            return null;
        }else{
            if(licznik2==0){
                licznik2++;
                ogloszenieSession.setOgloszenieDoWyswietlenia(ogloszenieSession.getOgloszenieDoWyswietlenia());
            }
            ogloszenie = ogloszenieSession.getOgloszenieDoWyswietlenia();
            return ogloszenie;
        }
    }
    /***
     * Funkcja sprawdzająca czy ogłoszenie posiada agenta
     * @return 
     */
    public Boolean posiadaAgenta() throws WyjatekSystemu{
        if(ogloszenieSession.getOgloszenieDoWyswietlenia()==null)
            return null;
        return ogloszenieSession.czyPosiadaJakiegosAgenta(ogloszenie);
    }

    /**
     * MOO.11
     * Handler na przycisku "Dodaj do ulubionych". Dodaje ogłoszenie do ulubionych
     * dla obecnie zalogowanego użytkownika(klienta).
     */
    public String dodajDoUlubionych() {
        ogloszenieSession.dodajDoUlubionych(ogloszenie);
        return "wyswietlSzczegolyOgloszenia";
    }
    
    /**
     * MOO.12
     * Handler na przycisku "Usun z ulubionych". Usuwa ogłoszenie z ulubionych
     * dla obecnie zalogowanego użytkownika(klienta).
     */
    public String usunZUlubionych() {
        ogloszenieSession.usunZUlubionych(ogloszenie);
        return "wyswietlSzczegolyOgloszenia";
    }    
    
    /**
     * Metoda sprawdza czy dane ogłoszenie jest w ulubionych, na potrzeby renderowania 
     * odpowiednich przycisków.
     * @return 
     */
    public Boolean czyUlubione() throws WyjatekSystemu {
        if(ogloszenieSession.getOgloszenieDoWyswietlenia()==null)
            return null;
        String login = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        return ogloszenie.isUlubione(login);
    }
          
    /**
     * Deaktywuje ogłoszenie innego użytkownika
     * @param ogloszenie innego użytkownika, które ma zostać deaktywowane
     * Przypadek użycia - MOO5
     */
    public String deaktywujOgloszenieInnegoUzytkownika() throws Exception {
        ogloszenieSession.deaktywujOgloszenieInnegoUzytkownika(ogloszenie);
        return "wyswietlOgloszenia";
    }
    
      /**
     * Sprawdza czy ogłoszenie jest aktywne
     * @return wartośc logiczna czy ogłoszenie jest aktywne
     * @throws WyjatekSystemu
     */    
    public Boolean czyOgloszenieAktywne() throws WyjatekSystemu {
        if(ogloszenieSession.getOgloszenieDoWyswietlenia()==null)
            return false;
        Ogloszenie otwarte = ogloszenieSession.getOgloszenieDoWyswietlenia();
        if(!otwarte.getAktywne())
            return false;
        return true;
    }
    
    /**
     * Pobiera liste agentow
     */
    public void pobierzListAgentow() {
        listaAgentów = ogloszenieSession.pobierzListeAgentow();
    }
    
    /***
     * Metoda odpowiada za pobranie ogłoszenia do edycji
     * Stowrzył Maksymilian Zgierski
     * MOO.2
     * @throws Exception
     * @return Łańcuch przekierowuje do widoku z edycją danych ogłoszenia
     */
    public String edytujOgloszenieDanegoUzytkownika() throws Exception {
        ogloszenieSession.pobierzOgloszenieDoEdycji(ogloszenie);
        return "edytujOgloszenieDanegoUzytkownika";
    }
    
      /***
     * Metoda odpowiada za pobranie ogłoszenia innego użytkownika do edycji
     * Stowrzył Tomasz Jędrzejewski
     * MOO.3
     * @return Łańcuch przekierowuje do widoku z edycją danych ogłoszenia
     */
    public String edytujOgloszenieInnegoUzytkownika() throws Exception {
        ogloszenieSession.pobierzOgloszenieDoEdycji(ogloszenie);
        return "edytujOgloszenieInnegoUzytkownika";
    }
    
    /**
     * Stworzył: Maksymilian Zgierski
     * Przypadek użycia: MOO.4 - Deaktywuj ogłoszenie dotyczące danego użytkownika 
     * @return łańcuch przekierowujący do widoku z przeglądaniem ogłoszeń
     * @throws Exception
     */
    public String deaktywujOgloszenieDanegoUzytkownika() throws Exception {
        try {
   //       ogloszenieSession.pobierzOgloszenieDoEdycji(ogloszenie);
            ogloszenieSession.deaktywujOgloszenieDanegoUzytkownika(ogloszenie);
            return "wyswietlOgloszenia";
        }
        catch(Exception e) {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            externalContext.redirect(origRequest.getContextPath() + "/wyjatki/wyjatekMOO.xhtml");  
        }
        return "wyswietlOgloszenia";
    }
    
    /**
     * Sprawdza czy użytkownik jest właścicielem lub agentem aktualnie otwartego ogłoszenia
     * @return wartośc logiczna czy ogłoszenie należy do aktualnie zalogowanego ogłoszenia
     * @throws WyjatekSystemu
     */
    public Boolean czyMojeOgloszenie() throws WyjatekSystemu
    {
        String loginKonta = ogloszenieSession.pobierzZalogowanegoUzytkownika();
        if(ogloszenieSession.getOgloszenieDoWyswietlenia()==null)
            return false;
        Ogloszenie otwarte = ogloszenieSession.getOgloszenieDoWyswietlenia();
        
        if(otwarte.getIdAgenta()!=null) {
            if(!otwarte.getIdWlasciciela().getLogin().equals(loginKonta)&& !otwarte.getIdAgenta().getLogin().equals(loginKonta))
                return false;
        }else{
            if(!otwarte.getIdWlasciciela().getLogin().equals(loginKonta)){
                return false;
            }
        }    
        return true;
    }
}
