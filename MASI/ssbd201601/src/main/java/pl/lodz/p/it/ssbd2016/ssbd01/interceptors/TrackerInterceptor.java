package pl.lodz.p.it.ssbd2016.ssbd01.interceptors;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu;

/**
 * Interceptor przechwytujący wywołania metod i logujący ich wywołania.
 */
public class TrackerInterceptor {
    @Resource
    private SessionContext sctx;
    private static final Logger loger = Logger.getLogger(TrackerInterceptor.class.getName());
    
    /***
     * Funkcja loguje wywołania metod.
     * @param ictx
     * @return 
     * @throws pl.lodz.p.it.ssbd2016.ssbd01.wyjatki.WyjatekSystemu 
     */
    @AroundInvoke
    public Object traceInvoke(InvocationContext ictx) throws WyjatekSystemu, Exception{
        StringBuilder message = new StringBuilder("********Przechwycone wywołanie metody: ");
        message.append(ictx.getMethod().toString());
        message.append(" użytkownik: " + sctx.getCallerPrincipal().getName());
        message.append(" wartości parametrów: ");

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
            loger.log(Level.SEVERE, "Złapany wyjątek w "+TrackerInterceptor.class.getName(), e);
            throw e;
        } 
    }
}
