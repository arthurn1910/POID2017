package pl.lodz.p.it.ssbd2016.ssbd01.moo.beans;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.*;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Obsługuje widok dla dodawania ogłoszenia
 */
@ManagedBean
@RequestScoped
public class DodajOgloszenieBean {

    @Inject
    private OgloszenieSession ogloszenieSession;
    /**
     * wstrzykniecie zapewnnia komunikacje z KategoriaWartoscBean
     */
    @Inject
    private KategoriaWartoscBean kategoriaWartoscBean;
    /**
     * wstrzykniecie zapewnnia komunikacje z TypyOgloszenNieruchomosciBean
     */
    @Inject
    private TypyOgloszenNieruchomosciBean typyOgloszenNieruchomosciBean;
    /**
     * Forma dla nowego ogloszenia
     */
    private Ogloszenie ogloszenie = new Ogloszenie();

    /**
     * Forma dla nowej nieruchomosci
      */
    private Nieruchomosc nieruchomosc = new Nieruchomosc();

    /**
     * Ponizej wybrane przez uzytkownika wartosci kategorii z selectboxow, wartoscia jest klucz w postaci stringa
     * w listach, znajduja sie wartosci z multi selectboxow
     */
    private String wybranyTypOgloszenia;
    private String wybranyTypNieruchomosci;
    private String wybranyRodzajZabudowy;
    private String wybranyMaterial;
    private String wybranePoddasze;
    private String wybranyDach;
    private String wybranePokrycieDachu;
    private String wybranyStanWykonczenia;
    private String wybraneOkno;
    private String wybranePolozenie;
    private String wybraneOgrodzenie;
    private String wybraneOgrzewanie;
    private String wybraneDojazd;
    private String wybranaOkolica;
    private List<String> wybraneZabezpieczenia = new ArrayList<>();
    private List<String> wybraneInfoDodatkowe = new ArrayList<>();
    private List<String> wybraneMedia = new ArrayList<>();

    private List<ElementWyposazeniaNieruchomosci> elementyKategorii;

    /**
     * MOO. 1 Dodaje ogłoszenie dla nieruchomości, Kamil Rogowski
     *
     * @return index
     */
    public String dodajOgloszenie() {

        elementyKategorii = kategoriaWartoscBean.getElementyKategorii();
        final List<ElementWyposazeniaNieruchomosci> elementyWyposazeniaNieruchomosci = konwertujZaznaczoneNaElementWyposazeniaNieruchomosci();
        final List<TypNieruchomosci> typyNieruchomosci = typyOgloszenNieruchomosciBean.getTypyNieruchomosci();
        final List<TypOgloszenia> typyOgloszen = typyOgloszenNieruchomosciBean.getTypyOgloszen();
        nieruchomosc.setTypNieruchomosci(odfiltrujZaznaczonyTypNieruchomosci(typyNieruchomosci));
        ogloszenie.setTypOgloszenia(odfiltrujZaznaczonyTypOgloszenia(typyOgloszen));
        ogloszenieSession.dodajOgloszenie(ogloszenie, nieruchomosc, elementyWyposazeniaNieruchomosci);

        return "index";
    }

