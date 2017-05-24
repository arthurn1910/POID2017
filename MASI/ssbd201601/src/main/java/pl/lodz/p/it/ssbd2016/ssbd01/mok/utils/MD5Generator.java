package pl.lodz.p.it.ssbd2016.ssbd01.mok.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Klasa użytkowa. Umożliwia generowania skrótów MD5 dla haseł
 */
public class MD5Generator {
    
    /**
     * Metoda generująca skrót MD5
     * @param haslo string z wartością do wygenerowania skrótu
     * @return wygenerowany skrót
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     */
    public static String generateMD5Hash(String haslo) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        byte[] bajtyWiadomosci = null;
        bajtyWiadomosci = haslo.getBytes("UTF-8");
        MessageDigest generatorMD = null;
        generatorMD = MessageDigest.getInstance("MD5");
            
        byte[] zaszyfrowaneBajty = generatorMD.digest(bajtyWiadomosci);
        BigInteger wartoscLiczbowa = new BigInteger(1,zaszyfrowaneBajty);
        return wartoscLiczbowa.toString(16);    
    }
}