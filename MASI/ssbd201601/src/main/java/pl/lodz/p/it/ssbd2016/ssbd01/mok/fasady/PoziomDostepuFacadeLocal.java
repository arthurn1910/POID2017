/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.mok.fasady;

import java.util.List;
import javax.ejb.Local;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.PoziomDostepu;

/**
 *
 * @author java
 */
@Local
public interface PoziomDostepuFacadeLocal {

    void create(PoziomDostepu poziomDostepu);

    void edit(PoziomDostepu poziomDostepu);

    void remove(PoziomDostepu poziomDostepu);

    PoziomDostepu find(Object id);

    List<PoziomDostepu> findAll();

    List<PoziomDostepu> findRange(int[] range);

    int count();
    
}
