package org.tptp.model;

import org.tptp.model.postgres.*;

import org.junit.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assume.*;



public class TransactionTest extends AbstractModelTest {
    @Test
    public void testTransactionIsNull() {
        assertNull(WorkManager.getCurrentTransaction());
    }
    
    @Test
    public void testTransactionSimpleRollback() {
        final AtpRepository repo = AtpRepository.getInstance();
        assertNotNull("ATP Repo must not be null", repo);
        
        WorkManager.run(new Runnable() { public void run() {
            Atp atp = new Atp("Prover 9 TEST2221", "Prover9", "1.0");
            atp.setOption("path", "/bin");
            repo.save(atp);
            
            WorkManager.getCurrentTransaction().rollback();
            
            Atp atp2 = repo.get(atp.getId());
            assertNull(atp2);
        }});
    }
    
    private long atpId;
    
    @Test
    public void testTransactionSimple2() {
        final AtpRepository repo = AtpRepository.getInstance();
        assertNotNull("ATP Repo must not be null", repo);
        
        assertNull(WorkManager.getCurrentTransaction());
        WorkManager.run(new Runnable() { public void run() {
            Atp atp = new Atp("Prover 9 TEST2222", "Prover9", "1.0");
            atp.setOption("path", "/bin");
            repo.save(atp);
            atpId = atp.getId();
        }});
        assertNull(WorkManager.getCurrentTransaction());
        
        Atp atp2 = repo.get(atpId);
        assertNotNull(atp2);
        
        repo.delete(atp2);
        
    }
}