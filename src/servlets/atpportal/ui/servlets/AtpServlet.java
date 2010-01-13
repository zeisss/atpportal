package atpportal.ui.servlets;

import org.tptp.*;
import org.tptp.model.*;
import org.tptp.atp.*;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class AtpServlet extends AtpPortalServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String requestURI = request.getRequestURI();
        if ( requestURI.endsWith("/atp/delete")) {
            String atpId = request.getParameter("atp_id");
            try {
                AtpRepository repo = AtpRepository.getInstance();
                
                Atp atp = repo.get(Long.valueOf(atpId));
                repo.delete(atp);
                
                redirectTo("/atp/list", request, response);
            } catch (Exception exc) {
                this.handleException(exc, request, response, "/atp/delete?atp_id=" + atpId);
            }
        }
        else if ( requestURI.endsWith("/atp/new"))
        {
            // Fetch the parameters from the request and create a new atp object
            // which is then stored in the backend.
            
            try {
                AtpRepository repo = AtpRepository.getInstance();
                
                String name = request.getParameter("name");
                String atpName = request.getParameter("atp_name");
                String atpVersion = request.getParameter("atp_version");
                String [] keys = request.getParameterValues("option_key");
                Map<String,String> options = new HashMap<String,String>();
                for (String key : keys) {
                    String value = request.getParameter("option_" + key + "_value");
                    if ( value == null || "".equals(value.trim()))
                        continue;
                    options.put(key, value );
                }
                
                Atp atp = AtpFactory.createAtp(name, atpName, atpVersion, options);
                repo.save(atp);
                
                addMessage("ATP created successfully", request);
                redirectTo("/atp/list", request, response);
            } catch (Exception exc)
            {
                this.handleException(exc, request, response, "/atp/list");
            }
        }
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String requestURI = request.getRequestURI();
        
        if ( requestURI.endsWith("/atp/list")) {
            AtpRepository repo = AtpRepository.getInstance();
            
            List<Atp> atps = repo.getAll();
            request.setAttribute("atps", atps);
            
            includeView("atp/list.jsp", request, response);
        } else if ( requestURI.endsWith("/atp/delete"))
        {
            String atpId = request.getParameter("atp_id");
            
            try
            {
                AtpRepository repo = AtpRepository.getInstance();
            
                Atp atp = repo.get(Long.valueOf(atpId));
                request.setAttribute("atp", atp);
                
                includeView("atp/delete.jsp", request, response);
            } catch (Exception exc) {
                this.handleException(exc, request, response, "/atp/list");
            }
            
        } else if ( requestURI.endsWith("/atp/new"))
        {
            try
            {
                
            
                Set<TheoremProver> provers = TheoremProver.getProvers();
                request.setAttribute("provers", provers);
                
                String atpId = request.getParameter("atp_id");
                if ( atpId != null ) {
                    AtpRepository repo = AtpRepository.getInstance();
                    Atp atp = repo.get(Long.valueOf(atpId));
                    if ( atp == null ) {
                        throw new ModelException("Invalid atp_id: " + atpId);
                    }
                    request.setAttribute("atp", atp);
                }
                
                includeView("atp/new.jsp", request, response);
            } catch(Exception exc) {
                this.handleException(exc, request, response, "/atp/list");
            }
            
        }
    }
}