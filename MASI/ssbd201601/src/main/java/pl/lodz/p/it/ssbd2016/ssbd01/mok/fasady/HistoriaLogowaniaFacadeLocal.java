package pl.lodz.p.it.ssbd2016.ssbd01.mok.fasady;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.HistoriaLogowania;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Kamil Rogowski on 01.05.2016.
 *
 * @author Kamil Rogowski
 */
@Local
public interface HistoriaLogowaniaFacadeLocal {

    void create(HistoriaLogowania historiaLogowania);

    void edit(HistoriaLogowania historiaLogowania);

    void remove(HistoriaLogowania historiaLogowania);

    HistoriaLogowania find(Object id);

    List<HistoriaLogowania> findAll();

    List<HistoriaLogowania> findRange(int[] range);

    int count();

    /**
     * zwraca null lub jeden wynik, bo na niej bazuje insert/update
     * @param konto
     * @return
     */
    HistoriaLogowania findyByIdKonta(Konto konto);
}
