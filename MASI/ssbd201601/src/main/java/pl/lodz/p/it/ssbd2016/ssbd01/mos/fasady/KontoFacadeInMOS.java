/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.mos.fasady;

import javax.annotation.security.RolesAllowed;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.fasady.AbstractFacade;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;

/**
 *
 * @author java
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class KontoFacadeInMOS extends AbstractFacade<Konto> implements KontoFacadeLocalInMOS {

    @PersistenceContext(unitName = "puwbazxnhkocevPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KontoFacadeInMOS() {
        super(Konto.class);
    }

    @Override
    @RolesAllowed({"pobierzMojeKonto", "rezerwujSpotkanie", "anulujSpotkanie"})
    public Konto znajdzPoLoginie(String login) {
        Query q = em.createNamedQuery("Konto.findByLogin");
        q.setParameter("login", login);
        return (Konto) q.getSingleResult();
    }
    
    @RolesAllowed("anulujSpotkanie")
    public void edit(Konto konto){
        super.edit(konto);
    }
    
    @RolesAllowed("rezerwujSpotkanie")
    public Konto find(Object id){
        return super.find(id);
    }
}
