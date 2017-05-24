package pl.lodz.p.it.ssbd2016.ssbd01.mok.managers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.Local;
import javax.naming.NamingException;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Interfejs dla KontoManager
 */
@Local
public interface KontoManagerLocal {
    
    /**
     * Metoda zmienia hasło obecnie zalogowanego użytkownika, pod warunkiem:
     * konto należy do tego użytkownika, stareHaslo zgadza się z stanem w bazie.
     * @param konto     konto obecnie zalogowanego użytkownika
     * @param noweHaslo  nowe hasło w postaci jawnej
     * @param stareHaslo stare hasło w w postaci jawnej
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     */
    void zmienMojeHaslo(Konto konto, String noweHaslo, String stareHaslo) throws WyjatekSystemu, NoSuchAlgorithmException,UnsupportedEncodingException ;

    /**
     * Metoda zmienia hasło podanego konta
     * @param konto     konto, które ma mieć zmienione hasło
     * @param noweHaslo nowe hasło
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    void zmienHaslo(Konto konto, String noweHaslo) throws NoSuchAlgorithmException,UnsupportedEncodingException;
    
    /**
     * Metoda wprowadza do systemu konto klienta (niepotwierdzone)
     * @param konto informacje kontcie do utworzenia
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    void rejestrujKontoKlienta(Konto konto) throws NoSuchAlgorithmException,UnsupportedEncodingException, NamingException;
    
    /**
     * Metoda wprowadza do systemu konto o dowolnym, niewykluczajacym sie poziomie dostepu
     * @param konto informacje kontcie do utworzenia
     * @param poziomyDostepu lista poziomów dostępu jakie beda przypisane
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    void utworzKonto(Konto konto, List<String> poziomyDostepu) throws NoSuchAlgorithmException,UnsupportedEncodingException, NamingException, WyjatekSystemu;
    
    
    /**
     * Metoda zwracająca liste kont podobnych do zadanego konta
     * @param konto     obiekt zawierający kryteria wyszukania
     * @return          lista podobnych kont
     */
    public List<Konto> znajdzPodobne(Konto konto);
    
    
    /**
     * Metoda zwracająca liste wszystkich kont
     * @return          lista wszystkich kont
     */
    public List<Konto> pobierzWszystkie();
    
    /**
     * Metoda dodająca dany poziom dostępu do konta
     * @param konto     konto do którego należy dodać poziom dostępu
     * @param poziom    nazwa poziomu dostępu
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    public void dodajPoziomDostepu(Konto konto, String poziom) throws WyjatekSystemu, NamingException;
    
    /**
     * Metoda odłączająca dany poziom dostępu do konta
     * @param konto     konto od którego należy odłączyć poziom dostępu
     * @param poziom    nazwa poziomu dostępu
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    public void odlaczPoziomDostepu(Konto konto, String poziom) throws WyjatekSystemu, NamingException;
}
