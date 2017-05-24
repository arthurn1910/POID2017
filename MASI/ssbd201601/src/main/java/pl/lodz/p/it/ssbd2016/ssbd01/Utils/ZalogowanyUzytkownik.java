package pl.lodz.p.it.ssbd2016.ssbd01.Utils;

import javax.faces.context.FacesContext;

/**
 * Created by Kamil Rogowski on 26.05.2016.
 * Klasa użytkowa, która zwraca aktualnie zalogowanego użytkownika
 */

public class ZalogowanyUzytkownik {

    private ZalogowanyUzytkownik(){

    }

    /**
     * Zwraca aktualnie zalogowanego użytkownika
     * @return login użytkownika
     */
    public static String getLoginZalogowanegoUzytkownika(){

        return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }
}
