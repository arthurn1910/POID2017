/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.mos.fasady;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;

import javax.ejb.Local;
import java.util.List;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;

/**
 *
 * @author java
 */
@Local
public interface OgloszenieFacadeLocalInMOS {

    void create(Ogloszenie ogloszenie);

    void edit(Ogloszenie ogloszenie);

    void remove(Ogloszenie ogloszenie);

    Ogloszenie find(Object id);

    List<Ogloszenie> findAll();

    List<Ogloszenie> findRange(int[] range);

    int count();

    /**
     * Znajduje ogloszenie po id
     * @param id ogloszenia
     * @return objekt ogloszenie
     */
    Ogloszenie findById(Long id);
    
    /***
     * Znajduje liste ogłoszeń po id agenta
     * @param agent
     * @return 
     */
    List<Ogloszenie> findByAgent(Konto agent);
    
}
