package pl.lodz.p.it.ssbd2016.ssbd01.moo.beans;

import pl.lodz.p.it.ssbd2016.ssbd01.Utils.ZalogowanyUzytkownik;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.*;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.endpoints.MOOEndpointLocal;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.ElementWyposazeniaNieruchomosci;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 * Ziarno zarządzające sesją użytkownika. Udostępnia API dla widoku.
 */
@SessionScoped
@ManagedBean(name = "ogloszenieSession")
public class OgloszenieSession implements Serializable {
    private static final Logger logger = Logger.getLogger(OgloszenieSession.class.getName());

    @EJB
    private MOOEndpointLocal mooEndpoint;
    private Ogloszenie ogloszenieDoWyswietlenia;

    private Ogloszenie ogloszenieEdytuj;
    
    private boolean czyWyswietlicPotwierdzenie;
    private List<Ogloszenie> ogloszeniaDataModel;
    
    private List<ElementWyposazeniaNieruchomosci> wyposazenieNieruchomosci;
    private List<ElementWyposazeniaNieruchomosci> mozliweWyposazenie;
    
    private Exception exception;
    int licznik=0, licznikWyswietlOgloszenia = 0;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
    
    public List<ElementWyposazeniaNieruchomosci> getWyposazenieNieruchomosci() {
        return wyposazenieNieruchomosci;
    }
    public void setWyposazenieNieruchomosci(List<ElementWyposazeniaNieruchomosci> w) {
        wyposazenieNieruchomosci = w;
    }
    
    public List<ElementWyposazeniaNieruchomosci> getMozliweWyposazenie() {
        return mozliweWyposazenie;
    }
    public void setMozliweWyposazenie(List<ElementWyposazeniaNieruchomosci> w) {
        mozliweWyposazenie = w;
    }


    
    public void setOgloszenieDoWyswietlenia(Ogloszenie ogloszenieDoWyswietlenia) {
        this.ogloszenieDoWyswietlenia = ogloszenieDoWyswietlenia;
        licznik=0;
    }
    
    public void setOgloszeniaDataModel(List<Ogloszenie> o) {
        ogloszeniaDataModel = o;
    }
    
    /**
     * Zwraca DataModel ogloszen, ogranicza wykonanie zapytań do bazy danych
     * @return 
     */
    public List<Ogloszenie> getOgloszeniaDataModel() {
        if(this.licznikWyswietlOgloszenia == 0) {
            pobierzWszystkieOgloszenia();
            licznikWyswietlOgloszenia = 1;
            return ogloszeniaDataModel;
        }
        if (null == ogloszeniaDataModel) pobierzWszystkieOgloszenia();
        return ogloszeniaDataModel;
    }

    /**
     * MOO. 1 Dodaje ogłoszenie dla nieruchomości, Kamil Rogowski
     *
     * @param ogloszenie   ogłoszenie do dodania
     * @param nieruchomosc nieruchomość do dodania
     */
    void dodajOgloszenie(Ogloszenie ogloszenie, Nieruchomosc nieruchomosc, List<ElementWyposazeniaNieruchomosci> elem) {

        final Nieruchomosc nowaNieruchomosc = ustawNieruchomosc(nieruchomosc);
        final Ogloszenie noweOgloszenie = ustawOgloszenie(ogloszenie);
        final List<ElementWyposazeniaNieruchomosci> elementWyposazeniaNieruchomosci = new ArrayList<>(elem);
        noweOgloszenie.setNieruchomosc(nowaNieruchomosc);
        nowaNieruchomosc.setOgloszenie(noweOgloszenie);
        nowaNieruchomosc.setElementWyposazeniaNieruchomosciCollection(elementWyposazeniaNieruchomosci);
        elementWyposazeniaNieruchomosci.forEach(e -> e.getNieruchomoscWyposazona().add(nowaNieruchomosc));

        mooEndpoint.dodajOgloszenie(noweOgloszenie, nowaNieruchomosc, elementWyposazeniaNieruchomosci);
        czyWyswietlicPotwierdzenie = true;
    }

    /**
     * Tworzy ogloszenie na podstawie podanego
     * @param ogloszenie
     * @return 
     */
    private Ogloszenie ustawOgloszenie(Ogloszenie ogloszenie) {
        Ogloszenie noweOgloszenie = new Ogloszenie();
        noweOgloszenie.setTytul(ogloszenie.getTytul());
        noweOgloszenie.setCena(ogloszenie.getCena());
        noweOgloszenie.setRynekPierwotny(ogloszenie.getRynekPierwotny());
        noweOgloszenie.setAktywne(false);
        noweOgloszenie.setDataDodania(new Date());
        final String loginZalogowanegoUzytkownika = ZalogowanyUzytkownik.getLoginZalogowanegoUzytkownika();
        noweOgloszenie.setIdWlasciciela(mooEndpoint.getKonto(loginZalogowanegoUzytkownika));
        noweOgloszenie.setTypOgloszenia(ogloszenie.getTypOgloszenia());
        return noweOgloszenie;
    }
    
