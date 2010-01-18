package atpportal.ui.filters;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import atpportal.ui.AuthHelper;
import org.tptp.model.Account;

/**
 * This filter makes sure, that every request is an authorized (See AuthHelper) and that a minimum account
 * level is contained. If not, the user is send an error page.
 *
 * Configuration:
 * This filter can be configured using the key 'minimum_role' that can be set to either 'normal' or 'admin'.
 *
 * @see AuthHelper
 * @see AuthHelper#isNormal()
 */
public final class AuthorizationFilter implements Filter {
    public static final String MINIMUM_ROLE = "minimum_role";
    public static final String ROLE_NORMAL = "normal";
    public static final String ROLE_ADMIN = "admin";
    
    private int minimumLevel;
    
    public void init(FilterConfig filterConfig) 
        throws ServletException {
        String role = filterConfig.getInitParameter(MINIMUM_ROLE);
        if ( role.equals(ROLE_NORMAL)) {
            this.minimumLevel = Account.NORMAL;
        } else if ( ROLE_ADMIN.equals(role)) {
            this.minimumLevel = Account.ADMINISTRATOR;
        } else {
            this.minimumLevel = Account.LOCKED;
        }
    }
   public void destroy() {
    
   }
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
      throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        if ( !AuthHelper.isMinimumLevel(req, this.minimumLevel)) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            
            // TODO: forward to the proper url after authorization
            RequestDispatcher rd = request.getRequestDispatcher("/auth/login");
    
            // Forward to requested URL
            rd.forward(request, response);
            
        } else {
            chain.doFilter(request, response);
        }
        
   }
}
