package pl.lodz.p.it.ssbd2016.ssbd01.mok.managers;

import pl.lodz.p.it.ssbd2016.ssbd01.Utils.MessageProvider;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.interceptors.TrackerInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Klasa odpowiedzialna za wysyłanie komunikatów mailowych
 * Created by Kamil Rogowski on 28.04.2016.
 *
 * @author Kamil Rogowski
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class NotyfikacjaService implements NotyfikacjaServiceLocal {
    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(NotyfikacjaService.class.getName());

    /**
     * Odpowiedzialna za utworzenie sesji email
     */
    private Session session;

    /**
     * Model wiadomości
     */
    private MimeMessage message;

    /**
     * Ujednolicony format daty w wiadomościach
     */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public NotyfikacjaService() {
        initProperties();
    }

    /**
     * metodda inicjalizuje sesję mailową
     */
    private void initProperties() {
        Properties properties = new Properties();
        final String username = MessageProvider.getConfig("notyfikacja.email");
        final String password = MessageProvider.getConfig("notyfikacja.haslo");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", MessageProvider.getConfig("notyfikacja.host"));
        properties.put("mail.smtp.port", MessageProvider.getConfig("notyfikacja.port"));

        session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        message = new MimeMessage(session);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PermitAll
    public void wyslijPowiadomienieRejestracji(Konto konto) throws MessagingException{
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(konto.getEmail())); //adres odbiorcy zmienic tutaj wartosc jesli chcecie zamockowac
        message.setSubject(MessageProvider.getMessage("notyfikacja.rejestracja.tytul"));
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent( MessageProvider.getMessage("notyfikacja.konto.login") + " " + konto.getLogin()
                + " " + MessageProvider.getMessage("notyfikacja.rejestracja.tresc") + " " +
                dateFormat.format(konto.getDataUtworzenia()) + ".", "text/html; charset=utf-8" );

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart( messageBodyPart );
        message.setContent( multipart );
        Transport.send(message);
    }

    /**
     * {@inheritDoc}
     */
    @RolesAllowed("zablokujKonto")
    @Override
    public void wyslijPowiadomienieZablokowaniaKonta(Konto konto) throws MessagingException{

        message.setFrom(new InternetAddress(MessageProvider.getConfig("notyfikacja.email")));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(konto.getEmail()));

        message.setSubject(MessageProvider.getMessage("notyfikacja.zablokowane.tytul"),"utf-8");

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(MessageProvider.getMessage("notyfikacja.konto.login") + " " + konto.getLogin()
                + " " + MessageProvider.getMessage("notyfikacja.zablokowane.tresc") + " "
                + dateFormat.format(new Date()) + ".", "text/html; charset=utf-8" );

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart( messageBodyPart );
        message.setContent( multipart );
        Transport.send(message);
    }

    /**
     * {@inheritDoc}
     */
    @RolesAllowed("odblokujKonto")
    @Override
    public void wyslijPowiadomienieAktywacjiKonta(Konto konto) throws MessagingException{
        message.setFrom(new InternetAddress(MessageProvider.getConfig("notyfikacja.email")));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(konto.getEmail()));
        message.setSubject(MessageProvider.getMessage("notyfikacja.odblokowane.tytul"));

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(MessageProvider.getMessage("notyfikacja.konto.login") + " " + konto.getLogin()
                + " " + MessageProvider.getMessage("notyfikacja.zablokowane.tresc") + " "
                + dateFormat.format(new Date()) + ".", "text/html; charset=utf-8" );
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart( messageBodyPart );
        message.setContent( multipart );
        Transport.send(message);
    }
}
