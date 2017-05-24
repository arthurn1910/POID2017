/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady;

import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.fasady.AbstractFacade;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypOgloszenia;

/**
 *
 * @author Gleam
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class TypNieruchomosciFacade extends AbstractFacade<TypNieruchomosci> implements TypNieruchomosciFacadeLocal {

    @PersistenceContext(unitName = "puwbazxnhkocevPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TypNieruchomosciFacade() {
        super(TypNieruchomosci.class);
    }

    @Override
    @PermitAll
    public TypNieruchomosci znajdzPoNazwie(String typ) {
        Query q = em.createNamedQuery("TypNieruchomosci.findAll");
        List<TypNieruchomosci> l = (List<TypNieruchomosci>) q.getResultList();
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i).getNazwa().equals(typ) == true) {
                return l.get(i);
            }
        }
        return null;
    }

    @RolesAllowed("przeliczAgregat")
    public void edit(TypNieruchomosci typNieruchomosci){
        super.edit(typNieruchomosci);
    }
    
    @RolesAllowed({"pobierzTypyNieruchomosci", "przeliczAgregat"})
    public List<TypNieruchomosci> findAll(){
        return super.findAll();
    }
}
