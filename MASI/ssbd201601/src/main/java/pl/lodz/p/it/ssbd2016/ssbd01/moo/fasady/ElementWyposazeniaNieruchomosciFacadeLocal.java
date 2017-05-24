package pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.ElementWyposazeniaNieruchomosci;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Kamil Rogowski on 26.05.2016.
 */
@Local
public interface ElementWyposazeniaNieruchomosciFacadeLocal {

    ElementWyposazeniaNieruchomosci find(Object id);

    List<ElementWyposazeniaNieruchomosci> findAll();

    List<ElementWyposazeniaNieruchomosci> findRange(int[] range);

    int count();

    void flush();

    void create(ElementWyposazeniaNieruchomosci elementWyposazeniaNieruchomosci);

    void edit(ElementWyposazeniaNieruchomosci elementWyposazeniaNieruchomosci);
}
