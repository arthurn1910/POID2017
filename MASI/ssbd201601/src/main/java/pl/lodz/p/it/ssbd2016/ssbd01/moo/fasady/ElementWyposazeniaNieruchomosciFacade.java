package pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.ElementWyposazeniaNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.fasady.AbstractFacade;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.PoziomDostepu;

/**
 * Created by Kamil Rogowski on 26.05.2016.
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ElementWyposazeniaNieruchomosciFacade extends AbstractFacade<ElementWyposazeniaNieruchomosci> implements ElementWyposazeniaNieruchomosciFacadeLocal {

    @PersistenceContext(unitName = "puwbazxnhkocevPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ElementWyposazeniaNieruchomosciFacade() {
        super(ElementWyposazeniaNieruchomosci.class);
    }
    
    @Override
    @RolesAllowed("dodajOgloszenie")
    public void edit(ElementWyposazeniaNieruchomosci elementWyposazeniaNieruchomosci){
        super.edit(elementWyposazeniaNieruchomosci);
    }
    
    @Override
    @RolesAllowed("pobierzElementyKategorii")
    public List<ElementWyposazeniaNieruchomosci> findAll(){
        return super.findAll();
    }
    
    @RolesAllowed("dodajOgloszenie")
    public void flush(){
        super.flush();
    }
    
}
