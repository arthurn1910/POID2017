/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.Utils;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;

/**
 *
 * @author A638852
 */
public class CloneUtilsTest {
    
    public CloneUtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

//    /**
//     * Test of deepCloneThroughSerialization method, of class CloneUtils.
//     */
//    @Test
//    public void testDeepCloneThroughSerialization() throws Exception {
//        System.out.println("deepCloneThroughSerialization");
//        Serializable source = new Konto(1L);
//        Object result = CloneUtils.deepCloneThroughSerialization(source);
//        assertNotSame(source, result);
//    }
//    
    /**
     * Test of deepCloneThroughSerializationW method, of class CloneUtils.
     */
    @Test
    public void testDeepCloneThroughSerializationW() throws Exception {
        System.out.println("deepCloneThroughSerializationW");
        long start = System.currentTimeMillis();
        Serializable source = new Konto(1L);
        Object result = CloneUtils.deepCloneThroughSerialization(source);
        long time = System.currentTimeMillis() - start;
        time = System.currentTimeMillis() - start;
        System.out.println(time);
        assertTrue(time<50);
    }
}
