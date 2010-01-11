package org.tptp.model;

import org.junit.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assume.*;


import org.tptp.model.postgres.*;

public class QueueJobTest extends AbstractModelTest {
    private Atp atp = new Atp("Prover 9", "Prover9", "1.0");
    
    @Test
    public void testQueueJobSetter() {
        QueueJob job = new QueueJob("x = 2", atp);
        assertEquals("ID", -1l, job.getId());
        assertEquals("x = 2", job.getGoalFormula());
        assertEquals("ATP EQUAL", atp.getId(), job.getAtpId());
        assertEquals("owner id", -1l, job.getAccountId());
        assertEquals("status", QueueJob.STATUS_NEW, job.getStatus());
        
        job.setAccountId(123);
        job.setProofId(123);
        job.setStatus(QueueJob.STATUS_PROCESSED);
        job.setAtpId(123);
        
        assertEquals("2account", 123, job.getAccountId());
        assertEquals("2proof", 123, job.getProofId());
        assertEquals("2status", QueueJob.STATUS_PROCESSED, job.getStatus());
        assertEquals("2atpid",123, job.getAtpId());
    }
    
    @Test
    public void testQueueJobEquals() {
        QueueJob a = new QueueJob("Kleensche Algebra", atp);
        QueueJob b = new QueueJob("Klee" + "nsche Algebra", atp);
        QueueJob c = new QueueJob("Boolsche Algebra", atp);
        
        assertFalse("test1", a == b);
        assertEquals("test2", a.hashCode(), b.hashCode());
        assertTrue("test3", a.equals(b));
        
        assertNotSame("test4", b,c);
        assertNotSame("test5", a,c);
        
        b.setGoalFormula("Boolsche Algebra");
        
        assertFalse("test6", a.equals(b));
        assertTrue("test7", b.equals(c));
    }
    
    @Test
    public void testQueueJobRepositoryCD() {
        AtpRepository atpRepo = AtpRepository.getInstance();
        assertNotNull("ATP Repo must not be null", atpRepo);
        
        QueueJobRepository repo = QueueJobRepository.getInstance();
        assertNotNull("QueueJobRepo must not be null", repo);
        
        QueueJob job = new QueueJob("Kleensche Algebra", atp);
        repo.save(job);
        
        assertNotSame("Id should have been updated", -1l, job.getId());
        long id = job.getId();
        
        repo.delete(job);
        assertEquals("Id should be -1", -1, job.getId());
        
        assertNull(repo.get(id));
    }
    
    @Test
    public void testQueueJobRepositoryCRUD() {
        QueueJobRepository repo = QueueJobRepository.getInstance();
        assumeNotNull(repo);
        
        QueueJob job = new QueueJob("Test", atp);
        repo.save(job);
        assertNotSame("Id should have been updated", -1l, job.getId());
        long id = job.getId();
        job = null; // no longer use this!
        
        QueueJob job2 = repo.get(id);
        assertNotNull("Reading the account should be possible", job2);
        assertEquals("matching account id", -1l, job2.getAccountId());
        assertEquals("machting atp id", -1l, job2.getAtpId());
        assertEquals("matching proof id", -1l, job2.getProofId());
        
        job2.setGoalFormula("x=y");
        repo.update(job2);
        
        Set<QueueJob> jobs = repo.getAll();
        assertTrue("getAll() should contain the saved account", jobs.contains(job2));
        
        repo.delete(job2);
        assertEquals("Id should be -1", -1, job2.getId());
        
        
        assertNull("Account with id " + id + " should be deleted.", repo.get(id));
    }
    
    @Test
    public void testQueueJobRepositoryFindByProof() {
        ProofRepository pRepo = ProofRepository.getInstance();
        assumeNotNull(pRepo);
        
        Proof proof = new Proof();
        pRepo.save(proof);
        assertNotSame(-1l, proof.getId());
        
        QueueJobRepository repo = QueueJobRepository.getInstance();
        assumeNotNull(repo);
        
        QueueJob job = new QueueJob("Test", atp);
        job.setProofId(proof.getId());
        repo.save(job);
        
        QueueJob job2 = repo.getByProofId(proof.getId());
        assertEquals("Equal jobs", job, job2);
        
        List<QueueJob> jobList = repo.getRange(0,1);
        assertNotNull(jobList.get(0));
        assertEquals(1, jobList.size());
        
        repo.delete(job2);
    }
    
    
}