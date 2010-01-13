package atpportal.ui.listeners;

import javax.servlet.*;
import javax.servlet.http.*;

import atpportal.ui.*;
import org.tptp.model.Account;

public class RoleSetterListener implements ServletRequestListener {
    public static final String USER_ROLE = "user_role";
    
    public void requestInitialized(ServletRequestEvent rre) {
        HttpServletRequest request = (HttpServletRequest)rre.getServletRequest();
        Account acc = AuthHelper.getAccount(request.getSession());
        if ( acc != null ) {
            switch ( acc.getLevel() ) {
                case Account.ADMINISTRATOR:
                    request.setAttribute(USER_ROLE, "admin");
                    break;
                case Account.NORMAL:
                    request.setAttribute(USER_ROLE, "normal");
                    break;
                case Account.LOCKED:
                    request.setAttribute(USER_ROLE, "locked");
                    break;
                default:
                    request.setAttribute(USER_ROLE, "none");
            }
        } else {
            request.setAttribute(USER_ROLE, "none");
        }
    }
    public void requestDestroyed(ServletRequestEvent rre) {
        
    }
}