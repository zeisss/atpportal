package org.tptp.servlets;

import org.tptp.*;
import org.tptp.model.*;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class AlgebraServlet extends AtpPortalServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {   
        AlgebraRepository repo = AlgebraRepository.getInstance();
        if ( request.getRequestURI().endsWith("/algebra/new")) {
            // Create a new algebra and forward to the overview
            String name = request.getParameter("algebra_name");
            String [] axioms = request.getParameterValues("axiom");
            
            try {
                if ( repo == null ) {
                    throw new IllegalStateException("Unable to get access to the AlgebraRepository");
                }
                
                FormulaRepository fRepo = FormulaRepository.getInstance();
                if ( fRepo == null ) {
                    throw new IllegalStateException("Unable to get access to the FormulaRepository");
                }
                
                Algebra algebra = new Algebra(name);
                repo.save(algebra);
                
                if ( axioms != null )
                {
                    for ( String formula : axioms ) {
                        if ( formula == null || "".equals(formula)) {
                            continue;
                        }
                        Formula f = fRepo.getByFormulaText(formula);
                        if ( f == null ) {
                            f = new Formula("", formula);    
                            fRepo.save(f);
                        }
                        repo.link(algebra, f, true);
                    }
                }
            
                redirectTo("/algebra/", request, response);
            } catch (Exception exc) {
                log("Error while saving new algebra", exc);
                
                addMessage("Error while saving algebra: " + exc.getMessage(), request);
                includeView("algebra/new.jsp", request, response);
            }
        }
        else if ( request.getRequestURI().endsWith("/algebra/delete")) {
            String sid = request.getParameter("algebra_id");
            Long id = Long.valueOf(sid);
            
            Algebra algebra = repo.get(id);
            repo.delete(algebra);
            
            redirectTo("/algebra/", request, response);
        }
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        AlgebraRepository repo = AlgebraRepository.getInstance();
        if ( request.getRequestURI().endsWith("/algebra/new")) {
            includeView("algebra/new.jsp", request, response);
        } else if ( request.getRequestURI().endsWith("/algebra/delete")) {
            String sid = request.getParameter("algebra_id");
            Long id = Long.valueOf(sid);
            
            Algebra algebra = repo.get(id);
            request.setAttribute("algebra", algebra);
            
            includeView("algebra/delete.jsp", request, response);
        } else {
            Set<Algebra> algebren = repo.getAll();
            request.setAttribute("algebren", algebren);
            
            includeView("algebra/list.jsp", request, response);
        }
    }    
}