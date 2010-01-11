package org.tptp.model;

import java.util.*;
import org.junit.*;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class ProofTest extends AbstractModelTest {
    @Test
    public void testProofSetters() {
        Date d = new Date();
    
        Proof p = new Proof(12, d);
        assertEquals(12, p.getId());
        assertEquals(d, p.getTime());
        assertEquals(0, p.getProofSteps().size());
        
        p.setId(123);
        p.setDetail("maxtime", "123123h");
        
        assertEquals(123, p.getId());
        assertEquals("123123h", p.getDetail("maxtime"));
        
        ProofStep ps = new ProofStep(12, "x = y * 2", "y = 4");
        p.addProofStep(ps);
        
        ProofStep [] pss = (ProofStep[]) p.getProofSteps().toArray(new ProofStep[1]);
        assertEquals(1, pss.length);
        assertEquals(ps, pss[0]);
    }
    
    @Test
    public void testProofEquals() {
        Date d = new Date();
        Date d2 = new Date(d.getTime() + 123141);
        
        Proof a = new Proof(12, d);
        a.setDetail("maxtime", "1");
        
        Proof b = new Proof(12, d);
        
        assertFalse(a.equals(b));
        b.setDetail("maxtime", "1");
        
        Proof c = new Proof(12, d2);
        c.setDetail("maxtime", "1");
        
        assertFalse("test1", a == b);
        assertEquals("test2", a.hashCode(), b.hashCode());
        assertTrue("test3", a.equals(b));
        
        assertNotSame("test4", b,c);
        assertNotSame("test5", a,c);
        
        b.setTime(d2);
        assertFalse("test6", a.equals(b));
        assertTrue("test7",b.equals(c));
    }
    
    @Test
    public void testProofRepositoryImplAvailable() {
        assertNotNull("ProofRepo not null", ProofRepository.getInstance());
    }
    
    @Test
    public void testProofRepositoryCD() {
        Date d = new Date();
        
        ProofRepository repo = ProofRepository.getInstance();
        assumeNotNull("ProofRepo not null", repo);
        
        Proof proof = new Proof(-1, d);
        proof.addProofStep(new ProofStep(1, "x = y * z", "y = 2"));
        proof.addProofStep(new ProofStep(3, "x = 2 * z", "z = 7"));
        proof.addProofStep(new ProofStep(1, "x = 14", ""));
        repo.save(proof);
        
        assertNotSame("Id should not be -1", -1l, proof.getId());
        long id = proof.getId();
        
        repo.delete(proof);
        assertEquals("Id should be -1", -1, proof.getId());
        
        assertNull(repo.get(id));
    }
    
    @Test
    public void testProofRepositoryCRUD() {
        Date timestamp = new Date();
        
        ProofRepository repo = ProofRepository.getInstance();
        assumeNotNull(repo);
        
        Proof proof = new Proof(timestamp);
        proof.setDetail("welcome", "Hello World");
        repo.save(proof);
        assertNotSame("Id should have been updated", -1l, proof.getId());
        long id = proof.getId();
        
        
        Proof proof2 = repo.get(id);
        assertNotNull("Reading the account should be possible", proof2);
        assertEquals("matching timestamp", timestamp, proof2.getTime());
        assertEquals("equals", proof, proof2);
        proof = null; // no longer use this!
        
        proof2.setDetail("foo", "Bar");
        repo.update(proof2);
        
        Set<Proof> proofs = repo.getAll();
        assertTrue("getAll() should contain the saved account", proofs.contains(proof2));
        
        repo.delete(proof2);
        assertEquals("Id should be -1", -1, proof2.getId());
        
        
        assertNull("Account with id " + id + " should be deleted.", repo.get(id));
    }
    
    @Test
    public void testProofToFormulaBinding() {
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
        Set<Proof> proofs = pRepo.getProofsForFormula(f);
        assertEquals("proofs size (1)", 1, proofs.size());
        assertArrayEquals("proof in there", new Object[]{p2}, proofs.toArray());
        
        proofs = pRepo.getProofsWhereFormulaUsed(f2);
        assertEquals("proofs size (2)", 1, proofs.size());
        assertArrayEquals("proof in there", new Object[]{p}, proofs.toArray());
    }
}