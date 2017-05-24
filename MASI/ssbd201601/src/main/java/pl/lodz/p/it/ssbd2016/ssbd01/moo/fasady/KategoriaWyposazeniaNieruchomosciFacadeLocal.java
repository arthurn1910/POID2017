package pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.KategoriaWyposazeniaNieruchomosci;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Kamil Rogowski on 26.05.2016.
 */
@Local
public interface KategoriaWyposazeniaNieruchomosciFacadeLocal {

    KategoriaWyposazeniaNieruchomosci find(Object id);

    List<KategoriaWyposazeniaNieruchomosci> findAll();

    List<KategoriaWyposazeniaNieruchomosci> findRange(int[] range);

    int count();
}
