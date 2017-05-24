package pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.KategoriaWyposazeniaNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.fasady.AbstractFacade;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Kamil Rogowski on 26.05.2016.
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class KategoriaWyposazeniaNieruchomosciFacade extends AbstractFacade<KategoriaWyposazeniaNieruchomosci> implements KategoriaWyposazeniaNieruchomosciFacadeLocal {

    @PersistenceContext(unitName = "puwbazxnhkocevPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KategoriaWyposazeniaNieruchomosciFacade() {
        super(KategoriaWyposazeniaNieruchomosci.class);
    }
    
    @RolesAllowed("pobierzKategorie")
    public List<KategoriaWyposazeniaNieruchomosci> findAll(){
        return super.findAll();
    }
}
