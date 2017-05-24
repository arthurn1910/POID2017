package pl.lodz.p.it.ssbd2016.ssbd01.moo.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.ElementWyposazeniaNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;
import java.util.*;

/**
 * Obsługuje widok edycji ogłoszenia
 */
@Named
@RequestScoped
public class EdytujOgloszenieBean {
    
    @Inject
    private OgloszenieSession ogloszenieSession;

    /**
     * Metoda ma za zadanie zapisanie ogłoszenia po zakończeniu edycji
     * @return zwraca łańcuch, który przekierowuje do widoku wyświetlającego ogłoszenia
     * @throws WyjatekSystemu
     */
    public String edytujOgloszenieDanegoUzytkownika() throws WyjatekSystemu {
        ogloszenieSession.edytujOgloszenieDanegoUzytkownika();
        return "wyswietlOgloszenia";
    }
    
    /**
     * Metoda usuwa element wyposażenia z kolekcji
     * @param element element do usunięcia
     */
    public void usunElementWyposazenia(ElementWyposazeniaNieruchomosci element) {
        List<ElementWyposazeniaNieruchomosci> wyposazenieNieruchomosci = ogloszenieSession.getWyposazenieNieruchomosci();
        for(int i = 0; i < wyposazenieNieruchomosci.size(); i++) {
            if(wyposazenieNieruchomosci.get(i).getId().equals(element.getId())) {
                wyposazenieNieruchomosci.remove(i);
                break;
            }
        }
        ogloszenieSession.setWyposazenieNieruchomosci(wyposazenieNieruchomosci);
        ogloszenieSession.getOgloszenieEdytuj().getNieruchomosc().setElementWyposazeniaNieruchomosciCollection(wyposazenieNieruchomosci);
        List<ElementWyposazeniaNieruchomosci> mozliweWyposazenie = ogloszenieSession.getMozliweWyposazenie();
        mozliweWyposazenie.add(element);
        ogloszenieSession.setMozliweWyposazenie(mozliweWyposazenie);
    }
    
    /**
     * Metoda dodaje element wyposażenia nieruchomości do kolekcji
     * @param element 
     */
    public void dodajElementWyposazenia(ElementWyposazeniaNieruchomosci element) {
        List<ElementWyposazeniaNieruchomosci> wyposazenieNieruchomosci = ogloszenieSession.getWyposazenieNieruchomosci();
        for(int i = 0; i < wyposazenieNieruchomosci.size(); i++)
            if(wyposazenieNieruchomosci.get(i).getId().equals(element.getId()))
                return;
        wyposazenieNieruchomosci.add(element);
        ogloszenieSession.setWyposazenieNieruchomosci(wyposazenieNieruchomosci);
        ogloszenieSession.getOgloszenieEdytuj().getNieruchomosc().setElementWyposazeniaNieruchomosciCollection(wyposazenieNieruchomosci);
        List<ElementWyposazeniaNieruchomosci> mozliweWyposazenie = ogloszenieSession.getMozliweWyposazenie();
        for(int i = 0; i < mozliweWyposazenie.size(); i++)
            if(mozliweWyposazenie.get(i).getId().equals(element.getId()))
                mozliweWyposazenie.remove(i);
        ogloszenieSession.setMozliweWyposazenie(mozliweWyposazenie);
    }
    /**
     * Metoda ma za zadanie zapisanie ogłoszenia innego użytkownika po zakończeniu edycji
     * @return zwraca łańcuch, który przekierowuje do widoku wyświetlającego ogłoszenia
     * @throws WyjatekSystemu
     */
    public String edytujOgloszenieInnegoUzytkownika() throws WyjatekSystemu{
        ogloszenieSession.zapiszOgloszenieInnegoUzytkownikaPoEdycji();
        ogloszenieSession.pobierzWszystkieOgloszenia();
        return "wyswietlOgloszenia";
    }
    
    /**
     * Metoda zwraca aktualnie edytowane ogłoszenie
     * @return edytowane ogłoszenie
     * @throws WyjatekSystemu 
     */
    public Ogloszenie getOgloszenieEdytuj() throws WyjatekSystemu {
        return ogloszenieSession.getOgloszenieEdytuj();
    }
    
    /**
     * Metoda zwraca wyposażenie nieruchomości aktualnie edytowanego ogłoszenia
     * @return lista elementów wyposażenia
     */
    public List<ElementWyposazeniaNieruchomosci> getWyposazenieNieruchomosci() {
        return ogloszenieSession.getWyposazenieNieruchomosci();
    }
    
    /**
     * Metoda zwraca dozwoloną listę elementów wyposażenia nieruchomości
     * Wszystkie możliwe elementy oprócz tych, które są już w nieruchomości z aktualnie edytowanego ogłoszenia
     * @return lista elementów wyposażenia
     */
    public List<ElementWyposazeniaNieruchomosci> getMozliweWyposazenie() {
        return ogloszenieSession.getMozliweWyposazenie();
    }
}
