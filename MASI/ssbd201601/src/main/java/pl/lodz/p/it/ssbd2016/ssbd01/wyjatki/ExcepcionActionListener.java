package pl.lodz.p.it.ssbd2016.ssbd01.wyjatki;

import com.sun.faces.application.ActionListenerImpl;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Funkcja actionListener której założeniem jest przechwyt wyjątku, zapisanie jego logów oraz wyświetlenie strony wyjątku.
 * @author java
 */
public class ExcepcionActionListener extends ActionListenerImpl implements ActionListener{
    Logger logger = Logger.getLogger(ExcepcionActionListener.class.getName());;
    
    /***
     * Metoda wyłapująca i obsługująca zdarzenia
     * @param event 
     */
    @Override
    public void processAction(ActionEvent event){
        try{
            super.processAction(event);
        }catch(FacesException fe){
            try {
                WyjatekSystemu e=(WyjatekSystemu) fe.getCause().getCause();
                logger.log(Level.SEVERE, fe.getCause().getCause().getMessage()+" !!!! "+e.getMiejsce()+"  Złapany wyjątek w "+ExcepcionActionListener.class.getName(), fe.getCause());
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
                if(e.getMiejsce().equals("MOK"))
                    externalContext.redirect(origRequest.getContextPath() + "/wyjatki/wyjatek.xhtml");
                else if(e.getMiejsce().equals("MOO"))
                    externalContext.redirect(origRequest.getContextPath() + "/wyjatki/wyjatekMOO.xhtml");
                else
                    externalContext.redirect(origRequest.getContextPath() + "/wyjatki/wyjatekMOS.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ExcepcionActionListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
