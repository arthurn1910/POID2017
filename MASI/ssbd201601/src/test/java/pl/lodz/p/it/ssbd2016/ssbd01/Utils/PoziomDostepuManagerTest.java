/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.Utils;

import static java.lang.Boolean.TRUE;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto;
import pl.lodz.p.it.ssbd2016.ssbd01.encje.PoziomDostepu;
import pl.lodz.p.it.ssbd2016.ssbd01.mok.fasady.KontoFacade;

/**
 *
 * @author A638852
 */
public class PoziomDostepuManagerTest {
    
    public PoziomDostepuManagerTest() {
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
//     * Test of stworzPoziomDostepu method, of class PoziomDostepuManager.
//     */
//    @Test
//    public void testStworzPoziomDostepu() throws Exception {
//        System.out.println("stworzPoziomDostepu");
//        long start = System.currentTimeMillis();
//        String poziom = "AGENT";
//        PoziomDostepuManager instance = new PoziomDostepuManager();
//        PoziomDostepu result = instance.stworzPoziomDostepu(poziom);        
//        long time = System.currentTimeMillis() - start;
//        time = System.currentTimeMillis() - start;
//        System.out.println(time);
//        assertTrue(time<50);
//    }
//
//    /**
//     * Test of czyMoznaDodacPoziom method, of class PoziomDostepuManager.
//     */
//    @Test
//    public void testCzyMoznaDodacPoziom() throws NamingException {
//        KontoFacade kontoFacade=new KontoFacade();
//            System.out.println("czyMoznaDodacPoziom!");
//            long start = System.currentTimeMillis();
//            Konto konto = kontoFacade.znajdzPoLoginie("agent");
//            String nowyPoziom = "MENADZER";
//            PoziomDostepuManager instance = new PoziomDostepuManager();
//            boolean result = instance.czyMoznaDodacPoziom(konto, nowyPoziom);
//            long time = System.currentTimeMillis() - start;
//            time = System.currentTimeMillis() - start;
//            System.out.println(time);
//            assertTrue(time<50);
//        
//    }


}
