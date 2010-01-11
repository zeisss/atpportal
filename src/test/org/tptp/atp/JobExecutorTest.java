package org.tptp.atp;

import java.io.*;
import org.junit.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.tptp.model.*;
import org.tptp.atp.prover9.*;

public class JobExecutorTest {
    @Test
    public void testExecuteNext() {
        AtpRepository atpRepo = AtpRepository.getInstance();
        QueueJobRepository qRepo = QueueJobRepository.getInstance();
        ProofRepository pRepo = ProofRepository.getInstance();
        FormulaRepository fRepo = FormulaRepository.getInstance();
        
        Atp atp = null;
        QueueJob job = null;
        Proof proof = null;
        Formula f1 = null, f2 = null, f3 = null, f4 = null;
        
        try {
            
            atp = new Atp("JOB EXECUTOR TEST ATP", "Prover9", "0.1");
            atp.setOption("path", "/Users/zeisss/Documents/Bachelorarbeit/Projekt/ldar");
            atpRepo.save(atp);
            
            job = new QueueJob("(x ^ y) ^ z = x ^ (y ^ z)", atp);
            
            qRepo.save(job);
            
            f1 = new Formula("McKenzie_1", "x v (y ^ (x ^ z)) = x");
            fRepo.save(f1);
            qRepo.storeInputFormula(job, f1);
            
            f2 = new Formula("McKenzie_2", "x ^ (y v (x v z)) = x");
            fRepo.save(f2);
            qRepo.storeInputFormula(job, f2);
            
            f3 = new Formula("McKenzie_3", "((y ^ x) v (x ^ z)) v x = x");
            fRepo.save(f3);
            qRepo.storeInputFormula(job, f3);
            
            f4 = new Formula("McKenzie_4", "((y v x) ^ (x v z)) ^ x = x");
            fRepo.save(f4);
            qRepo.storeInputFormula(job, f4);
            
            job.setStatus(QueueJob.STATUS_QUEUED);
            qRepo.update(job);
            
            // Now execute the job
            QueueJobExecutorService service = new QueueJobExecutorService();
            service.executeNextJob();
            
            job = qRepo.get(job.getId());
            assertEquals(QueueJob.STATUS_PROCESSED, job.getStatus());
            assertTrue(job.getProofId() > 0);
            
            proof = pRepo.get(job.getProofId());
            assertNotNull(proof);
        }
        finally {
            
            if ( job != null ) {
                qRepo.delete(job);
            }
            if ( proof != null ) {
                pRepo.delete(proof);
            }
            
            if ( f1 != null ) { fRepo.delete(f1); }
            if ( f2 != null ) { fRepo.delete(f2); }
            if ( f3 != null ) { fRepo.delete(f3); }
            if ( f4 != null ) { fRepo.delete(f4); }
            
            if ( atp != null && atp.getId() != -1 ) {
                atpRepo.delete(atp);    
            }
            
        }
    }
}