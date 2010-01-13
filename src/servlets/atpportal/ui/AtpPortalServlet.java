package atpportal.ui;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * This is the baseclass for all servlets in the ATPPortal application. It provides
 * some minor utility functions for redirecting and sending messages to the user.
 */
public class AtpPortalServlet extends HttpServlet {
    protected void handleException(Throwable t, HttpServletRequest request, HttpServletResponse response, String newUrl)
        throws ServletException, IOException
    {
        log("Error in " + request.getRequestURI(), t);
        
        request.setAttribute("exception", t);
        request.setAttribute("new_url", newUrl);
        
        RequestDispatcher rd = request.getRequestDispatcher("/error-page.jsp");
        rd.include(request, response);   
    }
    
    /**
     * Adds the given message to a <i>messages</i> attribute in the given request, so that it
     * gets rendered to the user later on. These messages are shown by the header.jsp view which
     * is included by almost all views.
     *
     * @see /views/layout/header.jsp
     */
    protected void addMessage(String message, HttpServletRequest request) {
        Set<String> strings = (Set<String>)request.getAttribute("messages");
        if ( strings == null ) {
            strings = new HashSet<String>();
            request.setAttribute("messages", strings);
        }
        strings.add(message);
    }
    
    /**
     * Forwards the user internally to the url. This should not be used in a POST
     * request, as this will result in a new POST to the given <code>url</code>, use
     * <code>redirectTo()</code> instead.
     *
     * @see redirectTo(String, HttpServletRequest, HttpServletResponse)
     * @see RequestDispatcher#forward(HttpServletRequest, HttpServletResponse)
     */
    protected void forwardTo(String url, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        
        RequestDispatcher rd = request.getRequestDispatcher(url);

        // Forward to requested URL
        rd.forward(request, response);
    }
    
    /**
     * Include the file in the /views/ folder into the output of the given request.
     * 
     * @see RequestDispatcher#include(HttpServletRequest, HttpServletResponse)
     */
    protected void includeView(String url, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        RequestDispatcher rd = request.getRequestDispatcher("/views/" + url);

        // Forward to requested URL
        rd.include(request, response);
    }
    
    /**
     * Sends the client a redirect message for the new URL.
     */
    protected void redirectTo(String url, HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        String uri = request.getContextPath();
        if ( !uri.endsWith("/") && !url.startsWith("/"))
            uri += "/";
        uri += url;
        response.sendRedirect(response.encodeRedirectURL(uri));
    }
    
    /*
    protected void service(HttpServletRequest req, HttpServletResponce res) {
        try {
            super.service(req, res);
        } catch(ModelException exc) {
            
        }
    }
    */
}