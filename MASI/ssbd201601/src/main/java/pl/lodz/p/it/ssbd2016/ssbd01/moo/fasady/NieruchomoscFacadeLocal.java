/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.Nieruchomosc;

import javax.ejb.Local;
import java.util.List;

/**
 * @author java
 */
@Local
public interface NieruchomoscFacadeLocal {

    void create(Nieruchomosc nieruchomosc);

    void edit(Nieruchomosc nieruchomosc);

    void remove(Nieruchomosc nieruchomosc);

    Nieruchomosc find(Object id);

    List<Nieruchomosc> findAll();

    List<Nieruchomosc> findRange(int[] range);

    int count();

    void flush();

}
