package pl.lodz.p.it.ssbd2016.ssbd01.moo.endpoints;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.*;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

import javax.ejb.Local;
import java.io.IOException;
import java.util.List;
import javax.naming.NamingException;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.ElementWyposazeniaNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Nieruchomosc;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypOgloszenia;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Interfejs API servera dla modułu funkcjonalnego MOO
 */
@Local
public interface MOOEndpointLocal {

    Konto getKonto(String login);

    /**
     * Pobiera wszystkie typy ogłoszeń z tabel słownikowych
     * @return lista kluczy
     */
    List<TypOgloszenia> pobierzTypyOgloszen();

    /**
     * Pobiera wszystkie typy nieruchomości z tabel słownikowych
     * @return lista kluczy
     */
    List<TypNieruchomosci> pobierzTypyNieruchomosci();

    /**
     * Dodaje ogłszenie dla nieruchomości MOO. 1, Kamil Rogowski
     *
     * @param noweOgloszenie   ogłoszenie
     * @param nowaNieruchomosc nieruchomości
     * @param elementWyposazeniaNieruchomosci lista elementow wyposazenia nieruchomosci
     */
    void dodajOgloszenie(Ogloszenie noweOgloszenie, Nieruchomosc nowaNieruchomosc,  List<ElementWyposazeniaNieruchomosci> elementWyposazeniaNieruchomosci);

    /**
     * Pobiera wszystkie predefiniowane elementy dla kategorii
     * @return lista kluczy do elementow kategorii
     */
    List<ElementWyposazeniaNieruchomosci> pobierzElementyKategorii();
    /**
     * Pobiera wszystkie predefiniowane kategorie
     * @return lista kluczy do kategorii
     */
    List<KategoriaWyposazeniaNieruchomosci> pobierzKategorie();
    /**
     * Metoda deaktywuje ogłoszenie
     * @param rowData
     * @throws WyjatekSystemu 
     */
    public void deaktywujOgloszenie(Ogloszenie rowData) throws WyjatekSystemu;

    /**
     * Metoda edytuje ogloszenie dotyczace klienta MOO.2
     * @param ogloszenieNowe
     * @throws WyjatekSystemu 
     */
    public void edytujOgloszenieDotyczaceUzytkownika(Ogloszenie ogloszenieNowe) throws WyjatekSystemu;    
    
    /**
     * Metoda odpowiadajaca za edycje ogloszenia danego uzytkownika MOO.3
     * @param ogloszenieNowe
     * @throws WyjatekSystemu
    */   
    public void edytujOgloszenieInnegoUzytkownika(Ogloszenie ogloszenieNowe) throws WyjatekSystemu;    
    
    /**
     * Metoda deaktywuje ogłoszenie dotyczace użytkownika MOO.4
     * @param  o ogloszenie do deaktywacji
     * @throws WyjatekSystemu
     */
    public void deaktywujOgloszenieDotyczaceUzytkownika(Ogloszenie o) throws WyjatekSystemu;
        
    /**
    * Metoda odpowiadajaca za deaktywacje ogłoszenia innego uzytkownika MOO.5
    * @param ogloszenie do deaktywacji
    * @throws WyjatekSystemu
    */   
    public void deaktywujOgloszenieInnegoUzytkownika(Ogloszenie ogloszenie) throws WyjatekSystemu;
    
    /***
     * Metoda przydzielająca konto agenta do ogłoszenia
     * Stowrzył Radosław Pawlaczyk
     * MOO.6
     * MOO 7
     * @param rowData
     * @param agent 
     */
    public void przydzielAgentaDoOgloszenia(Ogloszenie rowData, Konto agent) ;
    
    /***
     * Funkcja zwracająca ogłoszenie o wybranym ID
     * @param ID
     * @return 
     */
    public Ogloszenie znajdzOgloszeniePoID(Long ID);
    
    /**
     * Metoda pobiera wszystkie ogłoszenia MOO.8 i 9
     * @return lista ogłoszeń
     */
    List<Ogloszenie> pobierzWszytkieOgloszenia();
        
    /**
     * Metoda dodaje ogloszenie do ulubionych dla obecnie zalogowanego użytkownika MOO.11
     * @param ogloszenie ogłoszenie, które ma być dodane
     */
    public void dodajDoUlubionych(Ogloszenie ogloszenie);
    
    /**
     * Metoda usuwa ogloszenie z ulubionych dla obecnie zalogowanego użytkownika MOO.12
     * @param ogloszenie ogłoszenie, które ma być usunięte
     */
    public void usunZUlubionych(Ogloszenie ogloszenie);    

    /**
     * Metoda pobierająca ogłoszenie do edycji. Zapewnia blokade optymistyczną.
     * @param ogloszenie ogloszenie do edycji
     * @return 
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu 
     * @throws java.io.IOException 
     * @throws java.lang.ClassNotFoundException 
     */
    public Ogloszenie pobierzOgloszenieDoEdycji(Ogloszenie ogloszenie) throws WyjatekSystemu, IOException, ClassNotFoundException;
         
    /**
     * Pobiera liste agentów
     * @return lista agentow
     */ 
    public List<Konto> pobierzListeAgentow();
    
    /**
     * Zwraca login aktualnie zalogowanego użytkownika
     * @return login użytkownika
     */
    public String pobierzZalogowanegoUzytkownika();
    
    /**
     * Zwraca wyposażenie nieruchomości
     * @param idNieruchomosci id nieruchomości
     * @return wyposażenie
     */
    public List<ElementWyposazeniaNieruchomosci> pobierzWyposazenieNieruchomosci(Long idNieruchomosci);
    
    /**
     * Zwraca wszystkie możliwe wyposażenia nieruchomości
     * @return wyposażenie
     */
    public List<ElementWyposazeniaNieruchomosci> getWszystkieMozliweElementyWyposazeniaNieruchomosci();
    /***
     * Funkcja sprawdza czy ogłoszenie posiada danego agenta.
     * @param ogloszenie
     * @param rowData
     * @return 
     */
    public Boolean czyPosiadaAgenta(Ogloszenie ogloszenie, Konto rowData);
    /***
     * Funkcja sprawdza czy ogłoszenie posiada jakiegoś agenta.
     * @param ogloszenie
     * @return 
     */
    public Boolean czyPosiadaJakiegosAgenta(Ogloszenie ogloszenie);
    /***
     * Funkcja zwracająca liste agentów.
     * @return 
     */
    public List<Konto> getAgenci() throws NamingException;
    
    public TypNieruchomosci getTypNieruchomosci(String typ);
    public TypOgloszenia getTypOgloszenia(String typ) ;
            
}
