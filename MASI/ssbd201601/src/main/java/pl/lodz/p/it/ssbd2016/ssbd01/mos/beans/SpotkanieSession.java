package pl.lodz.p.it.ssbd2016.ssbd01.mos.beans;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Spotkanie;
import pl.lodz.p.it.ssbd2016.ssbd01.mos.endpoints.MOSEndpointLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Ziarno zarządzające sesją użytkownika. Udostępnia API dla widoku.
 */
@SessionScoped
@ManagedBean(name = "spotkanieSession")
public class SpotkanieSession implements Serializable {

    @EJB
    private MOSEndpointLocal mosEndpoint;
    
    private Exception exception;
    private Ogloszenie wybraneOgloszenie;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    private boolean czyWyswietlicPotwierdzenie;
    
    /**
     * Inicjalizacja modelu danych
     */
    @PostConstruct
    public void initModel(){
    }

    /**
     * Pobiera listę spotkań dla konta
     * MOS.4, MOS.2 P. Stepien
     * @param spotkaniaDlaKonta konto,
     * @return lista spotkań
     */
    public List<Spotkanie> pobierzSpotkania(Konto spotkaniaDlaKonta) {
        return mosEndpoint.pobierzSpotkaniaKlienta(spotkaniaDlaKonta);
    }

    /**
     * Pobiera listę swoich spotkań dla konta - klient,
     * MOS. 4, Kamil Rogowski
     * @return lista spotkań
     */
    List<Spotkanie> pobierzSwojeSpotkania() {
         return  mosEndpoint.pobierzSpotkaniaKlienta(mosEndpoint.pobierzMojeKonto());
    }

    /**
     * Pobiera listę swoich spotkań dla konta - agent,
     * MOS. 4, Kamil Rogowski
     * @return lista spotkań
     */
    List<Spotkanie> pobierzSpotkaniaAgenta() {
        return  mosEndpoint.pobierzSpotkaniaAgenta(mosEndpoint.pobierzMojeKonto());
    }
    /**
     * Anuluje spotkania dla konta, MOS.3, Kamil Rogowski
     *  po anuluje przekierowuje na ta samo strone
     * @param spotkanieDoAnulowania spotkanie do anulowania
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    public void anulujSpotkanie(Spotkanie spotkanieDoAnulowania) throws WyjatekSystemu {
        try {
            mosEndpoint.anulujSpotkanie(spotkanieDoAnulowania);
            czyWyswietlicPotwierdzenie = true;
        } catch (WyjatekSystemu e) {
            this.exception = e;
            throw e;
        }

    }

    /**
     * Pobiera spotkania dla ogłoszenia, MOS. 5, Kamil Rogowski
     *
     * @param ogloszenie ogłoszenie
     * @return lista spotkań
     */
    public List<Spotkanie> pobierzSpotkaniaDlaOgloszenia(Ogloszenie ogloszenie) {

        return mosEndpoint.pobierzSpotkaniaDlaOgloszenia(ogloszenie);
    }

    /**
     * rezerwuje spotkanie MOS 1, Radosław Pawlaczyk
     * @param spotkanie 
     */
    public void rezerwujSpotkanie(Spotkanie spotkanie) throws WyjatekSystemu {
        try{
            mosEndpoint.rezerwujSpotkanie(spotkanie);
            czyWyswietlicPotwierdzenie = true;
        }catch(WyjatekSystemu ex){
            this.exception=ex;
            throw ex;
        }
    }
    
    /**
     * pobiera spotkanie do edycji MOS.2 P. Stepien
     * @param spotkanie
     * @return Spotkanie
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public Spotkanie pobierzSpotkanieDoEdycji(Spotkanie spotkanie) throws WyjatekSystemu, IOException, ClassNotFoundException {
        try{
            return mosEndpoint.pobierzSpotkanieDoEdycji(spotkanie);
        }catch(Exception e){
            this.exception=e;
            throw e;
        }
    }
    
    /**
     * zapisuje spotkanie po edycji MOS.2 P. Stepien
     * @param spotkanie 
     */
    public void zapiszSpotkaniePoEdycji(Spotkanie spotkanie) {
        mosEndpoint.zapiszSpotkaniePoEdycji(spotkanie);
        czyWyswietlicPotwierdzenie = true;
    }

    /**
     * Metoda zwraca wartość określającą czy pokazać potwierdzenie operacji
     *
     * @return
     */
    public boolean isCzyWyswietlicPotwierdzenie() {
        if (czyWyswietlicPotwierdzenie) {
            czyWyswietlicPotwierdzenie = false;
            return true;
        }
        return false;
    }
}
