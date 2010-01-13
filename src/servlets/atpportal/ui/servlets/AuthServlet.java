package atpportal.ui.servlets;

import org.tptp.*;
import org.tptp.model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class AuthServlet extends AtpPortalServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        log("POST /auth | " + request.getRequestURI());
        
        if ( request.getRequestURI().endsWith("/auth/login")) {
            String username = request.getParameter("login_name");
            String password = request.getParameter("login_password");
            
            try {
                // Perform a login
                Account account = AuthService.login(username, password);
                
                if ( account != null )
                {
                    HttpSession session = request.getSession();
                    session.setAttribute("account", account);
                    session.setAttribute("account_id", account.getId());
                    
                    redirectTo("/", request, response);
                } else {
                    addMessage("No account for given login found.", request);
                    
                    includeView("auth/login.jsp", request, response);
                }
                
                
            } catch (Exception exc) {
                log( "ModelException in /auth/login", exc);
                
                addMessage("An error occured during login: " + exc.getMessage(), request);
                includeView("auth/login.jsp", request, response);
            }
        }
        else if ( request.getRequestURI().endsWith("/auth/profile"))
        {
            String displayName = request.getParameter("display_name");
            try {
                HttpSession session = request.getSession();
                Account oldSelf = (Account)session.getAttribute("account");
                if ( oldSelf == null ) {
                    throw new IllegalStateException("Not logged in");
                }
                
                AccountRepository repo = AccountRepository.getInstance();
                Account account = repo.get(oldSelf.getId());
                account.setDisplayName(displayName);
                repo.update(account);
                session.setAttribute("account", account);
                
                addMessage("Changes saved", request);
                
                redirectTo("/auth/profile", request, response);
            } catch (Exception exc) {
                log("Error in POST /auth/profile", exc);
                addMessage("Error while saving: " + exc.getMessage(), request);
                includeView("auth/profile.jsp", request, response);
            }
        }
        else if ( request.getRequestURI().endsWith("/auth/resetPassword")) {
            String loginPassword = request.getParameter("login_password");
            String loginPassword2 = request.getParameter("login_password2");
            
            
            try {
                if ( !loginPassword.equals(loginPassword2)) {
                    throw new IllegalStateException("Passwords are not equal.");
                }
                
                HttpSession session = request.getSession();
                Account oldSelf = (Account)session.getAttribute("account");
                if ( oldSelf == null ) {
                    throw new IllegalStateException("Not logged in");
                }
                
                AccountRepository repo = AccountRepository.getInstance();
                
                
                // Use the authservice to store the password
                AuthService.setPassword(oldSelf, loginPassword);
                
                // Redirect the user to the profile page.
                addMessage("Password updated!.", request);
                redirectTo("/auth/profile", request, response);
                
            } catch(Exception exc){
                log("Error in POST /auth/resetPassword", exc);
                addMessage("Error while saving: " + exc.getMessage(), request);
                includeView("auth/register.jsp", request, response);
            }
        }
        else if ( request.getRequestURI().endsWith("/auth/register"))
        {
            String email = request.getParameter("email");
            String loginName = request.getParameter("login_name");
            String loginPassword = request.getParameter("login_password");
            String loginPassword2 = request.getParameter("login_password2");
            
            try {
                if ( !loginPassword.equals(loginPassword2)) {
                    throw new IllegalArgumentException("Passwords are not equal!");
                }
                
                AuthService.register(email, loginName, loginPassword);
                
                // Redirect the user to the front page.
                addMessage("Account successfully created! You can login now.", request);
                redirectTo("/auth/login", request, response);
                
            } catch(Exception exc){
                log("Error in POST /auth/register", exc);
                addMessage("Error while saving: " + exc.getMessage(), request);
                includeView("auth/register.jsp", request, response);
            }
        }
    }
     
    /**
     * Redirects to the different views.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        if ( request.getRequestURI().endsWith("/auth/profile")) {
            includeView("auth/profile.jsp", request, response);
            
        } else if ( request.getRequestURI().endsWith("/auth/register")) {
            includeView("auth/register.jsp", request, response);
            
        } else if ( request.getRequestURI().endsWith("/auth/logout")) {
            HttpSession session = request.getSession();
            session.removeAttribute("account");
            session.removeAttribute("account_id");
            
            redirectTo("/", request, response);
        } else {
            includeView("auth/login.jsp", request, response);
        }
    }
    
}