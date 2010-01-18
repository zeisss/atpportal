package atpportal.ui.servlets;

import atpportal.ui.*;
import org.tptp.model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class AccountServlet extends AtpPortalServlet {
    private Random random = new Random();
    
    /**
     * Creates a random string of the given length contain the following characters:
     * <ul>
     *  <li>a-z</li>
     *  <li>A-Z</li>
     *  <li>0-9</li>
     * </ul>
     */
    private String generateRandomPassword(int length) {
        String result = "";
        for ( int i = 0; i < length;i++) {
            int x = random.nextInt(62);
            if (x < 26)
                result += (char)('a' + x);
            else if(x < 2*26) 
                result += (char)('A' + (x - 26));
            else
                result += (char)('0' + (x - 52));
            
        }
        return result;
    }
    
    /**
     * Creates a random password and sets the password of the account identified by the parameter
     * <code>account_id</code> to this new random password. The password is then showed by the view,
     * since we currently don't send mails or have any other way of informing the user.
     */
    public void doResetPasswordPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String accountId = request.getParameter("account_id");
        
        try {
            AccountRepository repo = AccountRepository.getInstance();
            Account account = repo.get(Long.valueOf(accountId));
            if ( account == null ) {
                throw new IllegalArgumentException("Invalid account_id");
            }
            String password = generateRandomPassword(10);
            AuthService.setPassword(account, password);
            
            request.setAttribute("account", account);
            request.setAttribute("new_password", password);
            includeView("account/reset_password_result.jsp", request, response);
            
        } catch(Exception exc) {
            this.handleException(exc, request, response, "/account/list");
        }
    }
    
    /**
     * Updates the level of the account identified by parameter <code>account_id</code> to the
     * level <code>role</code>. Valid values for role are admin, normal and locked. 
     */
    public void doUpdateRolePost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String account_id = request.getParameter("account_id");
        String role = request.getParameter("role");
        
        try {
            AccountRepository repo = AccountRepository.getInstance();
            Account account = repo.get(Long.valueOf(account_id));
            if ( account == null ) {
                throw new IllegalArgumentException("Invalid account_id");
            }
            
            if ( role.equals("admin")) {
                account.setLevel(Account.ADMINISTRATOR);
            } else if ( role.equals("normal")) {
                account.setLevel(Account.NORMAL);
            } else if ( role.equals("locked")) {
                account.setLevel(Account.LOCKED);
            } else {
                throw new IllegalArgumentException("Invalid role: " + role);
            }
            repo.update(account);
            
            redirectTo("/account/list", request, response);
            
        } catch(Exception exc) {
            this.handleException(exc, request, response, "/account/list");
        }   
    }
    
    /**
     * Calls submethods based on the called URL.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {   
        if ( request.getRequestURI().endsWith("/account/reset_password"))
        {
            doResetPasswordPost(request, response);
            
        } else if ( request.getRequestURI().endsWith("/account/update_role")) {
            doUpdateRolePost(request, response);
        }
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String requestUri = request.getRequestURI();
        
        if ( requestUri.endsWith("/account/list")) {
            try
            {
                AccountRepository repo = AccountRepository.getInstance();
                
                // NOTE: Maybe this should be split with a paginator
                Set<Account> accounts = repo.getAll();
                request.setAttribute("accounts", accounts);
                
                includeView("account/list.jsp", request, response);
            } catch (Exception exc) {
                this.handleException(exc, request, response, "/");
            }
        }
        else if ( requestUri.endsWith("/account/update_role")) {
            String accountId = request.getParameter("account_id");
            String role = request.getParameter("role");
            try {
                AccountRepository repo = AccountRepository.getInstance();
                Account account = repo.get(Long.valueOf(accountId));
                if ( account == null ) {
                    throw new ModelException("Invalid account_id!");
                }
                
                request.setAttribute("account", account);
                request.setAttribute("role", role);
                
                includeView("account/update_role.jsp", request, response);
            } catch (Exception exc) {
                this.handleException(exc, request, response, "/account/list");
            }
        }
        else if ( requestUri.endsWith("/account/reset_password")) {
            String account_id = request.getParameter("account_id");
            try {
                AccountRepository repo = AccountRepository.getInstance();
                
                Account account = repo.get(Long.valueOf(account_id));
                if ( account == null ) {
                    throw new IllegalArgumentException("Invalid account_id");
                }
                
                request.setAttribute("account", account);
                
                includeView("account/reset_password.jsp",request, response);
                
            } catch (Exception exc) {
                this.handleException(exc, request, response, "/account/list");
            }
        }
    }
    
}