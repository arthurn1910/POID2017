package pl.lodz.p.it.ssbd2016.ssbd01.wyjatki;

import javax.ejb.ApplicationException;

/**
 * Klasa wyjątku aplikacyjnego który obsługuje wyjątki aplikacyjne i systemowe
 * @author java
 */
@ApplicationException(rollback = true)
public class WyjatekSystemu extends Exception {
    private String miejsce;
    private String message;
    public WyjatekSystemu(String message, String miejsce) {
        super(message);
        this.miejsce=miejsce;
        this.message=message;
    }

    public WyjatekSystemu(String message, Throwable cause, String miejsce) {
        super(message, cause);
        this.miejsce=miejsce;
        this.message=message;
    }

    public WyjatekSystemu(Throwable cause, String miejsce) {
        super(cause);
        this.miejsce=miejsce;
    }

    public String getMiejsce() {
        return miejsce;
    }

    public void setMiejsce(String miejsce) {
        this.miejsce = miejsce;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
