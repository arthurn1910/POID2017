/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypNieruchomosci;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Gleam
 */
@Local
public interface TypNieruchomosciFacadeLocal {

    void create(TypNieruchomosci typNieruchomosci);

    void edit(TypNieruchomosci typNieruchomosci);

    void remove(TypNieruchomosci typNieruchomosci);

    TypNieruchomosci find(Object id);

    List<TypNieruchomosci> findAll();

    List<TypNieruchomosci> findRange(int[] range);

    int count();

    /**
     * Metoda zwraca obiekt TypNieruchomosci, który ma nazwe taką jak podana w parametrze
     * @param typ nazwa typu
     * @return odpowiedni obiektTypNieruchomosci
     */
    public TypNieruchomosci znajdzPoNazwie(String typ);
    
    void flush();
}
