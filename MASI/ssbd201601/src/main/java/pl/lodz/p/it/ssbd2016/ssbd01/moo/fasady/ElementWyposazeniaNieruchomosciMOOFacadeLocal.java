/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady;

import java.util.List;
import javax.ejb.Local;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.ElementWyposazeniaNieruchomosci;

/**
 *
 * @author java
 */
@Local
public interface ElementWyposazeniaNieruchomosciMOOFacadeLocal {

    void create(ElementWyposazeniaNieruchomosci typOgloszenia);

    void edit(ElementWyposazeniaNieruchomosci typOgloszenia);

    void remove(ElementWyposazeniaNieruchomosci typOgloszenia);

    ElementWyposazeniaNieruchomosci find(Object id);

    List<ElementWyposazeniaNieruchomosci> findAll();

    List<ElementWyposazeniaNieruchomosci> findRange(int[] range);

    int count();

    /**
     * Metoda zwraca listę elementów wyposażenia nieruchomości o id podanym w parametrze
     * @param idNieruchomosci id nieruchomości
     * @return lista elementów wyposażenia
     */
    public List<ElementWyposazeniaNieruchomosci> znajdzPoIdNieruchomosci(Long idNieruchomosci);
    
}
