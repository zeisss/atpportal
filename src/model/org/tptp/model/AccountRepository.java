package org.tptp.model;

import java.util.*;

public abstract class AccountRepository {
    private static AccountRepository instance = null;
    public static AccountRepository getInstance() {
        if ( null == instance) {
            instance = RepositoryFactory.getInstance().createAccountRepository();
        }
        return instance;
    }
    
    public AccountRepository() {}
    
    public abstract Account getByLoginNameAndPassword(String loginName, String password);
    public abstract Account getByLoginName(String loginName);
    public abstract Set<Account> getAll();
    public abstract Account get(long id);
    
    public abstract void save(Account account);
    public abstract void update(Account account);
    public abstract void delete(Account account);
    
    /**
     *Ê@deprecated Use AuthService#setPassword(Account, String) instead.
     */
    public abstract void storePassword(Account account, String password);
    
}