package pl.lodz.p.it.ssbd2016.ssbd01.mok.managers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.naming.NamingException;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.PoziomDostepu;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2016.ssbd01.mok.fasady.KontoFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.mok.fasady.PoziomDostepuFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.mok.utils.MD5Generator;
import pl.lodz.p.it.ssbd2016.ssbd01.Utils.PoziomDostepuManager;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Klasa pośrednicząca miedzy MOKEndpoint a fasadami. Przetwarza niezbędne dane.
 * Implementuje interfejs KontoManagaerLocal
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class KontoManager implements KontoManagerLocal {

    @EJB
    private KontoFacadeLocal kontoFacade;

    @EJB
    private PoziomDostepuFacadeLocal poziomDostepuFacade;

    @Resource
    private SessionContext sessionContext;

    @Override
    @RolesAllowed("zmienMojeHaslo")
    public void zmienMojeHaslo(Konto konto, String noweHaslo, String stareHasloWpisane) throws WyjatekSystemu, NoSuchAlgorithmException,UnsupportedEncodingException {
        if (!konto.getLogin().equals(sessionContext.getCallerPrincipal().getName())) {
            WyjatekSystemu ex = new WyjatekSystemu("blad.niezgodnyLogin","MOK");
            throw new WyjatekSystemu("blad.niezgodnyLogin", ex,"MOK");
        }
        String stareHaslo = konto.getHaslo();
        String hashedPassword = null;

        hashedPassword = MD5Generator.generateMD5Hash(noweHaslo);
        stareHasloWpisane = MD5Generator.generateMD5Hash(stareHasloWpisane);
        if (stareHasloWpisane.equals(stareHaslo)) {
            konto.setHaslo(hashedPassword);
            kontoFacade.edit(konto);
        } else {
            WyjatekSystemu ex = new WyjatekSystemu("blad.niezgodneHasla","MOK");
            throw new WyjatekSystemu("blad.niezgodneHasla", ex,"MOK");
        }
    }

    @Override
    @RolesAllowed("zmienHaslo")
    public void zmienHaslo(Konto konto, String noweHaslo) throws NoSuchAlgorithmException,UnsupportedEncodingException  {
        String noweZahashowanehaslo = MD5Generator.generateMD5Hash(noweHaslo);
        konto.setHaslo(noweZahashowanehaslo);
        kontoFacade.edit(konto);
    }

    @Override
    @PermitAll
    public void rejestrujKontoKlienta(Konto konto) throws NoSuchAlgorithmException,UnsupportedEncodingException, NamingException {
            konto.setAktywne(true);
            konto.setPotwierdzone(false);
            konto.setHaslo(MD5Generator.generateMD5Hash(konto.getHaslo()));

            PoziomDostepuManager poziomDostepuManager = new PoziomDostepuManager();

            PoziomDostepu poziomDostepu = poziomDostepuManager.stworzPoziomDostepuKlient();
            poziomDostepu.setKontoId(konto);
            poziomDostepu.setAktywny(true);
            kontoFacade.create(konto);
            kontoFacade.flush();
            //poziomDostepuFacade.create(poziomDostepu);

            konto.getPoziomDostepuCollection().add(poziomDostepu);
    }

    @Override
    @RolesAllowed("utworzKonto")
    public void utworzKonto(Konto konto, List<String> poziomyDostepu) throws NoSuchAlgorithmException,UnsupportedEncodingException, NamingException, WyjatekSystemu {
        PoziomDostepuManager tmp = new PoziomDostepuManager();
        if (tmp.czyPoprawnaKombinacjaPoziomowDostepu(poziomyDostepu)) {
            //try{
            konto.setAktywne(true);
            konto.setPotwierdzone(false);
            konto.setHaslo(MD5Generator.generateMD5Hash(konto.getHaslo()));

            for (String poziomDostepuStr : poziomyDostepu) {
                PoziomDostepu poziomDostepu = tmp.stworzPoziomDostepu(poziomDostepuStr);
                poziomDostepu.setKontoId(konto);
                poziomDostepu.setAktywny(true);
                konto.getPoziomDostepuCollection().add(poziomDostepu);
            }
            kontoFacade.create(konto);
            kontoFacade.flush();
        }
    }

    @Override
    @RolesAllowed("pobierzWszystkieKonta")
    public List<Konto> pobierzWszystkie() {
        return kontoFacade.findAll();
    }

    @Override
    @RolesAllowed("pobierzPodobneKonta")
    public List<Konto> znajdzPodobne(Konto konto) {
        Konto kontoWyszukaj = new Konto();
        if (konto.getImie() == null) {
            kontoWyszukaj.setImie("%%");
        } else {
            kontoWyszukaj.setImie("%" + konto.getImie().toLowerCase() + "%");
        }

        if (konto.getNazwisko() == null) {
            kontoWyszukaj.setNazwisko("%%");
        } else {
            kontoWyszukaj.setNazwisko("%" + konto.getNazwisko().toLowerCase() + "%");
        }

        if (konto.getEmail() == null) {
            kontoWyszukaj.setEmail("%%");
        } else {
            kontoWyszukaj.setEmail("%" + konto.getEmail().toLowerCase() + "%");
        }

        if (konto.getTelefon() == null) {
            kontoWyszukaj.setTelefon("%%");
        } else {
            kontoWyszukaj.setTelefon("%" + konto.getTelefon() + "%");
        }
        kontoWyszukaj.setAktywne(konto.getAktywne());
        kontoWyszukaj.setPotwierdzone(konto.getPotwierdzone());

        return kontoFacade.znajdzPodobne(kontoWyszukaj);
    }

    @Override
    @RolesAllowed("dodajPoziomDostepu")
    public void dodajPoziomDostepu(Konto konto, String poziom) throws WyjatekSystemu, NamingException {
        PoziomDostepuManager tmp = new PoziomDostepuManager();
        if (tmp.czyPosiadaPoziomDostepu(konto, poziom)) {
            // Posiadamy dany poziom
            PoziomDostepu aktualnyPoziom = tmp.pobierzPoziomDostepu(konto, poziom);
            // Sprawdzamy czy poziom jest aktywny i czy możemy dołączyć dany poziom
            if (!aktualnyPoziom.getAktywny() && tmp.czyMoznaDodacPoziom(konto, poziom)) {
                // Jeśli tak aktywujemy posiadany już poziom
                PoziomDostepu odlaczanyPoziom = poziomDostepuFacade.find(aktualnyPoziom.getId());
                odlaczanyPoziom.setAktywny(true);
            } else {
                // Jeśli nie zwracamy błąd
                WyjatekSystemu ex = new WyjatekSystemu("blad.PoziomDostepuDodanie","MOK");
                throw new WyjatekSystemu("blad.PoziomDostepuDodanie", ex,"MOK");
            }
        } else // Nie posiadamy danego poziomu dostępu
        // Sprawdzamy czy możemy taki poziom dodać
        {
            if (tmp.czyMoznaDodacPoziom(konto, poziom)) {
                Konto aktualneKonto = kontoFacade.znajdzPoLoginie(konto.getLogin());
                //Tworzymy i dodajemy nowy poziom dostępu
                PoziomDostepu nowyPoziom = tmp.stworzPoziomDostepu(poziom);
                nowyPoziom.setKontoId(aktualneKonto);
                nowyPoziom.setAktywny(true);
                poziomDostepuFacade.create(nowyPoziom);

                aktualneKonto.getPoziomDostepuCollection().add(nowyPoziom);
            } else {
                // Jeśli nie udało się dodać poziom dostępu zwracamy błąd
                WyjatekSystemu ex = new WyjatekSystemu("blad.PoziomDostepuDodanie","MOK");
                throw new WyjatekSystemu("blad.PoziomDostepuDodanie", ex,"MOK");
            }
        }
    }

    @Override
    @RolesAllowed("odlaczPoziomDostepu")
    public void odlaczPoziomDostepu(Konto konto, String poziom) throws WyjatekSystemu, NamingException {
        PoziomDostepuManager tmp = new PoziomDostepuManager();
        if (tmp.czyPosiadaPoziomDostepu(konto, poziom)) {

            PoziomDostepu aktualnyPoziom = tmp.pobierzPoziomDostepu(konto, poziom);

            if (aktualnyPoziom.getAktywny()) {
                // Jeśli poziom jest aktywny to go dezaktywujemy
                PoziomDostepu odlaczanyPoziom = poziomDostepuFacade.find(aktualnyPoziom.getId());
                odlaczanyPoziom.setAktywny(false);
            } else {
                // Jeśli poziom jest nieaktywny zwracamy błąd
                WyjatekSystemu ex = new WyjatekSystemu("blad.PoziomDostepuOlaczenie","MOK");
                throw new WyjatekSystemu("blad.PoziomDostepuOlaczenie", ex,"MOK");

            }
        } else {
            // Jeśli nie posiadamy danego poziomu dostępu zwracamy błąd
            WyjatekSystemu ex = new WyjatekSystemu("blad.PoziomDostepuOlaczenie","MOK");
            throw new WyjatekSystemu("blad.PoziomDostepuOlaczenie", ex,"MOK");
        }
    }
}
