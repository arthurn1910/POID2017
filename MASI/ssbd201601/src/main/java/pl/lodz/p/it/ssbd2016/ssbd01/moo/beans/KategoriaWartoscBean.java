package pl.lodz.p.it.ssbd2016.ssbd01.moo.beans;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.ElementWyposazeniaNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.KategoriaWyposazeniaNieruchomosci;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kamil Rogowski on 26.05.2016.
 * Zawiera obsługę wszystkich kategorii na formularzu dodawania ogłoszenia
 * Listy przechowuja wartosci kategorii w postai kluczy do slownika, klucze sciagane sa z bazy
 */
@RequestScoped
@Named
public class KategoriaWartoscBean {

    @Inject
    private OgloszenieSession ogloszenieSession;
    /**
     * Wszystkie możliwe elementy dla kategorii
     */
    private List<ElementWyposazeniaNieruchomosci> elementyKategorii;
    /**
     * Wszystkie możliwe  kategorie
     */
    private List<KategoriaWyposazeniaNieruchomosci> kategorie;
    /**
     * wszystkie wartości dla kategorii: rodzaj zabudowy
     */
    private List<ElementWyposazeniaNieruchomosci> rodzajZabudowy;
    /**
     * wszystkie wartości dla kategorii: materiał
     */
    private List<ElementWyposazeniaNieruchomosci> material;
    /**
     * wszystkie wartości dla kategorii: poddasze
     */
    private List<ElementWyposazeniaNieruchomosci> poddasze;
    /**
     * wszystkie wartości dla kategorii: dach
     */
    private List<ElementWyposazeniaNieruchomosci> dach;
    /**
     * wszystkie wartości dla kategorii: pokrycie dachu
     */
    private List<ElementWyposazeniaNieruchomosci> pokrycieDachu;
    /**
     * wszystkie wartości dla kategorii: stan wykonczenia
     */
    private List<ElementWyposazeniaNieruchomosci> stanWykonczenia;
    /**
     * wszystkie wartości dla kategorii: okna
     */
    private List<ElementWyposazeniaNieruchomosci> okna;
    /**
     * wszystkie wartości dla kategorii: polozenie
     */
    private List<ElementWyposazeniaNieruchomosci> polozenie;
    /**
     * wszystkie wartości dla kategorii: zabezpieczenia
     */
    private List<ElementWyposazeniaNieruchomosci> zabezpieczenia;

    /**
     * wszystkie wartości dla kategorii: ogrodzenie
     */
    private List<ElementWyposazeniaNieruchomosci> ogrodzenie;
    /**
     * wszystkie wartości dla kategorii: ogrzewanie
     */
    private List<ElementWyposazeniaNieruchomosci> ogrzewanie;
    /**
     * wszystkie wartości dla kategorii: media
     */
    private List<ElementWyposazeniaNieruchomosci> media;
    /**
     * wszystkie wartości dla kategorii: dojazd
     */
    private List<ElementWyposazeniaNieruchomosci> dojazd;
    /**
     * wszystkie wartości dla kategorii: okolica
     */
    private List<ElementWyposazeniaNieruchomosci> okolica;
    /**
     * wszystkie wartości dla kategorii: informacje dodatkowe
     */
    private List<ElementWyposazeniaNieruchomosci> informacjeDodatkowe;

    /**
     * inicjalizacja wszystkich list i pociecie ich wzgledem kategorii
     */
    @PostConstruct
    public void init() {
        elementyKategorii = ogloszenieSession.pobierzElementyKategorii();
        kategorie = ogloszenieSession.pobierzKategorie();
        rodzajZabudowy = filter(elementyKategorii, 1);
        material = filter(elementyKategorii, 2);
        poddasze = filter(elementyKategorii, 3);
        dach = filter(elementyKategorii, 4);
        pokrycieDachu = filter(elementyKategorii, 5);
        stanWykonczenia = filter(elementyKategorii, 6);
        okna = filter(elementyKategorii, 7);
        polozenie = filter(elementyKategorii, 8);
        zabezpieczenia = filter(elementyKategorii, 9);
        ogrodzenie = filter(elementyKategorii, 10);
        ogrzewanie = filter(elementyKategorii, 11);
        media = filter(elementyKategorii, 12);
        dojazd = filter(elementyKategorii, 13);
        okolica = filter(elementyKategorii, 14);
        informacjeDodatkowe = filter(elementyKategorii, 15);
    }

