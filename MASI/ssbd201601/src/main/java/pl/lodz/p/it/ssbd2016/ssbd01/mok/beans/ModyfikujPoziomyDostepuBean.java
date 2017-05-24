package pl.lodz.p.it.ssbd2016.ssbd01.mok.beans;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.Utils.PoziomDostepuManager;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Klasa ta jest wykorzystywana do modyfikacji poziomów dostępu dla wybranego konta
 */
@Named
@RequestScoped
public class ModyfikujPoziomyDostepuBean {
    
    @Inject
    private UzytkownikSession uzytkownikSession;
    
    private Konto konto;
    
    private DataModel<String> poziomyDostepuDataModel;

    /**
     * Metoda inizjalizująca dostępne poziomy dostępu i konta, dla którego
     * będą modyfikowane poziomy dostępu
     */
    @PostConstruct
    private void initModel(){
        try {
            PoziomDostepuManager tmp=new PoziomDostepuManager();
            poziomyDostepuDataModel = new ListDataModel<String>(tmp.getPoziomyDostepu());
            konto = uzytkownikSession.getWybraneKonto();
        } catch (Exception ex) {
            uzytkownikSession.setException(ex);
            Logger logger = Logger.getLogger(ModyfikujPoziomyDostepuBean.class.getName());;
            logger.log(Level.SEVERE, "Złapany wyjątek w "+ModyfikujPoziomyDostepuBean.class.getName(), ex.getCause());
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            try {
                externalContext.redirect("/ssbd201601/wyjatki/wyjatek.xhtml");
            } catch (IOException ex1) {
                Logger.getLogger(ModyfikujPoziomyDostepuBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
    /**
     * Handler dla przycisku dołącz. Metoda dołącza poziom dostępu do konta 
     * @return 
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu 
     */
    public String dodajPoziomDostepu() throws WyjatekSystemu, NamingException{
        uzytkownikSession.dodajPoziomDostepu(konto, poziomyDostepuDataModel.getRowData());
        initModel();
        return "modyfikujPoziomyDostepu";
    }
    
    /**
     * Handler dla przycisku odłącz. Metoda odłączająca poziom dostępu do konta 
     * @return 
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu 
     */
    public String odlaczPoziomDostepu() throws WyjatekSystemu, NamingException {
        uzytkownikSession.odlaczPoziomDostepu(konto, poziomyDostepuDataModel.getRowData());
        initModel();
        return "modyfikujPoziomyDostepu";
    }
    
    /**
     * Metoda dla widoku definiująca czy konto posiada dany poziom dostępu. 
     * Zmienia dostępne klawisze między dołącz i odłącz
     * @return  decyzcja czy konto posiada aktywny poziom dostępu
     * @throws javax.naming.NamingException
     */
    public boolean czyPosiadaAktywnyPoziomDostepu() throws NamingException {
        try {
            PoziomDostepuManager tmp=new PoziomDostepuManager();
            return tmp.czyPosiadaAktywnyPoziomDostepu(konto, poziomyDostepuDataModel.getRowData());
        } catch (NamingException ex) {
            uzytkownikSession.setException(ex);
            throw ex;
        }
    }
    
    // Gettery i Settery    
    
    public DataModel<String> getPoziomyDostepuDataModel() {
        return poziomyDostepuDataModel;
    }    
}
