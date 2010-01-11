package org.tptp.servlets;

import org.tptp.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.tptp.model.*;

public class SearchServlet extends AtpPortalServlet {
    private boolean isTrue(String s) {
        if ( s == null )
            return false;
        return ("true".equals(s) );
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        handleSearch(request, response);
        
        // Now include the view for rendering
        if ( request.getRequestURI().endsWith("/search.json")){
            includeView("search/json.jsp", request, response);
        } else {
            includeView("search/index.jsp", request, response);
        }
    }
    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {   
        // The vars in the page
        String filter = "";
        long algebraId = -1;
        String sAlgebraId = "all";
        boolean showAxioms = true, showTheorems = false;
        
        Set<Formula> formulas;
        Set<Algebra> algebren;
        
        // If we have a filter, the user searched!
        if ( request.getParameter("filter") != null)
        {   
            sAlgebraId = request.getParameter("algebra_id");
            if ( "all".equals(sAlgebraId) ) algebraId = -1;
            else {
                try {
                    algebraId = Long.valueOf(sAlgebraId);
                } catch(NumberFormatException exc) {
                    algebraId = -1;
                    sAlgebraId = "all";
                }
            }
            
            filter = request.getParameter("filter");
            showAxioms = isTrue(request.getParameter("axioms"));
            showTheorems = isTrue(request.getParameter("theorems"));
            
            FormulaRepository formulaRepo = FormulaRepository.getInstance();
            ProofRepository proofRepo = ProofRepository.getInstance();
            
            try {
                formulas = formulaRepo.getByFilter(algebraId, filter, showAxioms, showTheorems);
            } catch(Exception exc) {
                addMessage("Error while searching: " + exc.getMessage(), request);
                log("Error in /search", exc);
                formulas = Collections.EMPTY_SET;
            }
            
        } else {
            formulas = Collections.EMPTY_SET;
        }
        // Store everything in the request, so the rendering can use it
        request.setAttribute("formulas", formulas);
        
        AlgebraRepository algebraRepo = AlgebraRepository.getInstance();
        algebren = algebraRepo.getAll();
        request.setAttribute("algebren", algebren);
        
        request.setAttribute("algebra_id", sAlgebraId);
        request.setAttribute("filter", filter);
        request.setAttribute("axioms", showAxioms ? "true" : "false");
        request.setAttribute("theorems", showTheorems ? "true" : "false");
        
        
    }
    
}