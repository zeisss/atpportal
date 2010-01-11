package org.tptp.model;

import org.junit.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assume.*;


import org.tptp.model.postgres.*;

public class AccountTest extends AbstractModelTest {
    public AccountTest() {}
    
    @Test
    public void testAccountSetter() {
        Account acc = new Account("stephan.zeissler@moinz.de", "zeisss");
        assertEquals(-1, acc.getId());
        assertEquals("stephan.zeissler@moinz.de", acc.getEmail());
        assertEquals("zeisss", acc.getLoginName());
        assertEquals("zeisss", acc.getDisplayName());
        
        acc.setId(1234);
        acc.setEmail("szeiss2s@smail.inf.fh-brs.de");
        acc.setLoginName("szeiss2s");
        acc.setDisplayName("Stephan Z.");
        
        assertEquals(1234, acc.getId());
        assertEquals("szeiss2s@smail.inf.fh-brs.de", acc.getEmail());
        assertEquals("szeiss2s", acc.getLoginName());
        assertEquals("Stephan Z.", acc.getDisplayName());
        
    }
    
    @Test
    public void testAccountEquals() {
        Account a = new Account("a@acme.com", "Kleensche Algebra");
        Account b = new Account("a@acme.com", "Klee" + "nsche Algebra");
        Account c = new Account("a@acme.com", "Boolsche Algebra");
        
        assertFalse("test1", a == b);
        assertEquals("test2", a.hashCode(), b.hashCode());
        assertTrue("test3", a.equals(b));
        
        assertNotSame("test4", b,c);
        assertNotSame("test5", a,c);
        
        b.setLoginName("FOOLSCHE ALGEBRA");
        
        assertFalse("test6", a.equals(b));
    }
    
    @Test
    public void testAccountRepositoryCD() {
        AccountRepository repo = AccountRepository.getInstance();
        assertNotNull(repo);
        
        long id;
        
        Account acc = new Account("stephan.zeissler@moinz.de", "zeisss1");
        repo.save(acc);
        try {
            assertNotSame("Id should have been updated", -1l, acc.getId());
            id = acc.getId();
        } finally {
            repo.delete(acc);
            assertEquals("Id should be -1", -1, acc.getId());
        }
        assertNull(repo.get(id));
    }
    
    @Test
    public void testRepositoryCRUD() {
        AccountRepository repo = AccountRepository.getInstance();
        assertNotNull(repo);
        
        Account acc = new Account("stephan.zeissler@moinz.de", "zeisss2");
        repo.save(acc);
        assertNotSame("Id should have been updated", -1l, acc.getId());
        
        long id = acc.getId();
        acc = null; // no longer use this!
        
        Account acc2 = repo.get(id);
        assertNotNull("Reading the account should be possible", acc2);
        assertEquals("zeisss2", acc2.getLoginName());
        assertEquals("stephan.zeissler@moinz.de", acc2.getEmail());
        
        acc2.setDisplayName("Stephan TESTCRUD");
        repo.update(acc2);
        
        Set<Account> accs = repo.getAll();
        assertTrue("getAll() should contain the saved account", accs.contains(acc2));
        
        repo.delete(acc2);
        assertEquals("Id should be -1", -1, acc2.getId());
        
        assertNull("Account with id " + id + " should be deleted.", repo.get(id));
    }
    
    @Test
    @Ignore
    public void testRepositoryDoubleEmailLoginName() {
        // TODO: Test for a double email
        
        // TODO: Test for a double login name
    }
    
    @Test
    @Ignore
    public void testRegisterLoginReset() {
        // TODO: Test AuthService::*()
    }
    
    
}