    /**
     * Tworzy nieruchomosc na podstawie podanego
     * @param ogloszenie
     * @return 
     */
    private Nieruchomosc ustawNieruchomosc(Nieruchomosc nieruchomosc) {
        Nieruchomosc nowaNieruchomosc = new Nieruchomosc();
        nowaNieruchomosc.setMiejscowosc(nieruchomosc.getMiejscowosc());
        nowaNieruchomosc.setUlica(nieruchomosc.getUlica());
        nowaNieruchomosc.setRokBudowy(nieruchomosc.getRokBudowy());
        nowaNieruchomosc.setPowierzchniaNieruchomosci(nieruchomosc.getPowierzchniaNieruchomosci());
        nowaNieruchomosc.setTypNieruchomosci(nieruchomosc.getTypNieruchomosci());
        nowaNieruchomosc.setLiczbaPieter(nieruchomosc.getLiczbaPieter());
        nowaNieruchomosc.setLiczbaPokoi(nieruchomosc.getLiczbaPokoi());
        nowaNieruchomosc.setPowierzchniaDzialki(nieruchomosc.getPowierzchniaDzialki());
        nowaNieruchomosc.getElementWyposazeniaNieruchomosciCollection().addAll(nieruchomosc.getElementWyposazeniaNieruchomosciCollection());

        return nowaNieruchomosc;
    }

    /**
     * Pobiera wszystkie ogłoszenia
     *
     * @return lista ogłoszeń
     */
    void pobierzWszystkieOgloszenia() {
        ogloszeniaDataModel = mooEndpoint.pobierzWszytkieOgloszenia();
    }

    /**
     * Edytuje dane ogłoszenie
     * @throws WyjatekSystemu
     */
    void edytujOgloszenieDanegoUzytkownika() throws WyjatekSystemu {       
        try {
            this.licznikWyswietlOgloszenia = 0;
            mooEndpoint.edytujOgloszenieDotyczaceUzytkownika(ogloszenieEdytuj);
            czyWyswietlicPotwierdzenie = true;
        } catch (WyjatekSystemu e) {
            this.exception = e;
            throw e;
        }
    }

    /**
     * Deaktywuje ogłoszenie
     * @param ogloszenie ogłoszenie do deaktywacji
     * @throws Exception
     */
    void deaktywujOgloszenieDanegoUzytkownika(Ogloszenie ogloszenie) throws Exception {
        try{
            this.licznikWyswietlOgloszenia = 0;
            mooEndpoint.deaktywujOgloszenieDotyczaceUzytkownika(ogloszenie);
            czyWyswietlicPotwierdzenie = true;
        }catch(WyjatekSystemu e){
            this.exception=e;
            throw e;
        }
    }

    /***
     * Metoda wywołuje metodę przydzielAgentaDoOgloszenia w MOOEndpoint przekazując jej parametr Ogloszenie
     * Stowrzył Radosław Pawlaczyk
     * MOO 6
     * MOO 7
     */
    void przydzielAgentaDoOgloszenia(Ogloszenie rowData, Konto agent)  {
        mooEndpoint.przydzielAgentaDoOgloszenia(rowData, agent);
        czyWyswietlicPotwierdzenie = true;
    }

    /**
     * Metoda dodaje ogloszenie do ulubionych dla obecnie zalogowanego użytkownika
     *
     * @param ogloszenie ogłoszenie, które ma być dodane
     */
    void dodajDoUlubionych(Ogloszenie ogloszenie) {
        mooEndpoint.dodajDoUlubionych(ogloszenie);
        czyWyswietlicPotwierdzenie = true;
    }

    /**
     * Metoda usuwa ogloszenie z ulubionych dla obecnie zalogowanego użytkownika
     *
     * @param ogloszenie ogłoszenie, które ma być usunięte
     */
    void usunZUlubionych(Ogloszenie ogloszenie) {
        mooEndpoint.usunZUlubionych(ogloszenie);
        czyWyswietlicPotwierdzenie = true;
    }

