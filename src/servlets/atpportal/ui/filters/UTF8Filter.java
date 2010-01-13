package atpportal.ui.filters;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import atpportal.ui.*;

/**
 * This filter sets the content type of the response to text/html or text/javascript based on the request URI.
 * Further it sets the character set to utf8. These values can later be easily overwritten, but makes the whole
 * stuff a lot easier.
 */
public final class UTF8Filter implements Filter {
   private FilterConfig filterConfig = null;

   public void init(FilterConfig filterConfig) 
      throws ServletException {
      this.filterConfig = filterConfig;
   }
   public void destroy() {
      this.filterConfig = null;
   }
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
      throws IOException, ServletException {
        if (filterConfig == null)
            return;
        
        
        response.setCharacterEncoding("UTF-8");
    
        if ( ((HttpServletRequest)request).getRequestURI().endsWith(".json")) {
            ((HttpServletResponse)response).setHeader("Content-Type", "text/javascript; charset=UTF-8");
        }
        else {
            ((HttpServletResponse)response).setHeader("Content-Type", "text/html; charset=UTF-8");
        }
        chain.doFilter(request, response);
      
   }
}
