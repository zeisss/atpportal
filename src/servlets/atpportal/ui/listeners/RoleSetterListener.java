package atpportal.ui.listeners;

import javax.servlet.*;
import javax.servlet.http.*;

import atpportal.ui.*;
import org.tptp.model.Account;

/**
 * This class is a utility listener, setting a string into each request given
 * the role of the user. This makes it easier for the views (JSP pages) to
 * show/hide certain elements of the page based on the role of the user.
 */
public class RoleSetterListener implements ServletRequestListener {
    /**
     * The key under which the role is stored in the request.
     */
    public static final String USER_ROLE = "user_role";
    
    /**
     * Sets the role in the request.
     */
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