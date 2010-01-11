package org.tptp.model;

import org.junit.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assume.*;


import org.tptp.model.postgres.*;

public class AtpTest extends AbstractModelTest{
    @Test
    public void testAtpSetter() {
        Atp atp = new Atp(123, "Prover 9", "PROVER9", "0.1");
        atp.setOption("path", "/usr/local/prover9/bin/prover9");
        atp.setOption("maxtime", "123");
        
        assertEquals("id", 123, atp.getId());
        assertEquals("name", "Prover 9", atp.getName());
        assertEquals("path", "/usr/local/prover9/bin/prover9", atp.getOption("path"));
        assertEquals("123", atp.getOption("maxtime"));
    }
    
    @Test
    public void testAtpEquals() {
        Atp a = new Atp(1,"a","a", "1.0");
        a.setOption("a","b");
        Atp b = new Atp(1,"a","a", "1.0");
        b.setOption("a","b");
        Atp c = new Atp(2,"c","c", "1.0");
        
        assertFalse("test1", a == b);
        assertEquals("test2", a.hashCode(), b.hashCode());
        assertTrue("test3", a.equals(b));
        
        assertNotSame("test4", b,c);
        assertNotSame("test5", a,c);
        
        b.setName("test6");
        
        assertFalse("test6", a.equals(b));
    }
    
    @Test
    public void testAtpRepositoryCD() {
        AtpRepository repo = AtpRepository.getInstance();
        assertNotNull("ATP Repo must not be null", repo);
        Atp atp = new Atp("Prover 9 - ATP Test CD", "Prover9", "1.0");
        atp.setOption("path", "/bin");
        repo.save(atp);
        
        assertNotSame("Id should have been updated", -1l, atp.getId());
        long id = atp.getId();
        
        repo.delete(atp);
        assertEquals("Id should be -1", -1l, atp.getId());
        
        assertNull(repo.get(id));
    }
    
    @Test
    public void testAtpRepositoryCRUD() {
        AtpRepository repo = AtpRepository.getInstance();
        assertNotNull(repo);
        
        Atp atp = new Atp("Prover 9 - ATP Test CRUD", "Prover9", "1.0");
        atp.setOption("maxtime", "60");
        
        repo.save(atp);
        assertNotSame("Id should have been updated", -1l, atp.getId());
        long id = atp.getId();
        
        
        Atp atp2 = repo.get(id);
        assertNotNull("Reading the account should be possible", atp2);
        assertEquals(atp, atp2);
        assertEquals("60", atp2.getOption("maxtime"));
        
        atp2.setName("prover10");
        repo.update(atp2);
        
        List<Atp> atps = repo.getAll();
        assertTrue("getAll() should contain the saved account", atps.contains(atp2));
        
        repo.delete(atp2);
        assertEquals("Id should be -1", -1, atp2.getId());
        
        assertNull("Account with id " + id + " should be deleted.", repo.get(id));
    }
}