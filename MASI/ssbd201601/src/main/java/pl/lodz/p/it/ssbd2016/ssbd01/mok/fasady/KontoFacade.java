package pl.lodz.p.it.ssbd2016.ssbd01.mok.fasady;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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
public class KontoFacade extends AbstractFacade<Konto> implements KontoFacadeLocal {

    @PersistenceContext(unitName = "puwbazxnhkocevPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KontoFacade() {
        super(Konto.class);
    }
        
    @PermitAll
    @Override
    public void create(Konto konto) {
        super.create(konto);
    }
    
    @RolesAllowed({"zablokujKonto", "odblokujKonto", "pobierzKontoDoEdycji", "potwierdzKonto"})
    @Override
    public Konto find(Object id) {
        return super.find(id);
    }
    
    @RolesAllowed({"zmienMojeHaslo", "zmienHaslo", "zapiszSwojeKontoPoEdycji"})
    @Override
    public void edit(Konto konto) {
        super.edit(konto);
    }
    
    //@RolesAllowed({"znajdzPoLoginie", "dodajPoziomDostepu", "getSwojeKonto"})
    @Override
    @PermitAll
    public Konto znajdzPoLoginie(String login) {
        Query q = em.createNamedQuery("Konto.findByLogin");
        q.setParameter("login", login);
        return (Konto) q.getSingleResult();
    }
        
    @RolesAllowed("pobierzPodobneKonta")
    @Override
    public List<Konto> znajdzPodobne(Konto konto) {
        Query query = em.createNamedQuery("Konto.findSimilar");
        query.setParameter("imie", konto.getImie());
        query.setParameter("nazwisko", konto.getNazwisko());
        query.setParameter("email", konto.getEmail());
        query.setParameter("telefon", konto.getTelefon());
        query.setParameter("potwierdzone", konto.getPotwierdzone());
        query.setParameter("aktywne", konto.getAktywne());
        List<Konto> listaKontPodobnych = (List<Konto>) query.getResultList();
        
        if (listaKontPodobnych == null) {
            return new ArrayList<Konto>();
        }
        return listaKontPodobnych;
    }
}

