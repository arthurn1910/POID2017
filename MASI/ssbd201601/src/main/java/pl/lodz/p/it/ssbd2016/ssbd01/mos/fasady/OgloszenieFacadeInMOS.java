/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.mos.fasady;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.fasady.AbstractFacade;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;

/**
 * @author java
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class OgloszenieFacadeInMOS extends AbstractFacade<Ogloszenie> implements OgloszenieFacadeLocalInMOS {

    @PersistenceContext(unitName = "puwbazxnhkocevPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OgloszenieFacadeInMOS() {
        super(Ogloszenie.class);
    }

    @Override
    @RolesAllowed("rezerwujSpotkanie")
    public Ogloszenie findById(Long id) {
        Query q = em.createNamedQuery("Ogloszenie.findById");
        q.setParameter("id", id);
        return (Ogloszenie) q.getSingleResult();
    }
    
    @RolesAllowed("anulujSpotkanie")
    public void edit(Ogloszenie ogloszenie){
        super.edit(ogloszenie);
    }

    @Override
    @RolesAllowed("listaOgloszenPoIDAgenta")
    public List<Ogloszenie> findByAgent(Konto agent) {
        Query q = em.createNamedQuery("OgloszenieID.findByAgent");
        q.setParameter("agent", agent);
        return (List<Ogloszenie>) q.getResultList();
    }
    
    

}
