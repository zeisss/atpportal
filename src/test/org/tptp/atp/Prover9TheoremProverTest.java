package org.tptp.atp;

import java.io.*;
import org.junit.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.tptp.model.*;
import org.tptp.atp.prover9.*;

public class Prover9TheoremProverTest {
    @Test
    public void testProver9Available() {
        boolean found = false;
        
        for ( TheoremProver p : TheoremProver.getProvers()) {
            if ( p.getName().equals("Prover9")){
                found = true;
                assertNotNull(p.getDescription());
                assertNotNull(p.getVersion());
            }
        }
        
        if ( !found)
            fail("Unable to find prover9!");
    }
    
    private TheoremProver getProver9() {
        return new Prover9TheoremProver();
    }
    
    @Test
    public void testExampleExecution() throws IOException {
        Set<Formula> formulas = new HashSet<Formula>();
        formulas.add(new Formula("McKenzie_1", "x v (y ^ (x ^ z)) = x"));
        formulas.add(new Formula("McKenzie_2", "x ^ (y v (x v z)) = x"));
        formulas.add(new Formula("McKenzie_3", "((y ^ x) v (x ^ z)) v x = x"));
        formulas.add(new Formula("McKenzie_4", "((y v x) ^ (x v z)) ^ x = x"));
        
        String goal = "(x ^ y) ^ z = x ^ (y ^ z)";
        
        Properties properties = new Properties();
        properties.setProperty("path", "/Users/zeisss/Documents/Bachelorarbeit/Projekt/ldar");
        
        TheoremProver prover9 = getProver9();
        Result result = prover9.execute(properties, goal, formulas );
        assertNotNull(result);
        assertTrue(result.isSuccessfull());
        assertNotNull(result.getProofSteps());
        assertNotNull(result.getProvedFormulas());
        assertNotNull(result.getUsedFormulas());
        assertEquals(59, result.getProofSteps().size());
    }
    
    
}