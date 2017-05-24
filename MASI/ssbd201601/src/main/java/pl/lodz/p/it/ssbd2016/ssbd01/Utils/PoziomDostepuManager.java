package pl.lodz.p.it.ssbd2016.ssbd01.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.PoziomDostepu;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Klasa użytkowa definiująca dostępne poziomy dostępu. Udostępnia metody tworzenia,
 * pobierania i sprawdzania poziomów dostępu.
 */
public class PoziomDostepuManager {
    
    private List<String> poziomyDostepu;
    private List<List<String>> poprawneKombinacjePoziomowDostepu;
    
    /**
     * Konstruktor inicjalizujący listy
     * @throws javax.naming.NamingException
     */
    public PoziomDostepuManager() throws NamingException{    
        poziomyDostepu = dodajPoziomyDostepu();
        poprawneKombinacjePoziomowDostepu = dodajPoprawneKombinacjePoziomowDostepu();
    }
    
    /**
     * Metoda definiująca poziomy dostępu
     * @return      lista nazw poziomu dostepu
     */
    private List<String> dodajPoziomyDostepu() throws NamingException{
        List<String> nowePoziomy;
        String poziomyDostepuDoParsowania = null;
        Context ctx = new InitialContext();
        poziomyDostepuDoParsowania = (String) ctx.lookup("java:comp/env/PoziomyDostepu");
        
        nowePoziomy = new ArrayList<String>(Arrays.asList(poziomyDostepuDoParsowania.split(";")));
        
        return nowePoziomy;
    }

    /**
     * Metoda defniująca dostępne kombinacje poziomów dostępu
     * @return lista poprawnych kombinacji poziomów dostępu
     */
    private List<List<String>> dodajPoprawneKombinacjePoziomowDostepu() throws NamingException{
        List<List<String>> nowePoprawneKombinacjePoziomowDostepu = new ArrayList<List<String>>();
        String kombinacjePoziomowDostepuDoParsowania = null;
        List<String> pojedynczaKombinacjaDoParsowania = null;
        
        Context ctx = new InitialContext();
        kombinacjePoziomowDostepuDoParsowania = (String) ctx.lookup("java:comp/env/PoprawneKombinacjePoziomowDostepu");

        for (String kombinacja: kombinacjePoziomowDostepuDoParsowania.split(";")) {
            nowePoprawneKombinacjePoziomowDostepu.add(new ArrayList<String>(Arrays.asList(kombinacja.split(","))));
        }
        
        return nowePoprawneKombinacjePoziomowDostepu;
    }
       
    /**
     * Metoda tworząca poziomy dostępu z określonego zbioru wartości
     * @param poziom    nazwa poziomu dostepu
     * @return          nowy obiekt o określonej nazwie
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu
     */
    public PoziomDostepu stworzPoziomDostepu(String poziom) throws WyjatekSystemu{
        if (!poziomyDostepu.contains(poziom)) {
            throw new WyjatekSystemu("blad.poziomDostepuNieIstnieje"+poziom,"MOK");
        }

        PoziomDostepu nowyPoziomDostepu = new PoziomDostepu();
        nowyPoziomDostepu.setPoziom(poziom);
        
        return nowyPoziomDostepu;
    }
        
    /**
     * Metoda sprawdzająca warunki dodania poziomów dostępu
     * @param konto         konto do którego chcemy dodać poziom
     * @param nowyPoziom    nazwa poziomu, który chcemy dodać
     * @return              boolean określający decyzję czy można dodać poziom
     */
    public boolean czyMoznaDodacPoziom(Konto konto, String nowyPoziom) {
        List<String> poziomyAktywne = new ArrayList<String>();
        
        for (PoziomDostepu poziom:konto.getPoziomDostepuCollection()) {
            if (poziom.getAktywny()) {
                poziomyAktywne.add(poziom.getPoziom());
            }
        }
        // Jesli dany poziom juz posiadamy
        if (poziomyAktywne.contains(nowyPoziom)) {
            return false;
        }
        
        poziomyAktywne.add(nowyPoziom);
        // Jesli jestesmy adminem lub klientem - nie mozemy miec innych poziomow
        if (!czyPoprawnaKombinacjaPoziomowDostepu(poziomyAktywne)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Metoda sprawdzająca podana lista poziomów dostępu zawiera poprawną 
     * kombinację poziomów dostępu
     * @param poziomyDostepu lista poziomów dostępu
     * @return decyzja czy lista jest poprawna
     */
    public boolean czyPoprawnaKombinacjaPoziomowDostepu(List<String> poziomyDostepu) {
        if (poziomyDostepu.size() == 1) {
            return true;
        } else if (poziomyDostepu.size() > 1) {
            for (List<String> kombinacja:poprawneKombinacjePoziomowDostepu) {
                if (poziomyDostepu.size() == kombinacja.size() && kombinacja.containsAll(poziomyDostepu)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Tworzy poziom dostepu Klient
     * @return zwraca poziom dostepu
     */
    public PoziomDostepu stworzPoziomDostepuKlient() {
        
        PoziomDostepu nowyPoziomDostepu = new PoziomDostepu();
        nowyPoziomDostepu.setPoziom(poziomyDostepu.get(3));
        
        return nowyPoziomDostepu;
    }
    
    /*
     * Metoda sprawdzająca czy dane konto posiada poziom dostępu
     * @param konto         konto dla którego sprawdzamy poziomy dostępu
     * @param poziom        nazwa poziomu dostępu
     * @return              boolean określający czy posiadamy dany poziom dostępu
     */
    public boolean czyPosiadaPoziomDostepu(Konto konto, String poziom) {
        for (PoziomDostepu obecnyPoziom:konto.getPoziomDostepuCollection()) {
            if (obecnyPoziom.getPoziom().equals(poziom)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Metoda sprawdzająca czy dane konto posiada aktywny poziom dostępu, przed 
     * wykonaniem tej metody powiniśmy sprawdzić czy dany poziom dostępu posiadamy.
     * @param konto         konto dla którego sprawdzamy poziomy dostępu
     * @param poziom        nazwa poziomu dostępu
     * @return              boolean określający czy posiadamy aktywny poziom dostępu
     */
    public boolean czyPosiadaAktywnyPoziomDostepu(Konto konto, String poziom) {
        for (PoziomDostepu obecnyPoziom:konto.getPoziomDostepuCollection()) {
            if (obecnyPoziom.getPoziom().equals(poziom)) {
                return obecnyPoziom.getAktywny();
            }
        }
        return false;
    }
    
    /**
     * Metoda zwracająca poziom dostępu na podstawie nazwy, przed wykonaniem
     * tej metody powiniśmy sprawdzić czy dany poziom dostępu posiadamy.
     * @param konto         konto z którego pobieramy poziom dostępu
     * @param poziom        nazwa poziomu dostępu
     * @return              obiekt poziomu dostępu
     */
    public PoziomDostepu pobierzPoziomDostepu(Konto konto, String poziom) {
        for (PoziomDostepu obecnyPoziom:konto.getPoziomDostepuCollection()) {
            if (obecnyPoziom.getPoziom().equals(poziom)) {
                return obecnyPoziom;
            }
        }
        return null;
    }
    
    // Gettery i Settery
        
    public List<String> getPoziomyDostepu() {
        return new ArrayList<String>(poziomyDostepu);
    }
}
