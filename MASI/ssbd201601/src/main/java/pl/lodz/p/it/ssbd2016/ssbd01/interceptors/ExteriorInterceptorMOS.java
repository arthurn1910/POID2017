package pl.lodz.p.it.ssbd2016.ssbd01.interceptors;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.TransactionRolledbackLocalException;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import org.eclipse.persistence.exceptions.OptimisticLockException;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;
import org.postgresql.util.PSQLException;
import sun.security.provider.certpath.SunCertPathBuilderException;

/**
 * Interceptor zewnętrzny 
 */
public class ExteriorInterceptorMOS {
    @Resource
    private SessionContext sctx;
    private static final Logger loger = Logger.getLogger(TrackerInterceptor.class.getName());
    /***
     * Funkcja przechwytująca wywołania wyjatków, opakowywania wyjątków nieweryfikowalnych,
     * logująca ich wywołania oraz przesyłająca wyjątek dalej.
     * @param ictx
     * @return 
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu 
     */
    @AroundInvoke
    public Object traceInvoke(InvocationContext ictx) throws WyjatekSystemu, Exception{
        StringBuilder message = new StringBuilder("Przechwycone wywołanie metody: ");
        message.append(ictx.getMethod().toString());
        message.append(" użytkownik: " + sctx.getCallerPrincipal().getName());
        message.append(" wartości parametrów: ");
        Logger lg=Logger.getLogger("javax.enterprise.system.container.web.faces");
        if (ictx.getParameters() != null) {
            for (Object param : ictx.getParameters()) {
                if (null == param) {
                    message.append("null ");
                } else {
                    message.append(param.toString() + " ");
                }
            }
        }
        Object result;
        try {
            result = ictx.proceed();
            message.append(" wartość zwrócona: ");
             if (null == result) {
                message.append("null ");
            } else {
                message.append(result.toString() + " ");
            }
            loger.severe(message.toString());
        
            return result;
        }catch (Exception e) {
            Throwable tmp1=e, tmp2=e;
            while(tmp1.getCause()!=null){
                tmp2=tmp1;
                tmp1=tmp1.getCause();
            }
            tmp2=tmp2.getCause();
            loger.log(Level.SEVERE, "Złapany wyjątek w "+ExteriorInterceptorMOS.class.getName(), e);
            WyjatekSystemu exc=null;
            if(tmp2 instanceof WyjatekSystemu){
                throw (WyjatekSystemu) e;
            } else if(tmp2 instanceof RemoteException){
                exc=new WyjatekSystemu("blad.RemoteException", tmp2,"MOS");
            } else if(tmp2 instanceof NamingException){
                exc=new WyjatekSystemu("blad.niewykonanaOperacja", tmp2,"MOS");
            } else if(tmp2 instanceof OptimisticLockException){
                exc=new WyjatekSystemu("blad.OptimisticLockException", tmp2,"MOS");
            } else if(tmp2 instanceof UnsupportedEncodingException){
                exc=new WyjatekSystemu("blad.nieobslugiwaneKodowanie", tmp2,"MOS");
            } else if(tmp2 instanceof NoSuchAlgorithmException){
                exc=new WyjatekSystemu("blad.brakAlgorytmuKodowania", tmp2,"MOS");
            } else if(tmp2 instanceof SunCertPathBuilderException){
                exc=new WyjatekSystemu("blad.wyslaniaWiadomosci", tmp2,"MOS");
            } else if(tmp2 instanceof EJBAccessException){
                exc=new WyjatekSystemu("blad.EJBAccessException", tmp2,"MOS");
            } else if(tmp2 instanceof TransactionRolledbackLocalException){
                exc=new WyjatekSystemu("blad.EJBTransactionRolledbackException", tmp2,"MOS");
            } else if(tmp2 instanceof PSQLException){
                String mes="blad.PSQLException";
                if(tmp2.getMessage().substring(0, 62).equals("BŁĄD: podwójna wartość klucza narusza ograniczenie unikalności"))
                    mes="blad.naruszenieUniq";
                exc=new WyjatekSystemu(mes, tmp2,"MOS");
            } else if(tmp2 instanceof MessagingException){
                exc=new WyjatekSystemu("blad.wysylanieWiadomosci",tmp2,"MOS");
            } else if(tmp2 instanceof ClassNotFoundException){
                exc=new WyjatekSystemu("blad.klasaNieznaleziona",tmp2,"MOS");
            } else if(tmp2 instanceof IOException){
                exc=new WyjatekSystemu("blad.bladPliku",tmp2,"MOS");
            }else if(tmp2 instanceof NullPointerException){
                exc=new WyjatekSystemu("blad.NullPointerExcepton", tmp2,"MOS");
            } else if(tmp2 instanceof EJBException){
                exc=new WyjatekSystemu("blad.EJBException", tmp2,"MOS");
            } else{
                exc=new WyjatekSystemu("blad.nieobsluzonyWyjatek", tmp2,"MOS");
            }
            throw exc;
        } 
    }
}
