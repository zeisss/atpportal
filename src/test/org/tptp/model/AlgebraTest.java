package org.tptp.model;

import org.junit.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assume.*;


import org.tptp.model.postgres.*;

public class AlgebraTest extends AbstractModelTest {
    public AlgebraTest() {}
    
    @Test
    public void testAlgebraSetter() {
        Algebra algebra = new Algebra();
        assertEquals(-1, algebra.getId());
        assertEquals("", algebra.getName());
        
        Algebra algebra2 = new Algebra(214123, "DEMOSTRING", "Sample Comment");
        assertEquals(214123, algebra2.getId());
        assertEquals("DEMOSTRING", algebra2.getName());
        assertEquals("Sample Comment", algebra2.getComment());
    }
    
    @Test
    public void testAlgebraEquals() {
        Algebra a = new Algebra("Kleensche Algebra");
        Algebra b = new Algebra("Klee" + "nsche Algebra");
        Algebra c = new Algebra("Boolsche Algebra");
        
        assertFalse(a == b);
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.equals(b));
        
        assertNotSame(b,c);
        assertNotSame(a,c);
        
        b.setName("FOOLSCHE ALGEBRA");
        assertFalse(a.equals(b));
        
        Algebra d = new Algebra("Kleensche Algebra", "");
        assertEquals(a, d);
    }
    
    @Test
    public void testAlgebraRepoAvailable() {
        assertNotNull("Repository instance must not be null", AlgebraRepository.getInstance());
    }
    
    /**
     * Tests a bug where an algebra without a comment couldn't be saved.
     */
    @Test
    public void testAlgebraSaveWithoutComment() {
        AlgebraRepository repo = AlgebraRepository.getInstance();
        assumeNotNull(repo);
        
        
        Algebra a1 = new Algebra("Algebra Test - Comment Error #1");
        repo.save(a1);
        repo.delete(a1);
        
        Algebra a2 = new Algebra("Algebra Test - Comment Error #2", "");
        repo.save(a2);
        repo.delete(a2);
        
        Algebra a3 = new Algebra("Algebra Test - Comment Error #2", "DUMMY TEST");
        repo.save(a3);
        repo.delete(a3);
    }
 
    @Test
    public void testAlgebraRepoCRUD() {
        AlgebraRepository repo = AlgebraRepository.getInstance();
        assumeNotNull(repo);
        
        Algebra algebra = new Algebra("Kleensche TESTCRUD");
        repo.save(algebra);
        try
        {
            assertNotSame("Algebra id must not be -1 after saving", -1l, algebra.getId());
            
            Set<Algebra> algebren = repo.getAll();
            assertTrue("getAll() must return the saved algebra", algebren.contains(algebra));
            
            algebra.setName("Boolsche TESTCRUD");
            repo.update(algebra);
            
            Algebra algebra2 = repo.get(algebra.getId());
            assertNotNull("Algebra with id must be loadable", algebra2);
            assertEquals("Name update failed", "Boolsche TESTCRUD", algebra2.getName());
            
        }
        finally {
            repo.delete(algebra);
            assertEquals("ID must be reset to -1 after delete", -1l, algebra.getId());           
        }
    }
    
    @Test
    @Ignore
    public void testExceptionSave() {
        AlgebraRepository repo = AlgebraRepository.getInstance();
        assumeNotNull(repo);
        // TODO: Implement the tests, that the exceptions get raised
    }
    
    @Test
    public void testAlgebraFormulaLinking() {
        // Get the repositories 
        AlgebraRepository aRepo = AlgebraRepository.getInstance();
        assumeNotNull(aRepo);
        
        FormulaRepository fRepo = FormulaRepository.getInstance();
        assertNotNull("FormulaRepository must not null", fRepo);
        
        // Create the test objects
        Algebra algebra = new Algebra("Kleensche TESTCRUDLinking");
        aRepo.save(algebra);
        
        Algebra algebra2 = new Algebra("Boolsche TESTCRUDLinking");
        aRepo.save(algebra2);
        
        Formula formula = new Formula("DEMO Formula", "x = y^2");
        fRepo.save(formula);
        
        // Save a link
        aRepo.link(algebra, formula, true);  // Link as axiom
        aRepo.link(algebra2, formula, false); // Link as theorem
        
        // Test
        Set<Formula> setF1 = fRepo.getAxiomsByAlgebra(algebra);
        assertEquals(1, setF1.size());
        assertTrue("Formula should have been saved as axiom for the given algebra", setF1.contains(formula));
        assertEquals("Theoremset for algebra1 should be empty", 0, fRepo.getTheoremsByAlgebra(algebra).size());
        
        Set<Formula> setF2 = fRepo.getTheoremsByAlgebra(algebra2);
        assertEquals(1, setF2.size());
        assertTrue("Formula should have been saved as axiom for the given algebra", setF2.contains(formula));
        assertEquals("AxiomSet for Algebra2 should be empty.", 0, fRepo.getAxiomsByAlgebra(algebra2).size());
        
        // Perform the unlinking
        aRepo.unlink(algebra, formula);
        aRepo.unlink(algebra2, formula);
        
        // Retest
        assertEquals("AxiomSet should be empty for Algebra 1", 0, fRepo.getAxiomsByAlgebra(algebra).size());
        assertEquals("TheoremSet should be empty for Algebra 1", 0, fRepo.getTheoremsByAlgebra(algebra).size());
        assertEquals("AxiomSet should be empty for Algebra 2", 0, fRepo.getAxiomsByAlgebra(algebra2).size());
        assertEquals("TheoremSet should be empty for Algebra 2", 0, fRepo.getTheoremsByAlgebra(algebra2).size());
        
        // Can we still read the formula?
        assertNotNull(fRepo.get(formula.getId()));
        assertNotNull(aRepo.get(algebra.getId()));
        assertNotNull(aRepo.get(algebra2.getId()));
        
        fRepo.delete(formula);
        
        aRepo.delete(algebra);
        aRepo.delete(algebra2);
    }    
}