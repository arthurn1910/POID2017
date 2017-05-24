/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.mos.fasady;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Spotkanie;
import pl.lodz.p.it.ssbd2016.ssbd01.fasady.AbstractFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;

/**
 *
 * @author java
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class SpotkanieFacade extends AbstractFacade<Spotkanie> implements SpotkanieFacadeLocal {

    @PersistenceContext(unitName = "puwbazxnhkocevPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SpotkanieFacade() {
        super(Spotkanie.class);
    }

    @Override
    @RolesAllowed("pobierzSpotkania")
    public List<Spotkanie> pobierzSpotkaniaUzytkownika(Konto konto) {
        Query query = em.createNamedQuery("Spotkanie.findByIdUzytkownika");
        query.setParameter("idUzytkownika", konto);
        return (List < Spotkanie >) query.getResultList();
    }

    @Override
    @RolesAllowed("pobierzSpotkaniaDlaOgloszenia")
    public List<Spotkanie> findByOgloszenie(Ogloszenie ogloszenie) {
        Query q = em.createNamedQuery("Spotkanie.findByOgloszenie");
        q.setParameter("ogloszenie",ogloszenie);
        return (List<Spotkanie>) q.getResultList();
    }
    
    @Override
    @RolesAllowed("rezerwujSpotkanie")
    public List<Spotkanie> terminyUzytkownika(Konto id) {
        Query q = em.createNamedQuery("Spotkanie.findByIdUzytkownika");
        q.setParameter("idUzytkownika", id);
        return (List < Spotkanie >) q.getResultList();
    }
    
    @RolesAllowed("rezerwujSpotkanie")
    public void create(Spotkanie spotkanie){
        super.create(spotkanie);
    }

    @RolesAllowed({"zapiszSpotkaniePoEdycji",  "edytujSpotkania"})
    public void edit(Spotkanie spotkanie){
        super.edit(spotkanie);
    }
    
    @RolesAllowed("anulujSpotkanie")
    public void remove(Spotkanie spotkanie){
        super.remove(spotkanie);
    }
    
    @RolesAllowed("pobierzSpotkanieDoEdycji")
    public Spotkanie find(Object id){
        return super.find(id);
    }
}
