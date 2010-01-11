package org.tptp.model;

import java.util.*;

/**
 * The AuthService provides additional functionality for the user authentication and authorization.
 */
public class AuthService {
    private static void validateLogin(String email, String username, String password) {
        if ( email != null ) {
            if ( email.equals("") ) {
                throw new IllegalArgumentException ("Email must not be empty.");
            }
            if ( email.indexOf("@") == -1 || email.indexOf(".") == -1 ) {
                throw new IllegalArgumentException ("Email does not look like a valid email.");
            }
        }
        
        if ( username != null ) {
            if ( username.equals("")) {
                throw new IllegalArgumentException("Username must not be empty.");
            }
        }
        
        if ( password != null ) {
            if ( password.equals("")) {
                throw new IllegalArgumentException("Password must not be empty.");
            }
            
            if ( password.length() < 5 )
            {
                throw new IllegalArgumentException("Password should have a least 4 characters");
            }
        }
    }
    
    /**
     * Returns a valid account object if a user exists with the given login_name and password.
     */
    public static Account login(String username, String password ) {
        validateLogin(null, username, password);
        
        AccountRepository repo = AccountRepository.getInstance();
        
        Account account = repo.getByLoginNameAndPassword(username, password);
        if ( account != null ) {
            return account;
        }
        
        return null;
    }
    
    /**
     * Creates a new account and stores it in the backend.
     */
    public static Account register(String email, String username, String password) {
        validateLogin(email, username, password);
        
        AccountRepository repo = AccountRepository.getInstance();
        
        Account acc = new Account(email, username);
        
        repo.save(acc);
        
        repo.storePassword(acc, password);
        
        return acc; 
    }
    
    /**
     * Updates the password for the user in backend storage. 
     */
    public static void setPassword(Account account, String password) {
        validateLogin(null,null,password);
        
        AccountRepository repo = AccountRepository.getInstance();
        
        repo.storePassword(account, password);
    }
}