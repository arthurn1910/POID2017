/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady;

import java.util.*;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.ElementWyposazeniaNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Nieruchomosc;
import pl.lodz.p.it.ssbd2016.ssbd01.fasady.AbstractFacade;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;

/**
 *
 * @author java
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ElementWyposazeniaNieruchomosciMOOFacade extends AbstractFacade<ElementWyposazeniaNieruchomosci> implements ElementWyposazeniaNieruchomosciMOOFacadeLocal {
    
    @PersistenceContext(unitName = "puwbazxnhkocevPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ElementWyposazeniaNieruchomosciMOOFacade() {
        super(ElementWyposazeniaNieruchomosci.class);
    }

    @Override
    @RolesAllowed({"edytujOgloszenieDotyczaceUzytkownika", "pobierzWyposazenieNieruchomosci", "edytujOgloszenieInnegoUzytkownika"})
    public List<ElementWyposazeniaNieruchomosci> znajdzPoIdNieruchomosci(Long idNieruchomosci) {
        List<ElementWyposazeniaNieruchomosci> wyposazenie = new ArrayList();
        Query q = em.createNamedQuery("ElementWyposazeniaNieruchomosci.findAll");
        List<ElementWyposazeniaNieruchomosci> l = (List<ElementWyposazeniaNieruchomosci>) q.getResultList();
        for (int i = 0; i < l.size(); i++) {
            List<Nieruchomosc> nieruchomosci = new ArrayList(l.get(i).getNieruchomoscWyposazona());
            for(int j = 0; j < nieruchomosci.size(); j++) {
                if(nieruchomosci.get(j).getId().equals(idNieruchomosci))
                    wyposazenie.add(l.get(i));
            }
        }
        return wyposazenie;
    }
    
    @Override
    @RolesAllowed({"edytujOgloszenieDotyczaceUzytkownika" ,"edytujOgloszenieInnegoUzytkownika"})
    public void edit(ElementWyposazeniaNieruchomosci typOgloszenia){
        super.edit(typOgloszenia);
    }
        
    @Override
    @RolesAllowed({"edytujOgloszenieDotyczaceUzytkownika" ,"getWszystkieMozliweElementyWyposazeniaNieruchomosci", "edytujOgloszenieInnegoUzytkownika"})
    public ElementWyposazeniaNieruchomosci find(Object id){
        return super.find(id);
    }
    
    @RolesAllowed("getWszystkieMozliweElementyWyposazeniaNieruchomosci")
    public List<ElementWyposazeniaNieruchomosci> findAll(){
        return super.findAll();
    }
}
