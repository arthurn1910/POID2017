/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.Utils;

import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author A638852
 */
public class MessageProviderTest {
    
    public MessageProviderTest() {
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


    /**
     * Test of getMessage method, of class MessageProvider.
     */
    @Test
    public void testGetMessage() throws InterruptedException {
        System.out.println("getMessage");
        long start = System.currentTimeMillis();
        String key = "notyfikacja.rejestracja.tytul";
        String result = MessageProvider.getMessage(key);
        long time = System.currentTimeMillis() - start;
        System.out.println(time);
        assertTrue(time<50);
    }
    
}
