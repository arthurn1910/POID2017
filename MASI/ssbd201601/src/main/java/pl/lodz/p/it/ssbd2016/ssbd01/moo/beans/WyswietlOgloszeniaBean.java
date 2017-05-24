package pl.lodz.p.it.ssbd2016.ssbd01.moo.beans;

import java.util.*;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;


/**
 * Obsługuje widok wyświetlania ogłoszeń
 */
@Named
@RequestScoped
public class WyswietlOgloszeniaBean {
    
    @Inject
    private OgloszenieSession ogloszenieSession;
    
    /*
    Stworzył: Maksymilian Zgierski
    Przypadek użycia: MOO.9 - Przeglądaj ogłoszenia posortowane
    
    Poniższe pola będą użyte w formularzu JSF i będą określały według jakich kryteriów przeprowadzić
    sortowanie w metodzie sortujOgloszenia
    */
    private String sortuj;
    
    public String getSortuj() {
        return this.sortuj;
    }    
    public void setSortuj(String s) {
        this.sortuj = s;
    }
    
    /**
     * Wyświetla ogłoszenia posortowane
     */
    public void sortujOgloszenia() {
        if(sortuj == null)
            return;
        List<Ogloszenie> ogloszenia = ogloszenieSession.getOgloszeniaDataModel();
        Collections.sort(ogloszenia, new Comparator() {
            public int compare(Object o1, Object o2) {
                Ogloszenie x1 = (Ogloszenie) o1;
                Ogloszenie x2 = (Ogloszenie) o2;
                if(sortuj.equals("cena") == true) {
                    int sComp = 0;
                    if(x1.getCena() > x2.getCena())
                        sComp = 1;
                    else if(x1.getCena() < x2.getCena())
                        sComp = -1;
                    return sComp;
                }
                else if(sortuj.equals("dataDodania") == true) {
                    int sComp = 0;
                    if(x1.getDataDodania().after(x2.getDataDodania()))
                        sComp = 1;
                    else if(x1.getDataDodania().before(x2.getDataDodania()))
                        sComp = -1;
                    return sComp;
                }
                else if(sortuj.equals("typOgloszenia") == true) {
                    int sComp = x1.getTypOgloszenia().getNazwa().compareTo(x2.getTypOgloszenia().getNazwa());
                    return sComp;
                }
                else if(sortuj.equals("rynekPierwotny") == true) {
                    int sComp = 0;
                    if(x1.getRynekPierwotny() == true && x2.getRynekPierwotny() == false)
                        sComp = -1;
                    else if(x1.getRynekPierwotny() == false && x2.getRynekPierwotny() == true)
                        sComp = 1;
                    return sComp;
                }
                return 1;
            }
        });
        ogloszenieSession.setOgloszeniaDataModel(ogloszenia);
    }
    
    /**
     * Metoda odświeża listę ogłoszeń
     */
    public void odswiez() {
        ogloszenieSession.pobierzWszystkieOgloszenia();
    }
    
    /**
     * Metoda ustawiająca w ogloszenieSession ogłoszenie do wysiwetlenia
     * @return 
     */
    public String wyswietlSzczegolyOgloszenia(Ogloszenie o) {
        ogloszenieSession.setOgloszenieDoWyswietlenia(o);
        return "wyswietlSzczegolyOgloszenia";
    }
    
    /**
     * Metoda odpowiada za przekazanie do widoku listy z ogłoszeniami
     * @return lista ogłoszeń
     */
    public List<Ogloszenie> getOgloszeniaDataModel() {
        return ogloszenieSession.getOgloszeniaDataModel();
    } 
}