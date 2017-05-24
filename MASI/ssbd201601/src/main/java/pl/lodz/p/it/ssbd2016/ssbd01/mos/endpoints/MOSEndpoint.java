package pl.lodz.p.it.ssbd2016.ssbd01.mos.endpoints;

import pl.lodz.p.it.ssbd2016.ssbd01.Utils.CloneUtils;
import pl.lodz.p.it.ssbd2016.ssbd01.Utils.ZalogowanyUzytkownik;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Spotkanie;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.ExteriorInterceptorMOS;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2016.ssbd01.mos.fasady.KontoFacadeLocalInMOS;
import pl.lodz.p.it.ssbd2016.ssbd01.mos.fasady.OgloszenieFacadeLocalInMOS;
import pl.lodz.p.it.ssbd2016.ssbd01.mos.fasady.SpotkanieFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.mos.managers.SpotkanieManagerLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.interceptor.Interceptors;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * API servera dla modułu funkcjonalnego MOS
 */
@Stateful
@Interceptors({ExteriorInterceptorMOS.class ,TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MOSEndpoint implements MOSEndpointLocal, SessionSynchronization {
    @EJB
    private SpotkanieFacadeLocal spotkanieFacade;
    @EJB
    private KontoFacadeLocalInMOS kontoFacade;
    @EJB
    private OgloszenieFacadeLocalInMOS ogloszenieFacade;
    @EJB
    private SpotkanieManagerLocal spotkanieManager;
    @Resource
    private SessionContext sessionContext;

    private long txId;
    private static final Logger logger = Logger.getLogger(MOSEndpoint.class.getName());

    private Spotkanie spotkanieStan;

    @Override
    @RolesAllowed("pobierzSpotkania")
    public List<Spotkanie> pobierzSpotkaniaKlienta(Konto spotkaniaDlaKonta) {

        return spotkanieFacade.pobierzSpotkaniaUzytkownika(spotkaniaDlaKonta);
    }

    @Override
    @RolesAllowed("pobierzSpotkania")
    public List<Spotkanie> pobierzSpotkaniaAgenta(Konto spotkaniaDlaKonta) {
        List<Spotkanie> listaSpotkanAgentaPowiazanaZOgloszeniem = new ArrayList<>();
        final List<Ogloszenie> wszystkieOgloszeniaAgenta = ogloszenieFacade.findByAgent(spotkaniaDlaKonta);
        for (Ogloszenie ogloszenie : wszystkieOgloszeniaAgenta) {
            final List<Spotkanie> byOgloszenie = spotkanieFacade.findByOgloszenie(ogloszenie);
            listaSpotkanAgentaPowiazanaZOgloszeniem.addAll(byOgloszenie);
        }

        return listaSpotkanAgentaPowiazanaZOgloszeniem;

    }

    @Override
    @RolesAllowed("anulujSpotkanie")
    public void anulujSpotkanie(Spotkanie spotkanieDoAnulowania) throws WyjatekSystemu {
        spotkanieManager.anulujSpotkanie(spotkanieDoAnulowania);
    }

    @Override
    @RolesAllowed("pobierzSpotkaniaDlaOgloszenia")
    public List<Spotkanie> pobierzSpotkaniaDlaOgloszenia(Ogloszenie ogloszenie) {
        return spotkanieManager.pobierzSpotkaniaDlaOgloszenia(ogloszenie);
    }

    @Override
    @RolesAllowed("rezerwujSpotkanie")
    public void rezerwujSpotkanie(Spotkanie spotkanie) throws WyjatekSystemu{
        spotkanieManager.rezerwujSpotkanie(spotkanie, sessionContext.getCallerPrincipal().getName());
    }

    @Override
    @RolesAllowed("pobierzSpotkanieDoEdycji")
    public Spotkanie pobierzSpotkanieDoEdycji(Spotkanie spotkanie) throws WyjatekSystemu, IOException, ClassNotFoundException {
        if(sessionContext.getCallerPrincipal().getName().equals(spotkanie.getIdUzytkownika().getLogin()) == false) {
            WyjatekSystemu ex=new WyjatekSystemu("blad.nieJestesWlascicielemOgloszenia", "MOS");
            throw new WyjatekSystemu("blad.nieJestesWlascicielemOgloszenia", ex, "MOS");
        }
        spotkanieStan = spotkanieFacade.find(spotkanie.getId());
        return (Spotkanie) CloneUtils.deepCloneThroughSerialization(spotkanieStan);
    }

    @Override
    @RolesAllowed("zapiszSpotkaniePoEdycji")
    public void zapiszSpotkaniePoEdycji(Spotkanie spotkanie) {
        spotkanieFacade.edit(spotkanie);
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @RolesAllowed("pobierzMojeKonto")
    public Konto pobierzMojeKonto() {
        final String loginZalogowanegoUzytkownika = ZalogowanyUzytkownik.getLoginZalogowanegoUzytkownika();
        return kontoFacade.znajdzPoLoginie(loginZalogowanegoUzytkownika);
    }
    //Implementacja SessionSynchronization

    /**
     * Metoda logująca czas rozpoczęcia transakcji
     *
     * @throws EJBException
     * @throws RemoteException
     */
    @Override
    public void afterBegin() throws EJBException, RemoteException {
        txId = System.currentTimeMillis();
        logger.log(Level.SEVERE, "Transakcja o ID: " + txId + " zostala rozpoczeta");
    }

    /**
     * Metoda logująca czas przed zakończeniem transakcji
     *
     * @throws EJBException
     * @throws RemoteException
     */
    @Override
    public void beforeCompletion() throws EJBException, RemoteException {
        logger.log(Level.SEVERE, "Transakcja o ID: " + txId + " przed zakonczeniem");
    }

    /**
     * Metoda logująca stan zakończonej transakcji
     *
     * @param committed stan zakończonej transakcji
     * @throws EJBException
     * @throws RemoteException
     */
    @Override
    public void afterCompletion(boolean committed) throws EJBException, RemoteException {
        logger.log(Level.SEVERE, "Transakcja o ID: " + txId + " zostala zakonczona przez: " + (committed ? "zatwierdzenie" : "wycofanie"));
    }
}
