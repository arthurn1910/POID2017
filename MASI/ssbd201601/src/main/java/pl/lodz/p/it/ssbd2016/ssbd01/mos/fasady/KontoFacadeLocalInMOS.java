/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.mos.fasady;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;

import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author java
 */
@Local
public interface KontoFacadeLocalInMOS {

    void create(Konto konto);

    void edit(Konto konto);

    void remove(Konto konto);

    Konto find(Object id);

    List<Konto> findAll();

    List<Konto> findRange(int[] range);

    int count();

    /**
     * Metoda zwracająca obiekt klasy konto z danym loginem
     * @param login     login użytkownika do wyszukania
     * @return          obiekt klasy konto o zadanym loginie
     */
    Konto znajdzPoLoginie(String login);
    
}
