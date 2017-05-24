package pl.lodz.p.it.ssbd2016.ssbd01.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Klasa użytkowa umożliwia wykonanie głębokiej kopii obiektów
 */
public class CloneUtils {
    
    /**
     * Metoda wykonuje głęboką kopie obiektu wykorzystując serializację
     * @param source obiekt do wykonania kopii
     * @return kopia obiektu 
     * @throws java.io.IOException 
     * @throws java.lang.ClassNotFoundException 
     */
    public static Object deepCloneThroughSerialization(Serializable source) throws IOException, ClassNotFoundException{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(source);
 
            //De-serialization of object
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            return in.readObject();
    }    
}
