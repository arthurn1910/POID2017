package pl.lodz.p.it.ssbd2016.ssbd01.mok.beans;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.HistoriaLogowania;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.mok.endpoints.MOKEndpointLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.DataModel;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Ziarno zarządzające sesją użytkownika. Udostępnia API dla widoku.
 */
@SessionScoped
@ManagedBean(name = "uzytkownikSession")
public class UzytkownikSession implements Serializable {

    @EJB
    private MOKEndpointLocal MOKEndpoint;

    private Konto wybraneKonto;

    private Konto kontoEdytuj;

    private DataModel<Konto> kontaDataModel;

    private String login;

    private boolean czyWyswietlicPotwierdzenie;
    private boolean czyZapisany = false;

    /**
     * MOK.16 logujemy zalogowanego użytkownika w historii logowania
     */

    public void zapiszIP(String login) {

        if (!czyZapisany) {
            MOKEndpoint.ustawIP(login);
            czyWyswietlicPotwierdzenie = true;
            czyZapisany = true;
        }
    }

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * Rejestruje konto, nadając mu poziom dostępu klienta
     *
     * @param k konto, które ma zostać zarejestrowane
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     * @throws javax.naming.NamingException
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws javax.mail.MessagingException
     */
    public void rejestrujKontoKlienta(Konto k) throws WyjatekSystemu, NoSuchAlgorithmException, UnsupportedEncodingException, NamingException, MessagingException {
        try {
            Konto kontoRejestracja = new Konto();
            kontoRejestracja.setLogin(k.getLogin());
            kontoRejestracja.setHaslo(k.getHaslo());
            kontoRejestracja.setImie(k.getImie());
            kontoRejestracja.setNazwisko(k.getNazwisko());
            kontoRejestracja.setEmail(k.getEmail());
            kontoRejestracja.setDataUtworzenia(Date.from(Instant.now()));
            kontoRejestracja.setTelefon(k.getTelefon());
            MOKEndpoint.rejestrujKontoKlienta(kontoRejestracja);
            czyWyswietlicPotwierdzenie = true;
        } catch (Exception ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Rejestruje konto, nadając mu wybrane poizomy dostępu
     *
     * @param k              konto, które ma zostać zarejestrowane
     * @param poziomyDostepu poziomy dostępu, który ma mieć nowo tworzone konto
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     * @throws javax.naming.NamingException
     */
    public void utworzKonto(Konto k, List<String> poziomyDostepu) throws WyjatekSystemu, NoSuchAlgorithmException, UnsupportedEncodingException, NamingException {
        try {
            Konto kontoRejestracja = new Konto();
            kontoRejestracja.setLogin(k.getLogin());
            kontoRejestracja.setHaslo(k.getHaslo());
            kontoRejestracja.setImie(k.getImie());
            kontoRejestracja.setNazwisko(k.getNazwisko());
            kontoRejestracja.setEmail(k.getEmail());
            kontoRejestracja.setDataUtworzenia(Date.from(Instant.now()));
            kontoRejestracja.setTelefon(k.getTelefon());

            MOKEndpoint.utworzKonto(kontoRejestracja, poziomyDostepu);
            czyWyswietlicPotwierdzenie = true;
        } catch (Exception ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Metoda zmienia stan konta na potwierdzone
     *
     * @param konto konto do potwierdzenia
     */
    public void potwierdzKonto(Konto konto) {
        MOKEndpoint.potwierdzKonto(konto);
        czyWyswietlicPotwierdzenie = true;
    }

    /**
     * Metoda oblokowuje konto
     *
     * @param konto konto do odblokowania
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws javax.mail.MessagingException
     */
    public void odblokujKonto(Konto konto) throws WyjatekSystemu, MessagingException {
        try {
            MOKEndpoint.odblokujKonto(konto);
            czyWyswietlicPotwierdzenie = true;
        } catch (Exception ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Metoda blokująca konto
     *
     * @param konto konto do zablokowania
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws javax.mail.MessagingException
     */
    public void zablokujKonto(Konto konto) throws WyjatekSystemu, MessagingException {
        try {
            MOKEndpoint.zablokujKonto(konto);
            czyWyswietlicPotwierdzenie = true;
        } catch (Exception ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Metoda zwraca konto o zadanym loginie
     *
     * @param login login konta
     * @return szukane konto
     */
    public Konto znajdzPoLoginie(String login) {
        return MOKEndpoint.znajdzPoLoginie(login);
    }

    /**
     * Metoda pobierająca konto do edycji. Zapewnia blokadę optymistyczną.
     *
     * @param konto konto do edycji
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public void pobierzKontoDoEdycji(Konto konto) throws WyjatekSystemu, IOException, ClassNotFoundException {
        try {
            setKontoEdytuj(MOKEndpoint.pobierzKontoDoEdycji(konto));
        } catch (IOException | ClassNotFoundException ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Metoda zapisuje zmienione konto. Sprawdzana jest blokada optymistyczna
     *
     * @throws Exception
     */
    void zapiszSwojeKontoPoEdycji() throws WyjatekSystemu {
        try {
            MOKEndpoint.zapiszSwojeKontoPoEdycji(kontoEdytuj);
            czyWyswietlicPotwierdzenie = true;
        } catch (Exception ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Metoda zapisuje zmienione konto. Sprawdzana jest blokada optymistyczna
     *
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    public void zapiszKontoPoEdycji() throws WyjatekSystemu {
        try {
            MOKEndpoint.zapiszKontoPoEdycji(kontoEdytuj);
            czyWyswietlicPotwierdzenie = true;
        } catch (Exception ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Metoda zmienia hasło obecnie zalogowanego użytkownika. Sprawdzana jest blokada
     * optymistyczna
     *
     * @param noweHaslo  nowe hasło w postaci jawnej
     * @param stareHaslo stare hasło w postaci jawnej
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     */
    public void zmienMojeHaslo(String noweHaslo, String stareHaslo) throws WyjatekSystemu, NoSuchAlgorithmException, UnsupportedEncodingException {
        try {
            MOKEndpoint.zmienMojeHaslo(noweHaslo, stareHaslo);
            czyWyswietlicPotwierdzenie = true;
        } catch (Exception ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Metoda zmienia hasło obecnie edytowanego konta. Sprawdzana jest blokada
     * optymistyczna
     *
     * @param noweHaslo nowe hasło w postaci jawnej
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     */
    public void zmienHaslo(String noweHaslo) throws WyjatekSystemu, NoSuchAlgorithmException, UnsupportedEncodingException {
        try {
            MOKEndpoint.zmienHaslo(noweHaslo);
            czyWyswietlicPotwierdzenie = true;
        } catch (Exception ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Metoda zwraca liste kont podobnych do danego konta
     *
     * @param konto konto zawierające kryteria wyszukania
     * @return lista kont podobnych
     */
    List<Konto> pobierzPodobneKonta(Konto konto) {
        return MOKEndpoint.pobierzPodobneKonta(konto);
    }

    List<Konto> pobierzWszystkieKonta() {
        return MOKEndpoint.pobierzWszystkieKonta();
    }

    /**
     * Metoda dodająca poziom dostępu do konta
     *
     * @param konto  konto do którego należy dodać poziom dostępu
     * @param poziom nazwa poziomu dostępu
     */
    void dodajPoziomDostepu(Konto konto, String poziom) throws WyjatekSystemu, NamingException {
        try {
            MOKEndpoint.dodajPoziomDostepu(konto, poziom);
            czyWyswietlicPotwierdzenie = true;
        } catch (Exception ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Metoda odłączająca poziom dostępu do konta
     *
     * @param konto  konto od którego należy odłączyć poziom dostępu
     * @param poziom nazwa poziomu dostępu
     */
    void odlaczPoziomDostepu(Konto konto, String poziom) throws WyjatekSystemu, NamingException {
        try {
            MOKEndpoint.odlaczPoziomDostepu(konto, poziom);
            czyWyswietlicPotwierdzenie = true;
        } catch (Exception ex) {
            this.exception = ex;
            throw ex;
        }
    }

    // Gettery i Settery

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

    public List<HistoriaLogowania> pobierzHistorieLogowanUzytkownikow() {
        return MOKEndpoint.pobierzHistorieLogowanUzytkownikow();
    }

    public void setKontoEdytuj(Konto kontoEdytuj) {
        this.kontoEdytuj = kontoEdytuj;
    }

    /**
     * Metoda pobierająca wybrane konto do edycji
     *
     * @return
     * @throws WyjatekSystemu rzucany gdy uprzednio nie wybrano konta do edycji
     */
    public Konto getKontoEdytuj() throws WyjatekSystemu {
        if (kontoEdytuj == null) {
            WyjatekSystemu ex = new WyjatekSystemu("blad.brakKontaDoEdycji","MOK");
            this.setException(ex);
            throw ex;
        }

        return kontoEdytuj;
    }

    /**
     * Metoda pobierająca wybrane konto do wyświetlenia
     *
     * @return
     * @throws WyjatekSystemu rzucany gdy uprzednio nie wybrano konta do wyświetlenia
     */
    public Konto getWybraneKonto() throws WyjatekSystemu {
        Konto tmp = MOKEndpoint.znajdzPoLoginie(wybraneKonto.getLogin());
        if (tmp.getId() == null) {
            WyjatekSystemu ex = new WyjatekSystemu("blad.NullPointerException","MOK");
            this.setException(ex);
            throw ex;
        }
        return tmp;
    }

    public void setWybraneKonto(Konto wybraneKonto) {
        this.wybraneKonto = wybraneKonto;
    }

    public Konto getSwojeKonto() throws WyjatekSystemu {
        try {
            wybraneKonto = MOKEndpoint.getSwojeKonto();
        } catch (WyjatekSystemu ex) {
            this.exception = ex;
            throw ex;
        }
        return wybraneKonto;
    }

    public DataModel<Konto> getKontaDataModel() {
        return kontaDataModel;
    }

    public void setKontaDataModel(DataModel<Konto> kontaDataModel) throws WyjatekSystemu {
        try {
            this.kontaDataModel = kontaDataModel;
        } catch (Exception ex) {
            WyjatekSystemu exc = new WyjatekSystemu("blad.EJBAccessExcpetion","MOK");
            this.exception = exc;
            throw exc;
        }
    }
}
