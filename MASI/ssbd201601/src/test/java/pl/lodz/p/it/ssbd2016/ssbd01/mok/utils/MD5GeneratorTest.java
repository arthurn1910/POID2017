/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.mok.utils;

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
public class MD5GeneratorTest {
    
    public MD5GeneratorTest() {
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
     * Test of generateMD5Hash method, of class MD5Generator.
     */
    @Test
    public void testGenerateMD5Hash() throws Exception {
        System.out.println("generateMD5Hash");
        long start = System.currentTimeMillis();
        String haslo = "zaq1@WSX";
        String expResult = "9e38e8d688743e0d07d669a1fcbcd35b";
        String result = MD5Generator.generateMD5Hash(haslo);
        long time = System.currentTimeMillis() - start;
        System.out.println(time);
        assertTrue(time<50);
    }
    
}
