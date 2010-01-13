package atpportal.ui;

import org.tptp.model.*;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class AuthHelper {
    public static Account getAccount(HttpSession session) {
        return (Account) session.getAttribute("account");
    }
    public static boolean isMinimumLevel(Account account, int level) {
        if ( account == null ) {
            return false;
        }
        
        return ( account.getLevel() >= level);
    }
    
    public static boolean isMinimumLevel(HttpServletRequest request, int level) {
        HttpSession session = request.getSession(false);
        if ( session == null ) {
            return false;
        }
        return isMinimumLevel(getAccount(session), level);
    }
    
    public static boolean isAdmin(HttpServletRequest request) {
        return isMinimumLevel(request, Account.ADMINISTRATOR);
    }
    
    public static boolean isNormal(HttpServletRequest request) {
        return isMinimumLevel(request, Account.NORMAL);
    }
    
    
}