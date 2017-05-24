package pl.lodz.p.it.ssbd2016.ssbd01.mok.beans;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import java.util.ArrayList;
import java.util.List;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Obiekty tej klasy są wykorzystywane do wyszukiwania kont użytkowników
 */
@Named
@RequestScoped
public class WyszukajKontaBean {
    
    @Inject
    private UzytkownikSession uzytkownikSession;
        
    private String imie;
    private String nazwisko;
    private String email;
    private String telefon;
    private boolean aktywne;
    private boolean potwierdzone;
    
    /**
     * Metoda wywoływana zaraz po stworzeniu obiektu. Inicjalizuje pola
     * aktywne i potwierdzone
     */
    @PostConstruct
    public void init() {
        setAktywne(true);
        setPotwierdzone(true);
    }
      
    /**
     * Metoda wyszukująca konta według podanego kryterium i ustawia otrzymaną listę
     * w uzytkownikSession umożliwiając wyświetlenie kont.
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    public void pobierzPodobneKonta() throws WyjatekSystemu {
        Konto konto = new Konto();
        if (!"".equals(imie))
            konto.setImie(imie);
        if (!"".equals(nazwisko))
            konto.setNazwisko(nazwisko);
        if (!"".equals(email))
            konto.setEmail(email);
        if (!"".equals(telefon))
            konto.setTelefon(telefon);   
        konto.setAktywne(aktywne);
        konto.setPotwierdzone(potwierdzone);
        
        uzytkownikSession.setKontaDataModel(new ListDataModel<>(uzytkownikSession.pobierzPodobneKonta(konto)));
    }
    
    /**
     * Metoda odpowiada za automatyczne uzupełnianie pola imie w formularzu
     * @param query dane, które aktualnie są wpisane w formularzu
     * @return lista imion, które rozpoczynają się od frazy wpisanej w formularzu
     */
    public List<String> completeImie(String query) {
        List<String> results = new ArrayList();
        List<Konto> konta = uzytkownikSession.pobierzWszystkieKonta();
        for(int i = 0; i < konta.size(); i++) {
            if(konta.get(i).getImie().length() >= query.length()) {
                if(konta.get(i).getImie().substring(0, query.length()).equals(query) == true) {
                    boolean istnieje = false;
                    for(int j = 0; j < results.size(); j++)
                        if(results.get(j).equals(konta.get(i).getImie()) == true)
                        {
                            istnieje = true;
                            break;
                        }
                    if(istnieje == false)
                        results.add(konta.get(i).getImie());
                }
            }
        }
        return results;
    }
    
     /**
     * Metoda odpowiada za automatyczne uzupełnianie pola nazwisko w formularzu
     * @param query dane, które aktualnie są wpisane w formularzu
     * @return lista nazwisk, które rozpoczynają się od frazy wpisanej w formularzu
     */
    public List<String> completeNazwisko(String query) {
        List<String> results = new ArrayList();
        List<Konto> konta = uzytkownikSession.pobierzWszystkieKonta();
        for(int i = 0; i < konta.size(); i++) {
            if(konta.get(i).getNazwisko().length() >= query.length()) {
                if(konta.get(i).getNazwisko().substring(0, query.length()).equals(query) == true) {
                    boolean istnieje = false;
                    for(int j = 0; j < results.size(); j++)
                        if(results.get(j).equals(konta.get(i).getNazwisko()) == true)
                        {
                            istnieje = true;
                            break;
                        }
                    if(istnieje == false)
                        results.add(konta.get(i).getNazwisko());
                }
            }
        }
        return results;
    }
    
     /**
     * Metoda odpowiada za automatyczne uzupełnianie pola email w formularzu
     * @param query dane, które aktualnie są wpisane w formularzu
     * @return lista adresów email, które rozpoczynają się od frazy wpisanej w formularzu
     */
    public List<String> completeEmail(String query) {
        List<String> results = new ArrayList();
        List<Konto> konta = uzytkownikSession.pobierzWszystkieKonta();
        for(int i = 0; i < konta.size(); i++) {
            if(konta.get(i).getEmail().length() >= query.length()) {
                if(konta.get(i).getEmail().substring(0, query.length()).equals(query) == true) {
                    boolean istnieje = false;
                    for(int j = 0; j < results.size(); j++)
                        if(results.get(j).equals(konta.get(i).getEmail()) == true)
                        {
                            istnieje = true;
                            break;
                        }
                    if(istnieje == false)
                        results.add(konta.get(i).getEmail());
                }
            }
        }
        return results;
    }
    
     /**
     * Metoda odpowiada za automatyczne uzupełnianie pola telefon w formularzu
     * @param query dane, które aktualnie są wpisane w formularzu
     * @return lista telefonów, które rozpoczynają się od frazy wpisanej w formularzu
     */
    public List<String> completeTelefon(String query) {
        List<String> results = new ArrayList();
        List<Konto> konta = uzytkownikSession.pobierzWszystkieKonta();
        for(int i = 0; i < konta.size(); i++) {
            if(konta.get(i).getTelefon().length() >= query.length()) {
                if(konta.get(i).getTelefon().substring(0, query.length()).equals(query) == true) {
                    boolean istnieje = false;
                    for(int j = 0; j < results.size(); j++)
                        if(results.get(j).equals(konta.get(i).getTelefon()) == true)
                        {
                            istnieje = true;
                            break;
                        }
                    if(istnieje == false)
                        results.add(konta.get(i).getTelefon());
                }
            }
        }
        return results;
    }
    
    /**
     * Handler przyciksu wyświetl informacje w widoku. Ustawia wybrane konto
     * i przechodzi do odpowiendiej strony ze szczegółami
     * @return      przekierowanie do strony z szczegółami
     */
    public String wyswietlSzczegolyKonta() {    
        uzytkownikSession.setWybraneKonto(getKontaDataModel().getRowData());
        return "wyswietlSzczegolyKonta";
    }
        
    // Gettery i Settery
    
    public DataModel<Konto> getKontaDataModel() {
        return uzytkownikSession.getKontaDataModel();
    }
            
    public String getImie() {
        return imie;
    }  
    public void setImie(String imie) {
        this.imie = imie;
    }
    
    public String getNazwisko() {
        return nazwisko;
    }    
    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getEmail() {
        return email;
    }    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefon() {
        return telefon;
    }    
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public boolean isAktywne() {
        return aktywne;
    }
    public void setAktywne(boolean aktywne) {
        this.aktywne = aktywne;
    }

    public boolean isPotwierdzone() {
        return potwierdzone;
    }
    public void setPotwierdzone(boolean potwierdzone) {
        this.potwierdzone = potwierdzone;
    }
    
}