    /**
     * metoda deaktywująca ogłoszenie innego użytkownika
     *
     * @param ogloszenie, które ma zostać deaktywowane
     */
    void deaktywujOgloszenieInnegoUzytkownika(Ogloszenie ogloszenie) throws Exception {
        try {
            mooEndpoint.deaktywujOgloszenieInnegoUzytkownika(ogloszenie);
            czyWyswietlicPotwierdzenie = true;
        } catch (WyjatekSystemu e) {
            this.exception=e;
            throw e;
        }
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
    /**
     * Metoda pobierająca ogłoszenie do edycji. Zapewnia blokade optymistyczną.
     *
     * @param ogloszenie ogloszenie do edycji
     */
    void pobierzOgloszenieDoEdycji(Ogloszenie ogloszenie) throws WyjatekSystemu, IOException, ClassNotFoundException {
        try{
            setOgloszenieEdytuj(mooEndpoint.pobierzOgloszenieDoEdycji(ogloszenie));
            Nieruchomosc n = ogloszenieEdytuj.getNieruchomosc();
            wyposazenieNieruchomosci = mooEndpoint.pobierzWyposazenieNieruchomosci(n.getId());
            mozliweWyposazenie = mooEndpoint.getWszystkieMozliweElementyWyposazeniaNieruchomosci();
            for(int i = 0; i < mozliweWyposazenie.size(); i++) {
                for(int j = 0; j < wyposazenieNieruchomosci.size(); j++) {
                    if(mozliweWyposazenie.get(i).getId().equals(wyposazenieNieruchomosci.get(j).getId()))
                        mozliweWyposazenie.remove(i);
                }
            }        
        }catch(Exception e){
            this.exception=e;
            throw e;
        }
    }

    /**
     * Metoda zapisuje zmienione ogloszenie innego uzytkownika.
     * @throws WyjatekSystemu
     */
    public void zapiszOgloszenieInnegoUzytkownikaPoEdycji() throws WyjatekSystemu{
            mooEndpoint.edytujOgloszenieInnegoUzytkownika(ogloszenieEdytuj);
            czyWyswietlicPotwierdzenie = true;
    }

    /**
     * metoda umożliwiająca edycje ogłoszenia innego użytkownika
     */
    void edytujOgloszenieInnegoUzytkownika() throws Exception {
        try {
            mooEndpoint.edytujOgloszenieInnegoUzytkownika(ogloszenieEdytuj);
            pobierzWszystkieOgloszenia();
            czyWyswietlicPotwierdzenie = true;
        } catch (Exception e) {
            this.exception = e;
            throw e;
        }
    }
    
    /**
     * Pobiera liste agentów
     *
     * @return lista agentow
     */
    List<Konto> pobierzListeAgentow() {
        return mooEndpoint.pobierzListeAgentow();
    }
    
    /**
     * Zwraca ogloszenie do wysweitlenia, ogranicza liczbe zapytań do bazy
     * @return
     * @throws WyjatekSystemu 
     */
    public Ogloszenie getOgloszenieDoWyswietlenia() throws WyjatekSystemu {
        try{
            if(ogloszenieDoWyswietlenia==null){
                WyjatekSystemu ex=new WyjatekSystemu("blad.NullPointerException", "MOO");
                WyjatekSystemu exc=new WyjatekSystemu("blad.NullPointerException",ex, "MOO");
                this.exception=exc;
                return null;
            }
            if(licznik==0){
                licznik++;
                ogloszenieDoWyswietlenia=mooEndpoint.znajdzOgloszeniePoID(ogloszenieDoWyswietlenia.getId());
            }
            return ogloszenieDoWyswietlenia;
        }catch(Exception e){
            WyjatekSystemu ex=new WyjatekSystemu("blad.NullPointerException", "MOO");
            WyjatekSystemu exc=new WyjatekSystemu("blad.NullPointerException",ex, "MOO");
            this.exception=exc;
            return null;
        }
    }

    /**
     * Pobiera wszystkie typy ogłoszeń z tabel słownikowych
     *
     * @return lista kluczy
     */
    public List<TypOgloszenia> pobierzTypyOgloszen() {
        return mooEndpoint.pobierzTypyOgloszen();
    }

    /**
     * Pobiera wszystkie typy nieruchomości z tabel słownikowych
     *
     * @return lista kluczy
     */
    public List<TypNieruchomosci> pobierzTypyNieruchomosci() {

        return mooEndpoint.pobierzTypyNieruchomosci();
    }

    public List<ElementWyposazeniaNieruchomosci> pobierzElementyKategorii() {
        return mooEndpoint.pobierzElementyKategorii();
    }

    public List<KategoriaWyposazeniaNieruchomosci> pobierzKategorie() {
        return mooEndpoint.pobierzKategorie();
    }

    public Ogloszenie getOgloszenieEdytuj() {
        return ogloszenieEdytuj;
    }

    public void setOgloszenieEdytuj(Ogloszenie ogloszenieEdytuj) {
        this.ogloszenieEdytuj = ogloszenieEdytuj;
    }
    
    /**
     * Zwraca login aktualnie zalogowanego użytkownika
     * @return login użytkownika
     */
    public String pobierzZalogowanegoUzytkownika() {
        return mooEndpoint.pobierzZalogowanegoUzytkownika();
    }

    /***
     * Funkcja zwracająca liste agentów.
     * @return 
     */
    List<Konto> getAgenci() throws NamingException {
        return mooEndpoint.getAgenci();
    }
    /***
     * Funkcja sprawdza czy ogłoszenie posiada danego agenta.
     * @return 
     */
    Boolean czyPosiadaAgenta(Ogloszenie ogloszenie, Konto rowData) {
        return mooEndpoint.czyPosiadaAgenta(ogloszenie, rowData);
    }
    /***
     * Funkcja sprawdza czy ogłoszenie posiada jakiegoś agenta.
     * @return 
     */
    Boolean czyPosiadaJakiegosAgenta(Ogloszenie ogloszenie) {
        return mooEndpoint.czyPosiadaJakiegosAgenta(ogloszenie);
    }

}