    /**
     * Przepakowuje zaznaczone wartosci kategorii, ktore sa Stringami na ElementWyposazeniaNieruchomosci
     *
     * @return lista zaznaczonych wartosci dla kategorii
     */
    private List<ElementWyposazeniaNieruchomosci> konwertujZaznaczoneNaElementWyposazeniaNieruchomosci() {
        List<ElementWyposazeniaNieruchomosci> listaElementow = new ArrayList<>();
        int i = 1, j = 1, k = 1;
        for (ElementWyposazeniaNieruchomosci anElementyKategorii : elementyKategorii) {

            if (Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybranyRodzajZabudowy))) {
                listaElementow.add(anElementyKategorii);
            } else if (Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybranyRodzajZabudowy))) {
                listaElementow.add(anElementyKategorii);
            } else if (Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybranyMaterial))) {
                listaElementow.add(anElementyKategorii);
            } else if (Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybranePoddasze))) {
                listaElementow.add(anElementyKategorii);
            } else if (Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybranyDach))) {
                listaElementow.add(anElementyKategorii);
            } else if (Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybranyStanWykonczenia))) {
                listaElementow.add(anElementyKategorii);
            } else if (Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybraneOkno))) {
                listaElementow.add(anElementyKategorii);
            } else if (Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybranePolozenie))) {
                listaElementow.add(anElementyKategorii);
            } else if (Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybraneOgrodzenie))) {
                listaElementow.add(anElementyKategorii);
            } else if (Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybraneOgrzewanie))) {
                listaElementow.add(anElementyKategorii);
            } else if (Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybraneDojazd))) {
                listaElementow.add(anElementyKategorii);
            } else if (Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybranaOkolica))) {
                listaElementow.add(anElementyKategorii);
            } else if (wybraneMedia.size() > 0 && Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybraneMedia.get(i - 1)))) { //glupie ale dziala
                if (i < wybraneMedia.size())
                    i++;
                listaElementow.add(anElementyKategorii);
            } else if (wybraneZabezpieczenia.size() > 0 && Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybraneZabezpieczenia.get(j - 1)))) {
                if (j < wybraneZabezpieczenia.size())
                    j++;
                listaElementow.add(anElementyKategorii);
            } else if (wybraneInfoDodatkowe.size() > 0 && Objects.equals(anElementyKategorii.getId(), Long.valueOf(wybraneInfoDodatkowe.get(k - 1)))) {
                if (k < wybraneInfoDodatkowe.size())
                    k++;
                listaElementow.add(anElementyKategorii);
            }
        }
        return listaElementow;
    }

    /**
     * Wybiera zaznaczony przez uzytkownika typ ogloszenia
     * @param typyOgloszenia wszystkie predefeniowane typy ogloszen
     * @return zwraca objekt TypOgloszenia
     */
    private TypOgloszenia odfiltrujZaznaczonyTypOgloszenia(List<TypOgloszenia> typyOgloszenia) {
        for (TypOgloszenia typOgloszenia : typyOgloszenia) {
            if (Objects.equals(typOgloszenia.getId(), Integer.valueOf(wybranyTypOgloszenia)))
                return typOgloszenia;
        }
        return null;
    }

    /**
     * Wybiera zaznaczony przez uzytkownika typ nieruchomosci
     * @param typyNieruchomosci wszystkie predefeniowane typy nieruchomosci
     * @return zwraca objekt TypNieruchomosci
     */
    private TypNieruchomosci odfiltrujZaznaczonyTypNieruchomosci(List<TypNieruchomosci> typyNieruchomosci) {
        for (TypNieruchomosci typNieruchomosci : typyNieruchomosci) {
            if (Objects.equals(typNieruchomosci.getId(), Integer.valueOf(wybranyTypNieruchomosci)))
                return typNieruchomosci;
        }
        return null;

    }

    public String getWybranyTypOgloszenia() {
        return wybranyTypOgloszenia;
    }

    public void setWybranyTypOgloszenia(String wybranyTypOgloszenia) {
        this.wybranyTypOgloszenia = wybranyTypOgloszenia;
    }

    public String getWybranyTypNieruchomosci() {
        return wybranyTypNieruchomosci;
    }

    public void setWybranyTypNieruchomosci(String wybranyTypNieruchomosci) {
        this.wybranyTypNieruchomosci = wybranyTypNieruchomosci;
    }

    public Ogloszenie getOgloszenie() {
        return ogloszenie;
    }

    public Nieruchomosc getNieruchomosc() {
        return nieruchomosc;
    }

    public String getWybranyMaterial() {
        return wybranyMaterial;
    }

    public void setWybranyMaterial(String wybranyMaterial) {
        this.wybranyMaterial = wybranyMaterial;
    }

    public String getWybranyRodzajZabudowy() {
        return wybranyRodzajZabudowy;
    }

    public void setWybranyRodzajZabudowy(String wybranyRodzajZabudowy) {
        this.wybranyRodzajZabudowy = wybranyRodzajZabudowy;
    }

    public String getWybranaOkolica() {
        return wybranaOkolica;
    }

    public String getWybranePolozenie() {
        return wybranePolozenie;
    }

    public void setWybranePolozenie(String wybranePolozenie) {
        this.wybranePolozenie = wybranePolozenie;
    }

    public String getWybraneOkno() {
        return wybraneOkno;
    }

    public void setWybraneOkno(String wybraneOkno) {
        this.wybraneOkno = wybraneOkno;
    }

    public String getWybranyStanWykonczenia() {
        return wybranyStanWykonczenia;
    }

    public void setWybranyStanWykonczenia(String wybranyStanWykonczenia) {
        this.wybranyStanWykonczenia = wybranyStanWykonczenia;
    }

    public String getWybranePokrycieDachu() {
        return wybranePokrycieDachu;
    }

    public void setWybranePokrycieDachu(String wybranePokrycieDachu) {
        this.wybranePokrycieDachu = wybranePokrycieDachu;
    }

    public String getWybranyDach() {
        return wybranyDach;
    }

    public void setWybranyDach(String wybranyDach) {
        this.wybranyDach = wybranyDach;
    }

    public String getWybranePoddasze() {
        return wybranePoddasze;
    }

    public void setWybranePoddasze(String wybranePoddasze) {
        this.wybranePoddasze = wybranePoddasze;
    }

    public void setWybranaOkolica(String wybranaOkolica) {

        this.wybranaOkolica = wybranaOkolica;
    }

    public String getWybraneDojazd() {
        return wybraneDojazd;
    }

    public void setWybraneDojazd(String wybraneDojazd) {
        this.wybraneDojazd = wybraneDojazd;
    }

    public String getWybraneOgrzewanie() {
        return wybraneOgrzewanie;
    }

    public void setWybraneOgrzewanie(String wybraneOgrzewanie) {
        this.wybraneOgrzewanie = wybraneOgrzewanie;
    }

    public String getWybraneOgrodzenie() {
        return wybraneOgrodzenie;
    }

    public void setWybraneOgrodzenie(String wybraneOgrodzenie) {
        this.wybraneOgrodzenie = wybraneOgrodzenie;
    }


    public List<String> getWybraneMedia() {
        return wybraneMedia;
    }

    public void setWybraneMedia(List<String> wybraneMedia) {
        this.wybraneMedia = wybraneMedia;
    }


    public List<String> getWybraneInfoDodatkowe() {
        return wybraneInfoDodatkowe;
    }

    public void setWybraneInfoDodatkowe(List<String> wybraneInfoDodatkowe) {
        this.wybraneInfoDodatkowe = wybraneInfoDodatkowe;
    }

    public List<String> getWybraneZabezpieczenia() {
        return wybraneZabezpieczenia;
    }

    public void setWybraneZabezpieczenia(List<String> wybraneZabezpieczenia) {
        this.wybraneZabezpieczenia = wybraneZabezpieczenia;
    }

}
