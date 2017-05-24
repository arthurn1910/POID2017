package pl.lodz.p.it.ssbd2016.ssbd01.moo.endpoints;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.*;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.naming.NamingException;
import pl.lodz.p.it.ssbd2016.ssbd01.Utils.CloneUtils;
import pl.lodz.p.it.ssbd2016.ssbd01.Utils.PoziomDostepuManager;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Nieruchomosc;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.PoziomDostepu;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.TypOgloszenia;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.ElementWyposazeniaNieruchomosci;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.ExteriorInterceptorMOO;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.NieruchomoscFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.OgloszenieFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.TypNieruchomosciFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.TypOgloszeniaFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.KontoMOOFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.fasady.ElementWyposazeniaNieruchomosciMOOFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.moo.managers.OgloszenieManagerLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * API servera dla modułu funkcjonalnego MOO
 */
@Stateful
@Interceptors({ExteriorInterceptorMOO.class ,TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MOOEndpoint implements MOOEndpointLocal, SessionSynchronization {
    @EJB
    private OgloszenieManagerLocal ogloszenieManager;
    @EJB
    private KontoMOOFacadeLocal kontoFacade;
    @EJB
    private TypOgloszeniaFacadeLocal typOgloszeniaFacade;
    @EJB
    private TypNieruchomosciFacadeLocal typNieruchomosciFacade;
    @EJB
    private OgloszenieFacadeLocal ogloszenieFacadeLocal;
    @EJB
    private NieruchomoscFacadeLocal nieruchomoscFacadeLocal;
    @EJB
    private ElementWyposazeniaNieruchomosciMOOFacadeLocal elementWyposazeniaFacadeLocal;
    @EJB
    private ElementWyposazeniaNieruchomosciFacadeLocal elementWyposazeniaNieruchomosciFacade;
    @EJB
    private KategoriaWyposazeniaNieruchomosciFacadeLocal kategoriaWyposazeniaNieruchomosciFacade;
    @Resource
    private SessionContext sessionContext;

    private Ogloszenie ogloszenieStan;
    private Nieruchomosc nieruchomoscStan;


    private long txId;
    private static final Logger loger = Logger.getLogger(MOOEndpoint.class.getName());

    @Override
    @RolesAllowed("dodajOgloszenie")
    public void dodajOgloszenie(Ogloszenie noweOgloszenie, Nieruchomosc nowaNieruchomosc, List<ElementWyposazeniaNieruchomosci> elementWyposazeniaNieruchomosci) {

        ogloszenieFacadeLocal.create(noweOgloszenie);
        ogloszenieFacadeLocal.flush();
        for (ElementWyposazeniaNieruchomosci wyposazeniaNieruchomosci : elementWyposazeniaNieruchomosci) {
            elementWyposazeniaNieruchomosciFacade.edit(wyposazeniaNieruchomosci);
        }
        elementWyposazeniaNieruchomosciFacade.flush();
    }

    @RolesAllowed("pobierzElementyKategorii")
    @Override
    public List<ElementWyposazeniaNieruchomosci> pobierzElementyKategorii() {
        return elementWyposazeniaNieruchomosciFacade.findAll();
    }

    @RolesAllowed("pobierzKategorie")
    @Override
    public List<KategoriaWyposazeniaNieruchomosci> pobierzKategorie() {
        return kategoriaWyposazeniaNieruchomosciFacade.findAll();
    }

    @Override
    @RolesAllowed("edytujOgloszenieDotyczaceUzytkownika")
    public void edytujOgloszenieDotyczaceUzytkownika(Ogloszenie ogloszenieNowe) throws WyjatekSystemu {
        String loginKonta = sessionContext.getCallerPrincipal().getName();
        if (ogloszenieStan == null) {
            throw new WyjatekSystemu("blad.brakOgloszeniaDoEdycji","MOO");
        }
        if (nieruchomoscStan == null) {
            throw new WyjatekSystemu("blad.brakNieruchomosciDoEdycji","MOO");
        }
        if(ogloszenieStan.getIdWlasciciela().getLogin().equals(loginKonta) == false && ogloszenieStan.getIdAgenta().getLogin().equals(loginKonta) == false) {
            throw new WyjatekSystemu("blad.nieJestesWlascielemOgloszenia","MOO");
        }
        else {
            TypOgloszenia typ = typOgloszeniaFacade.znajdzPoNazwie(ogloszenieNowe.getTypOgloszenia().getNazwa());
            nieruchomoscStan.setMiejscowosc(ogloszenieNowe.getNieruchomosc().getMiejscowosc());
            nieruchomoscStan.setUlica(ogloszenieNowe.getNieruchomosc().getUlica());
            nieruchomoscStan.setLiczbaPieter(ogloszenieNowe.getNieruchomosc().getLiczbaPieter());
            nieruchomoscStan.setLiczbaPokoi(ogloszenieNowe.getNieruchomosc().getLiczbaPokoi());
            nieruchomoscStan.setPowierzchniaDzialki(ogloszenieNowe.getNieruchomosc().getPowierzchniaDzialki());
            nieruchomoscStan.setPowierzchniaNieruchomosci(ogloszenieNowe.getNieruchomosc().getPowierzchniaNieruchomosci());
            nieruchomoscStan.setElementWyposazeniaNieruchomosciCollection(ogloszenieNowe.getNieruchomosc().getElementWyposazeniaNieruchomosciCollection());
            List<ElementWyposazeniaNieruchomosci> wyposazenie = elementWyposazeniaFacadeLocal.znajdzPoIdNieruchomosci(nieruchomoscStan.getId());
            List<ElementWyposazeniaNieruchomosci> wyposazenieNowe = new ArrayList(ogloszenieNowe.getNieruchomosc().getElementWyposazeniaNieruchomosciCollection());
            for(int i = 0; i < wyposazenie.size(); i++) {
                boolean jest = false;
                for(int j = 0; j < wyposazenieNowe.size(); j++)
                    if(wyposazenieNowe.get(j).getId().equals(wyposazenie.get(i).getId()))
                        jest = true;
                
                if(!jest) {
                    ElementWyposazeniaNieruchomosci el = elementWyposazeniaFacadeLocal.find(wyposazenie.get(i).getId());
                    List<Nieruchomosc> n = new ArrayList(el.getNieruchomoscWyposazona());
                    for(int j = 0; j < n.size(); j++)
                        if(n.get(j).getId().equals(nieruchomoscStan.getId()))
                            n.remove(j);
                    el.setNieruchomoscWyposazonaCollection(n);
                    elementWyposazeniaFacadeLocal.edit(el);
                }
            }
            
            for(int i = 0; i < wyposazenieNowe.size(); i++) {
                boolean jest = false;
                for(int j = 0; j < wyposazenie.size(); j++)
                    if(wyposazenieNowe.get(i).getId().equals(wyposazenie.get(j).getId()))
                        jest = true;
                
                if(!jest) {
                    ElementWyposazeniaNieruchomosci el = elementWyposazeniaFacadeLocal.find(wyposazenieNowe.get(i).getId());
                    List<Nieruchomosc> n = new ArrayList(el.getNieruchomoscWyposazona());
                    n.add(nieruchomoscStan);
                    el.setNieruchomoscWyposazonaCollection(n);
                    elementWyposazeniaFacadeLocal.edit(el);
                }
            }
            
            ogloszenieStan.setTypOgloszenia(typ);
            ogloszenieStan.setTytul(ogloszenieNowe.getTytul());
            ogloszenieStan.setCena(ogloszenieNowe.getCena());
            nieruchomoscFacadeLocal.edit(nieruchomoscStan);
            ogloszenieFacadeLocal.edit(ogloszenieStan);
            ogloszenieManager.przeliczAgregat();
            ogloszenieStan = null;
            nieruchomoscStan = null;
        }
    }

    @Override
    @RolesAllowed("deaktywujOgloszenieDotyczaceUzytkownika")
    public void deaktywujOgloszenieDotyczaceUzytkownika(Ogloszenie ogloszenie) throws WyjatekSystemu {
        String loginKonta = sessionContext.getCallerPrincipal().getName();
        Ogloszenie o = ogloszenieFacadeLocal.find(ogloszenie.getId());
        if(o.getSpotkanieCollection() != null) {
            if(o.getSpotkanieCollection().size() != 0) {
                System.out.println("------------aaa");
                WyjatekSystemu ex=new WyjatekSystemu("blad.toOgloszenieMaSpotkania","MOO");
                throw new WyjatekSystemu("blad.toOgloszenieMaSpotkania", ex, "MOO");
            }
        }
        if(o.getIdWlasciciela().getLogin().equals(loginKonta) == false && o.getIdAgenta().getLogin().equals(loginKonta) == false) {
            WyjatekSystemu ex=new WyjatekSystemu("blad.nieJestesWlascicielemOgloszenia","MOO");
            throw new WyjatekSystemu("blad.nieJestesWlascicielemOgloszenia",ex,"MOO");
        }
        else if(o.getAktywne() == false) {
            WyjatekSystemu ex=new WyjatekSystemu("blad.ogloszenieDeaktywowaneWczesniej","MOO");
            throw new WyjatekSystemu("blad.ogloszenieDeaktywowaneWczesniej",ex,"MOO");
        }
        else {
            o.setAktywne(false);
            ogloszenieManager.przeliczAgregat();
        }
    }

    @Override
    @RolesAllowed("deaktywujOgloszenie")
    public void deaktywujOgloszenie(Ogloszenie rowData) throws WyjatekSystemu {
    }

    @Override
    @RolesAllowed("pobierzListeAgentow")
    public List<Konto> pobierzListeAgentow() {
        return kontoFacade.findAll();
    }
    
    @Override
    @RolesAllowed("getWszystkieMozliweElementyWyposazeniaNieruchomosci")
    public List<ElementWyposazeniaNieruchomosci> getWszystkieMozliweElementyWyposazeniaNieruchomosci() {
        return elementWyposazeniaFacadeLocal.findAll();
    }
    
    @Override
    @RolesAllowed("pobierzWyposazenieNieruchomosci")
    public List<ElementWyposazeniaNieruchomosci> pobierzWyposazenieNieruchomosci(Long idNieruchomosci) {
        return elementWyposazeniaFacadeLocal.znajdzPoIdNieruchomosci(idNieruchomosci);
    }

    @Override
    @PermitAll
    public List<Ogloszenie> pobierzWszytkieOgloszenia() {
        return ogloszenieFacadeLocal.findAll();
    }

    @Override
    @PermitAll
    public String pobierzZalogowanegoUzytkownika() {
        return sessionContext.getCallerPrincipal().getName();
    }
    

    @Override
    @RolesAllowed("dodajDoUlubionych")
    public void dodajDoUlubionych(Ogloszenie ogloszenie) {
        ogloszenieManager.dodajDoUlubionych(ogloszenie);
    }

    @Override
    @RolesAllowed("usunZUlubionych")
    public void usunZUlubionych(Ogloszenie ogloszenie) {
        ogloszenieManager.usunZUlubionych(ogloszenie);
    }

    @Override
    @PermitAll
    public Ogloszenie znajdzOgloszeniePoID(Long id) {
        return ogloszenieFacadeLocal.znajdzPoID(id);
    }

    @Override
    @RolesAllowed("przydzielAgentaDoOgloszenia")
    public void przydzielAgentaDoOgloszenia(Ogloszenie rowData, Konto agent) {
        ogloszenieManager.przydzielAgenta(rowData, agent);
        ogloszenieManager.przeliczAgregat();
    }

    @Override
    @RolesAllowed("deaktywujOgloszenieInnegoUzytkownika")
    public void deaktywujOgloszenieInnegoUzytkownika(Ogloszenie ogloszenie) throws WyjatekSystemu {
        Ogloszenie o = ogloszenieFacadeLocal.find(ogloszenie.getId());
        if(o.getSpotkanieCollection() != null) {
            if(o.getSpotkanieCollection().size() != 0) {
                System.out.println("------------aaa");
                WyjatekSystemu ex=new WyjatekSystemu("blad.toOgloszenieMaSpotkania","MOO");
                throw new WyjatekSystemu("blad.toOgloszenieMaSpotkania", ex, "MOO");
            }
        }
        if(o.getAktywne() == false) {
            throw new WyjatekSystemu("blad.ogloszenieDeaktywowaneWczesniej","MOO");
        }
        else {
            o.setAktywne(false);
            ogloszenieFacadeLocal.edit(o);
            ogloszenieManager.przeliczAgregat();
        }
        
    }

    @RolesAllowed("pobierzTypyOgloszen")
    @Override
    public List<TypOgloszenia> pobierzTypyOgloszen() {
        return typOgloszeniaFacade.findAll();
    }

    @RolesAllowed("pobierzTypyNieruchomosci")
    @Override
    public List<TypNieruchomosci> pobierzTypyNieruchomosci() {
        return typNieruchomosciFacade.findAll();
    }

      @Override
    @RolesAllowed("edytujOgloszenieInnegoUzytkownika")
    public void edytujOgloszenieInnegoUzytkownika(Ogloszenie ogloszenieNowe) throws WyjatekSystemu {
        if (ogloszenieStan == null) {
            throw new WyjatekSystemu("blad.brakOgloszeniaDoEdycji","MOO");
        }

        if (nieruchomoscStan == null) {
            throw new WyjatekSystemu("blad.brakNieruchomosciDoEdycji","MOO");
        }

        nieruchomoscStan.setMiejscowosc(ogloszenieNowe.getNieruchomosc().getMiejscowosc());
        nieruchomoscStan.setUlica(ogloszenieNowe.getNieruchomosc().getUlica());
        nieruchomoscStan.setLiczbaPieter(ogloszenieNowe.getNieruchomosc().getLiczbaPieter());
        nieruchomoscStan.setLiczbaPokoi(ogloszenieNowe.getNieruchomosc().getLiczbaPokoi());
        nieruchomoscStan.setPowierzchniaDzialki(ogloszenieNowe.getNieruchomosc().getPowierzchniaDzialki());
        nieruchomoscStan.setPowierzchniaNieruchomosci(ogloszenieNowe.getNieruchomosc().getPowierzchniaNieruchomosci());
        nieruchomoscStan.setElementWyposazeniaNieruchomosciCollection(ogloszenieNowe.getNieruchomosc().getElementWyposazeniaNieruchomosciCollection());
        List<ElementWyposazeniaNieruchomosci> wyposazenie = elementWyposazeniaFacadeLocal.znajdzPoIdNieruchomosci(nieruchomoscStan.getId());
        List<ElementWyposazeniaNieruchomosci> wyposazenieNowe = new ArrayList(ogloszenieNowe.getNieruchomosc().getElementWyposazeniaNieruchomosciCollection());
        for(int i = 0; i < wyposazenie.size(); i++) {
            boolean jest = false;
            for(int j = 0; j < wyposazenieNowe.size(); j++)
                if(wyposazenieNowe.get(j).getId().equals(wyposazenie.get(i).getId()))
                    jest = true;

            if(!jest) {
                ElementWyposazeniaNieruchomosci el = elementWyposazeniaFacadeLocal.find(wyposazenie.get(i).getId());
                List<Nieruchomosc> n = new ArrayList(el.getNieruchomoscWyposazona());
                for(int j = 0; j < n.size(); j++)
                    if(n.get(j).getId().equals(nieruchomoscStan.getId()))
                        n.remove(j);
                el.setNieruchomoscWyposazonaCollection(n);
                elementWyposazeniaFacadeLocal.edit(el);
            }
        }

        for(int i = 0; i < wyposazenieNowe.size(); i++) {
            boolean jest = false;
            for(int j = 0; j < wyposazenie.size(); j++)
                if(wyposazenieNowe.get(i).getId().equals(wyposazenie.get(j).getId()))
                    jest = true;

            if(!jest) {
                ElementWyposazeniaNieruchomosci el = elementWyposazeniaFacadeLocal.find(wyposazenieNowe.get(i).getId());
                List<Nieruchomosc> n = new ArrayList(el.getNieruchomoscWyposazona());
                n.add(nieruchomoscStan);
                el.setNieruchomoscWyposazonaCollection(n);
                elementWyposazeniaFacadeLocal.edit(el);
                ogloszenieStan = null;
                nieruchomoscStan = null;
            }
        }

        ogloszenieStan.setTytul(ogloszenieNowe.getTytul());
        ogloszenieStan.setCena(ogloszenieNowe.getCena());
        nieruchomoscFacadeLocal.edit(nieruchomoscStan);
        ogloszenieFacadeLocal.edit(ogloszenieStan);
        ogloszenieManager.przeliczAgregat();
        ogloszenieStan = null;
        nieruchomoscStan = null;
    }
    

    @Override
    @RolesAllowed("pobierzOgloszenieDoEdycji")
    public Ogloszenie pobierzOgloszenieDoEdycji(Ogloszenie ogloszenie) throws WyjatekSystemu, IOException, ClassNotFoundException {
        ogloszenieStan = ogloszenieFacadeLocal.find(ogloszenie.getId());
        nieruchomoscStan = nieruchomoscFacadeLocal.find(ogloszenieStan.getNieruchomosc().getId());
        return (Ogloszenie) CloneUtils.deepCloneThroughSerialization(ogloszenieStan);
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
        loger.log(Level.SEVERE, "Transakcja o ID: " + txId + " zostala rozpoczeta");
    }

    /**
     * Metoda logująca czas przed zakończeniem transakcji
     *
     * @throws EJBException
     * @throws RemoteException
     */
    @Override
    public void beforeCompletion() throws EJBException, RemoteException {
        loger.log(Level.SEVERE, "Transakcja o ID: " + txId + " przed zakonczeniem");
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
        loger.log(Level.SEVERE, "Transakcja o ID: " + txId + " zostala zakonczona przez: " + (committed ? "zatwierdzenie" : "wycofanie"));
    }

    // gettery i settery

    @Override
    @PermitAll
    public Konto getKonto(String login) {
        return kontoFacade.znajdzPoLoginie(login);
    }

    @Override
    @PermitAll
    public TypOgloszenia getTypOgloszenia(String typ) {
        return typOgloszeniaFacade.znajdzPoNazwie(typ);
    }

    @Override
    @PermitAll
    public TypNieruchomosci getTypNieruchomosci(String typ) {
        return typNieruchomosciFacade.znajdzPoNazwie(typ);
    }

    @Override
    @PermitAll
    public Boolean czyPosiadaAgenta(Ogloszenie ogloszenie, Konto rowData) {
        Ogloszenie o = ogloszenieFacadeLocal.find(ogloszenie.getId());
        if(o.getIdAgenta().getId().equals(rowData.getId()))
            return true;
        return false;
    }
    
    @Override
    @PermitAll
    public Boolean czyPosiadaJakiegosAgenta(Ogloszenie ogloszenie) {
        Ogloszenie o = ogloszenieFacadeLocal.find(ogloszenie.getId());
        if(o.getIdAgenta()!=null)
            return true;
        return false;
    }
    /***
     * Funckja zwracająca liste agentów
     * @return 
     * @throws javax.naming.NamingException 
     */
    @Override
    @RolesAllowed("pobierzAgentow")
    public List<Konto> getAgenci() throws NamingException{
        PoziomDostepuManager poziomDostepuManager=new PoziomDostepuManager();
        List<Konto> tmp=kontoFacade.findAll();
        List<Konto> tmp2=new ArrayList<>();
        for(Konto k : tmp){
            for(PoziomDostepu p :k.getPoziomDostepuCollection()){
                if(p.getPoziom().equals(poziomDostepuManager.getPoziomyDostepu().get(2)))
                    tmp2.add(k);
            }
        }
        return tmp2;
    }
}
