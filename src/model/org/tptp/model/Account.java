package org.tptp.model;

import java.util.*;

/**
 * This represents an account of a user able to login.
 * 
 * @see AccountRepository
 * @see AuthService
 */
public class Account {
    /**
     * Level when the user is not allowed to login, because the admin has not yet authorized this user.
     */
    public static final int LOCKED = 0;
    
    /**
     * Level for a normal user able to perform all normal actions.
     */
    public static final int NORMAL = 50;
    
    /**
     * Level for administration users. Able to perform all actions.
     */
    public static final int ADMINISTRATOR = 100;
    
    
    private long id;
    private String displayName;
    private String loginName;
    private String email;
    private int level;
    // NOTE: We don't save the password here, because changing the password involves a bit of logic. See AuthService for this.
    
    public Account(String email, String loginName) {
        this(email, loginName, loginName);
    }
    public Account(String email, String loginName, String displayName) {
        this(-1, email, loginName, displayName, LOCKED);
    }
    
    public Account(long id, String email, String loginName, String displayName, int level) {
        this.id = id;
        this.email = email;
        this.loginName = loginName;
        this.displayName = displayName;
        this.level = level;
    }
    
    
    public boolean equals(Object obj) {
        if ( obj instanceof Account) {
            Account acc = (Account) obj;
            return (id == acc.id) &&
                    (displayName == null ? acc.displayName == null : (displayName.equals(acc.displayName))) &&
                    (loginName == null ? acc.loginName == null : (loginName.equals(acc.loginName))) &&
                    (email == null ? acc.email  == null : (email .equals(acc.email ))) &&
                    (level == acc.level);
        }
        return false;
    }
    public int hashCode() {
        return (int)this.id;
    }
    
    public String toString() {
        return "Account [id=" + id + ";login_name=" + loginName + "; display_name=" + displayName + ";level=" + level + "]";
    }
    
    public void setId(long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setLoginName(String loginName) { this.loginName = loginName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setLevel(int level) {
        this.level = level;
    }
    
    public long getId() { return this.id; }
    public String getEmail() { return this.email; }
    public String getLoginName() { return this.loginName; }
    public String getDisplayName() { return this.displayName; }
    public int getLevel() { return this.level; }
}