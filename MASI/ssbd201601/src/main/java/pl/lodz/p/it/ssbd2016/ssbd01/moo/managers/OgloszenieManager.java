package pl.lodz.p.it.ssbd2016.ssbd01.moo.managers;

import java.util.logging.Logger;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.KontoMOOFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.NieruchomoscFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.OgloszenieFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.TypNieruchomosciFacadeLocal;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.context.FacesContext;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.KontoMOOFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.OgloszenieFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

/**
 * Klasa pośrednicząca miedzy MOOEndpoint a fasadami. Przetwarza niezbędne dane.
 * Implementuje interfejs OgloszenieManagaerLocal
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class OgloszenieManager implements OgloszenieManagerLocal {

    
    @EJB
    private KontoMOOFacadeLocal kontoFacade;
    @EJB
    private OgloszenieFacadeLocal ogloszenieFacade;
    @EJB
    private NieruchomoscFacadeLocal nieruchomoscFacade;
    @EJB
    private TypNieruchomosciFacadeLocal typNieruchomosciFacade;

    private static final Logger logger = Logger.getLogger(OgloszenieManager.class.getName());

    @Override
    @RolesAllowed("dodajDoUlubionych")
    public void dodajDoUlubionych(Ogloszenie ogloszenie) {
        String login = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        
        Konto konto = kontoFacade.znajdzPoLoginie(login);
        Ogloszenie ogloszenieTemp = ogloszenieFacade.znajdzPoID(ogloszenie.getId());
        
        ogloszenieTemp.getKontoCollection().add(konto);
        konto.getOgloszenieUlubioneCollection().add(ogloszenieTemp);
        
        ogloszenieFacade.edit(ogloszenieTemp);
        kontoFacade.edit(konto);
    }

    @Override
    @RolesAllowed("usunZUlubionych")
    public void usunZUlubionych(Ogloszenie ogloszenie) {
        String login = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        Konto konto = kontoFacade.znajdzPoLoginie(login);
        Ogloszenie ogloszenieTemp = ogloszenieFacade.znajdzPoID(ogloszenie.getId());
        
        ogloszenieTemp.getKontoCollection().remove(konto);
        konto.getOgloszenieUlubioneCollection().remove(ogloszenieTemp);
        
        ogloszenieFacade.edit(ogloszenieTemp);
        kontoFacade.edit(konto);
    }

    @Override
    @RolesAllowed("przeliczAgregat")
    public void przeliczAgregat() {
        final List<TypNieruchomosci> listaNieruchomosci = typNieruchomosciFacade.findAll();
        final List<Ogloszenie> ogloszenia = ogloszenieFacade.findAll();
        double powierzchnia = 0, cena = 0;

        double agregat;
        for (TypNieruchomosci typNieruchomosci : listaNieruchomosci) {

            for (Ogloszenie ogloszenie : ogloszenia) {
                
                if (!ogloszenie.getAktywne())
                    continue;
                
                if(typNieruchomosci.getId().equals(ogloszenie.getNieruchomosc().getTypNieruchomosci().getId()) && typNieruchomosci.getId() == 3 ){
                    powierzchnia += ogloszenie.getNieruchomosc().getPowierzchniaDzialki();
                    cena += ogloszenie.getCena();
                }
                else if(typNieruchomosci.getId().equals(ogloszenie.getNieruchomosc().getTypNieruchomosci().getId())){
                    powierzchnia += ogloszenie.getNieruchomosc().getPowierzchniaNieruchomosci();
                    cena += ogloszenie.getCena();

                }
                logger.info("cena " + cena + "powierzchnia" + powierzchnia + "typNieruchomosci.getId()" + typNieruchomosci.getId());
            }
            if(powierzchnia == 0) powierzchnia = 1;
            agregat =  cena / powierzchnia;
            logger.info("cena: " + cena + "powierzchnia: " + powierzchnia + "agregat: ");
            typNieruchomosci.setSredniaCenaMetraKwadratowego(BigDecimal.valueOf(agregat));
            typNieruchomosciFacade.edit(typNieruchomosci);
            powierzchnia = 0;
            cena = 0;
        }

    }
    
    @Override
    @RolesAllowed("przydzielAgentaDoOgloszenia")
    public void przydzielAgenta(Ogloszenie rowData, Konto agent) {
        Logger loger = Logger.getLogger(TrackerInterceptor.class.getName());
        Ogloszenie o = ogloszenieFacade.znajdzPoID(rowData.getId());
        Konto tmp;
        if(o.getIdAgenta()!=null){
            tmp=kontoFacade.znajdzPoLoginie(agent.getLogin());
            tmp.getOgloszenieAgentaCollection().remove(o);
            kontoFacade.edit(tmp);
        }else{
            o.setAktywne(true);
        }
        
        tmp=kontoFacade.znajdzPoLoginie(agent.getLogin());
        o.setIdAgenta(tmp);
        ogloszenieFacade.edit(o);
        tmp.getOgloszenieAgentaCollection().add(o);
        kontoFacade.edit(tmp);
    }
}