    /**
     * rozpakowuje wszystkie elementy kategorii
     *
     * @param listaElementow lista wszystkich elementow
     * @param idKategorii    id kategorii w tabeli
     * @return zwraca odfiltrowana liste wartosci dla kategorii
     */
    public List<ElementWyposazeniaNieruchomosci> filter(final List<ElementWyposazeniaNieruchomosci> listaElementow, int idKategorii) {

        return listaElementow.stream()
                .filter(elem -> elem.getIdKategorii().getId() == idKategorii)
                .collect(Collectors.toList());
    }

    public List<ElementWyposazeniaNieruchomosci> getElementyKategorii() {
        return elementyKategorii;
    }

    public List<ElementWyposazeniaNieruchomosci> getInformacjeDodatkowe() {
        return informacjeDodatkowe;
    }

    public void setInformacjeDodatkowe(List<ElementWyposazeniaNieruchomosci> informacjeDodatkowe) {
        this.informacjeDodatkowe = informacjeDodatkowe;
    }

    public List<ElementWyposazeniaNieruchomosci> getOkolica() {
        return okolica;
    }

    public void setOkolica(List<ElementWyposazeniaNieruchomosci> okolica) {
        this.okolica = okolica;
    }

    public List<ElementWyposazeniaNieruchomosci> getDojazd() {
        return dojazd;
    }

    public void setDojazd(List<ElementWyposazeniaNieruchomosci> dojazd) {
        this.dojazd = dojazd;
    }

    public List<ElementWyposazeniaNieruchomosci> getMedia() {
        return media;
    }

    public void setMedia(List<ElementWyposazeniaNieruchomosci> media) {
        this.media = media;
    }

    public List<ElementWyposazeniaNieruchomosci> getOgrzewanie() {
        return ogrzewanie;
    }

    public void setOgrzewanie(List<ElementWyposazeniaNieruchomosci> ogrzewanie) {
        this.ogrzewanie = ogrzewanie;
    }

    public List<ElementWyposazeniaNieruchomosci> getZabezpieczenia() {
        return zabezpieczenia;
    }

    public void setZabezpieczenia(List<ElementWyposazeniaNieruchomosci> zabezpieczenia) {
        this.zabezpieczenia = zabezpieczenia;
    }

    public List<ElementWyposazeniaNieruchomosci> getPolozenie() {
        return polozenie;
    }

    public void setPolozenie(List<ElementWyposazeniaNieruchomosci> polozenie) {
        this.polozenie = polozenie;
    }

    public List<ElementWyposazeniaNieruchomosci> getOgrodzenie() {
        return ogrodzenie;
    }

    public void setOgrodzenie(List<ElementWyposazeniaNieruchomosci> ogrodzenie) {
        this.ogrodzenie = ogrodzenie;
    }

    public List<ElementWyposazeniaNieruchomosci> getStanWykonczenia() {
        return stanWykonczenia;
    }

    public void setStanWykonczenia(List<ElementWyposazeniaNieruchomosci> stanWykonczenia) {
        this.stanWykonczenia = stanWykonczenia;
    }

    public List<ElementWyposazeniaNieruchomosci> getOkna() {
        return okna;
    }

    public void setOkna(List<ElementWyposazeniaNieruchomosci> okna) {
        this.okna = okna;
    }

    public List<ElementWyposazeniaNieruchomosci> getPokrycieDachu() {
        return pokrycieDachu;
    }

    public void setPokrycieDachu(List<ElementWyposazeniaNieruchomosci> pokrycieDachu) {
        this.pokrycieDachu = pokrycieDachu;
    }

    public List<ElementWyposazeniaNieruchomosci> getDach() {
        return dach;
    }

    public void setDach(List<ElementWyposazeniaNieruchomosci> dach) {
        this.dach = dach;
    }

    public List<ElementWyposazeniaNieruchomosci> getPoddasze() {
        return poddasze;
    }

    public void setPoddasze(List<ElementWyposazeniaNieruchomosci> poddasze) {
        this.poddasze = poddasze;
    }

    public List<ElementWyposazeniaNieruchomosci> getMaterial() {
        return material;
    }

    public void setMaterial(List<ElementWyposazeniaNieruchomosci> material) {
        this.material = material;
    }

    public List<ElementWyposazeniaNieruchomosci> getRodzajZabudowy() {
        return rodzajZabudowy;
    }

    public void setRodzajZabudowy(List<ElementWyposazeniaNieruchomosci> rodzajZabudowy) {
        this.rodzajZabudowy = rodzajZabudowy;
    }

}
