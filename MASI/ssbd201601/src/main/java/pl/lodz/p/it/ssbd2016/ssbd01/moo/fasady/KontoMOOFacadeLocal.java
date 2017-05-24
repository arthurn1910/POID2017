/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;

import javax.ejb.Local;
import java.util.List;

/**
 * @author java
 */
@Local
public interface KontoMOOFacadeLocal {

    void create(Konto konto);

    void edit(Konto konto);

    void remove(Konto konto);

    Konto find(Object id);

    List<Konto> findAll();

    List<Konto> findRange(int[] range);

    /**
     * Zwraca konto o podanym loginie
     * @param login
     * @return 
     */
    Konto znajdzPoLoginie(String login);

    int count();

    void flush();
}
