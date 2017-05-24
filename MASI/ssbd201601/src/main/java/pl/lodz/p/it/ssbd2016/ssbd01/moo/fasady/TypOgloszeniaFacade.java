/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady;

import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypOgloszenia;
import pl.lodz.p.it.ssbd2016.ssbd01.fasady.AbstractFacade;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Nieruchomosc;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypOgloszenia;
import pl.lodz.p.it.ssbd2016.ssbd01.fasady.AbstractFacade;

/**
 *
 * @author java
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class TypOgloszeniaFacade extends AbstractFacade<TypOgloszenia> implements TypOgloszeniaFacadeLocal {

    @PersistenceContext(unitName = "puwbazxnhkocevPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TypOgloszeniaFacade() {
        super(TypOgloszenia.class);
    }

    @Override
    @PermitAll
    public TypOgloszenia znajdzPoNazwie(String typ) {
        Query q = em.createNamedQuery("TypOgloszenia.findAll");
        List<TypOgloszenia> l = (List<TypOgloszenia>) q.getResultList();
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i).getNazwa().equals(typ) == true) {
                return l.get(i);
            }
        }
        return null;
    }
    
    @RolesAllowed("pobierzTypyOgloszen")
    public List<TypOgloszenia> findAll(){
         return super.findAll();
    }
    
}
