package pl.lodz.p.it.ssbd2016.ssbd01.mok.managers;

import pl.lodz.p.it.ssbd2016.ssbd01.encje.HistoriaLogowania;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2016.ssbd01.mok.fasady.HistoriaLogowaniaFacadeLocal;
import pl.lodz.p.it.ssbd2016.ssbd01.mok.fasady.KontoFacadeLocal;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.context.FacesContext;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kamil Rogowski on 30.04.2016.
 * Serwis przetwarzający IP z requestu i wiązanie go z kontem
 *
 * @author Kamil Rogowski
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AdresIpService implements AdresIpServiceLocal {
    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(AdresIpService.class.getName());

    @EJB
    private HistoriaLogowaniaFacadeLocal historiaLogowaniaFacade;

    @EJB
    private KontoFacadeLocal kontoFacade;
    /**
     * nagłówki do sprawdzenia gdyby użytkownik był za proxy
     */
    private final String[] HEADERS_TO_CHECK = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};

    /**
     * Pole trzymające request z którego przyszło żądanie logowania
     */
    private HttpServletRequest request;

    /**
     * {@inheritDoc}
     */
    @Override
    @PermitAll
    public String getClientIpAddress() {
        for (String header : HEADERS_TO_CHECK) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PermitAll
    public void processIpAdress(String login) {
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        final String clientIpAddress = getClientIpAddress();
        Konto kontoDocelowe = kontoFacade.znajdzPoLoginie(login);
        HistoriaLogowania historiaLogowania = historiaLogowaniaFacade.findyByIdKonta(kontoDocelowe);

        if (historiaLogowania == null) {
            historiaLogowania = new HistoriaLogowania();
        }

        historiaLogowania.setAdresIp(clientIpAddress);
        historiaLogowania.setIdKonta(kontoDocelowe);
        historiaLogowania.setDataLogowania(new Date());
        historiaLogowaniaFacade.edit(historiaLogowania);

        LOGGER.log(Level.INFO, "konto o loginie " + login + " i adresie IP "
                + clientIpAddress + " zostało zalogowane dnia " + new Date());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RolesAllowed("pobierzHistorieLogowanUzytkownikow")
    public List<HistoriaLogowania> pobierzHistorieLogowanUzytkownikow() {
        return historiaLogowaniaFacade.findAll();
    }

}
