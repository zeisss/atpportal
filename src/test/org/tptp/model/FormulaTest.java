package org.tptp.model;

import org.junit.*;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.util.*;

public class FormulaTest extends AbstractModelTest {    
    @Test
    public void testFormulaSetter()
    {
        // "Empty" Algebra
        Formula f = new Formula();
        assertEquals(-1, f.getId());
        assertEquals("",f.getName());
        assertEquals("",f.getFormulaText());
        assertEquals("",f.getComment());
        
        assertEquals(0, f.getReferences().size());
        
        f = new Formula(123123, "Kleener Test", "x = 1", "A simple test");
        assertEquals(123123, f.getId());
        assertEquals("Kleener Test", f.getName());
        assertEquals("x = 1", f.getFormulaText());
        assertEquals("A simple test", f.getComment());
        
        List<FormulaReference> references = f.getReferences();
        assertEquals(0, references.size());
    }
    
    @Test
    public void testFormulaReferencesInSync()
    {
        Formula f = new Formula("Kleene Test");
        FormulaReference ref = new FormulaReference("DEMO01", "Demo Author", "Demo Reference", 2009);
        f.addReference(ref);
        
        assertEquals(1, f.getReferences().size());
        assertEquals(ref, f.getReferences().get(0));
        
        f.removeReference(ref);
        assertEquals(0, f.getReferences().size());
    }
    
    @Test
    public void testFormulaRepositoryAvailable() {
        FormulaRepository repo = FormulaRepository.getInstance();
        assertNotNull(repo);
        
    }
    
    
    @Test
    public void testFormulaCD() {
        FormulaRepository repo = FormulaRepository.getInstance();
        assumeNotNull(repo);
        
        Formula f = new Formula("Simple start", "x = 1", "UNIT TEST ENTRY");
        f.addReference(new FormulaReference("EVANS04", "Eric Evans", "Domain Driven-Design", 2004));
        repo.save(f);
        assertNotSame("Save must update the id", -1l, f.getId());
        long id = f.getId();
        
        repo.delete(f);
        assertSame(-1l, f.getId());
        
        assertNull(repo.get(id));
    }
    
    @Test
    public void testFormulaRepoCRUD() {
        FormulaRepository repo = FormulaRepository.getInstance();
        assumeNotNull(repo);
        
        Formula f = new Formula("Simple start", "x = 1");
        f.setComment("UnitTest entry");
        assertEquals("Id must be -1 before saving", -1l, f.getId());
        
        repo.save(f);
        assertNotSame("Id must be updated after save()", -1l, f.getId());
        
        Set<Formula> formulas = repo.getAll();
        assertTrue("Saved formula must be returned by getAll()", formulas.contains(f));
        
        Formula f2 = repo.get(f.getId());
        assertEquals("Get() must return an equal object", f, f2);
        
        f.setComment("UNIT TEST ENTRY");
        repo.update(f);
        
        Formula f3 = repo.get(f.getId());
        assertEquals(f, f3);      
        
        repo.delete(f);
        assertEquals("Id must be -1 after delete", -1, f.getId());
        
    }
    
    @Test
    public void testFormulaToAlgebraBinding() {
        AlgebraRepository aRepo = AlgebraRepository.getInstance();
        FormulaRepository fRepo = FormulaRepository.getInstance();
        assumeNotNull(aRepo);
        assumeNotNull(fRepo);
        
        Formula f = null, f2 = null;
        Algebra algebra = null, algebra2 = null;
        
        try {
            // Create the data
            f = new Formula("demo formula A", "and(true,true) = true");
            f2 = new Formula("demo formula B", "and(true,true) = true");
            
            algebra = new Algebra("Sample Algebra 1", "Sample Algebra");
            algebra2 = new Algebra("Sample Algebra 2");
            
            
            aRepo.save(algebra);
            aRepo.save(algebra2);
            
            // Save it
            fRepo.save(f);
            fRepo.save(f2);
                
            // Link it
            aRepo.link(algebra, f, true);
            aRepo.link(algebra, f2, false);
            aRepo.link(algebra2, f2, true);
            aRepo.link(algebra2, f, false);
            
            // Test it
            Set<Algebra> algebren = aRepo.getAlgebrenForAxiom(f);
            assertEquals("algebren size (1)", 1, algebren.size());
            assertEquals("formula in there", algebra, algebren.toArray()[0]);
            
            algebren = aRepo.getAlgebrenForTheorem(f);
            assertEquals("algebren size (2)", 1, algebren.size());
            assertEquals("formula in there", algebra2, algebren.toArray()[0]);
            
            algebren = aRepo.getAlgebrenForAxiom(f2);
            assertEquals("algebren size (3)", 1, algebren.size());
            assertEquals("formula in there", algebra2, algebren.toArray()[0]);
            
            algebren = aRepo.getAlgebrenForTheorem(f2);
            assertEquals("algebren size (4)", 1, algebren.size());
            assertEquals("formula in there", algebra, algebren.toArray()[0]);
            
        } finally {
            fRepo.delete(f);
            fRepo.delete(f2);
            
            if ( algebra != null ) {
                aRepo.delete(algebra);
            }
            if ( algebra2 != null ) {
                aRepo.delete(algebra2);
            }
        }

        
    }
    
    @Test
    public void testFormulaToProofBinding() {
        ProofRepository pRepo = ProofRepository.getInstance();
        FormulaRepository fRepo = FormulaRepository.getInstance();
        assumeNotNull(pRepo);
        assumeNotNull(fRepo);
        
        // Create the data
        Formula f = new Formula("demo formula A", "and(true,true) = true");
        Formula f2 = new Formula("demo formula B", "and(true,true) = true");
        
        Proof p = new Proof();
        Proof p2 = new Proof();
        
        // Save it
        fRepo.save(f);
        fRepo.save(f2);
        
        pRepo.save(p);
        pRepo.save(p2);
        
        // Link it
        pRepo.storeUsage(p, f);
        pRepo.storeUsage(p, f2);
        pRepo.storeProve(p2, f);
        pRepo.storeProve(p2, f2);
        
        // Test it
        Set<Formula> formulas = fRepo.getUsedFormulasForProof(p);
        assertEquals("proofs size (1)", 2, formulas.size());
        assertTrue("formula in there", formulas.contains(f));
        assertTrue("formula2 in there", formulas.contains(f2));
        
        formulas = fRepo.getProvedFormulasByProof(p2);
        assertEquals("algebren size (2)", 2, formulas.size());
        assertTrue("formula3 in there", formulas.contains(f));
        assertTrue("formula4 in there", formulas.contains(f2));
        
        fRepo.delete(f);
        fRepo.delete(f2);
    }
}