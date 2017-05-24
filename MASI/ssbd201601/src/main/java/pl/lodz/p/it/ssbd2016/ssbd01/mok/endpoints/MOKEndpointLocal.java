package pl.lodz.p.it.ssbd2016.ssbd01.mok.endpoints;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.HistoriaLogowania;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;

import javax.ejb.Local;
import java.util.List;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Interfejs API servera dla modułu funkcjonalnego MOK
 */
@Local
public interface MOKEndpointLocal {

    /**
     * Metoda wprowadza do systemu konto klienta (niepotwierdzone)
     * @param konto informacje kontcie do utworzenia
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    void rejestrujKontoKlienta(Konto konto) throws NoSuchAlgorithmException, UnsupportedEncodingException, NamingException, MessagingException;
    
    /**
     * Metoda wprowadza do systemu konto o dowolnym, niewykluczajacym sie poziomie dostepu
     * @param konto informacje kontcie do utworzenia
     * @param poziomyDostepu lista poziomów dostępu jakie beda przypisane
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    void utworzKonto(Konto konto, List<String> poziomyDostepu) throws NoSuchAlgorithmException, UnsupportedEncodingException, NamingException, WyjatekSystemu;
    
    /**
     * Metoda zmienia stan konta na potwierdzone
     * @param konto konto, które ma zostać potwierdzone
     */
    void potwierdzKonto(Konto konto);

    /**
     * Metoda zmienia stan konta na aktywne
     * @param konto konto które ma zostać odblokowane
     */
     void odblokujKonto(Konto konto) throws MessagingException;

    /**
     * Metoda zmienia stan konta na nieaktywne
     * @param konto konto które ma zostać zablokowane
     */
     void zablokujKonto(Konto konto) throws MessagingException;

    /**
     * Zmienia hasło dla obecnie zalogowanego użytkownika
     * @param noweHaslo  nowe hasło w postaci jawnej
     * @param stareHaslo stare hasło w postaci jawnej
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    void zmienMojeHaslo(String noweHaslo, String stareHaslo) throws WyjatekSystemu, NoSuchAlgorithmException,UnsupportedEncodingException;

    /**
     * Zmienia hasło dla obecnie edytowanego użytkownika
     * @param noweHaslo nowe hasło w postaci jawnej
     */
    void zmienHaslo(String noweHaslo) throws WyjatekSystemu, NoSuchAlgorithmException,UnsupportedEncodingException;

    /**
     * Wyszukuje konto o podanym loginie
     * @param login login konta
     * @return znalezione konto
     */
    Konto znajdzPoLoginie(String login);

    /**
     * Metoda zwracająca liste kont podobnych do zadanego konta
     * @param konto     obiekt zawierający kryteria wyszukania
     * @return          lista podobnych kont
     */

    public List<Konto> pobierzPodobneKonta(Konto konto);
    
    /**
     * Metoda zwracająca liste wszystkich kont
     * @return lista wszystkich kont
     */
    public List<Konto> pobierzWszystkieKonta();
    
    /**
     * Metoda dodająca dany poziom dostępu do konta
     * @param konto     konto do którego należy dodać poziom dostępu
     * @param poziom    nazwa poziomu dostępu
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    public void dodajPoziomDostepu(Konto konto, String poziom) throws WyjatekSystemu,NamingException;

    /**
     * Metoda odłączająca dany poziom dostępu do konta
     * @param konto     konto od którego należy odłączyć poziom dostępu
     * @param poziom    nazwa poziomu dostępu
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    public void odlaczPoziomDostepu(Konto konto, String poziom) throws WyjatekSystemu,NamingException;

    /**
     * Metoda pobierająca konto do edycji. Zapewnia blokadę optymistyczną.
     * @param konto     konto które chcemy edytować
     * @return          głęboka kopia encji Konto
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    Konto pobierzKontoDoEdycji(Konto konto) throws IOException, ClassNotFoundException;
    
    /**
     * Metoda zapisuje konto użytkownika obecnie zalogowanego ze zmienionymi
     * parameterami. Sprawdzana jest blokada optymistyczna.
     * @param konto     konto ze zmianami
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    public void zapiszSwojeKontoPoEdycji(Konto konto) throws WyjatekSystemu;
    
    /**
     * Metoda zapisuje konto ze zmienionymi parameterami. Sprawdzana jest
     * blokada optymistyczna.
     * @param konto konto ze zmianami
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    void zapiszKontoPoEdycji(Konto konto) throws WyjatekSystemu;

    /**
     * Metoda pobierająca konto obecnie zalogowanego użytkownika
     * @return      konto obecnie zalogowanego użytkownika
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
     Konto getSwojeKonto() throws WyjatekSystemu;
 
    /**
     * Metoda pobiera całą historię aktywności użytkowników związanych z logowaniem
     *
     * @return 
     */
    List<HistoriaLogowania> pobierzHistorieLogowanUzytkownikow();
    
    /**
     * MOK.16 loguje historie logowanie uzytkownika
     * @param login 
     */
    void ustawIP(String login);
}

