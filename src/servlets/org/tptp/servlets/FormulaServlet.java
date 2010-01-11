package org.tptp.servlets;

import org.tptp.*;
import org.tptp.model.*;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class FormulaServlet extends AtpPortalServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String requestURI = request.getRequestURI();
        if ( requestURI.endsWith("/formula/delete")) {
            doPostDelete(request, response);
        }
        else if ( requestURI.endsWith("/formula/update")) {
            doPostUpdate(request, response);
        } else if ( requestURI.endsWith("/formula/makeAxiom")) {
            doPostMakeAxiom(request,response);
        }
    }
    
    public void doPostMakeAxiom(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        
    }
    
    public void doPostDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        FormulaRepository repo = FormulaRepository.getInstance();
        long id = Long.valueOf(request.getParameter("formula_id"));
        Formula f = repo.get(id);
        
        repo.delete(f);
        
        redirectTo("/formula/", request, response);
    }
    public void doPostUpdate(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        try {
            FormulaRepository repo = FormulaRepository.getInstance();
            
            // Get the form data
            String id = request.getParameter("formula_id");
            if ( id == null || "".equals(id)) {
                throw new IllegalArgumentException("Formula ID is empty or not given.");
            }
            
            Formula formula = repo.get(Long.valueOf(id));
            
            
            // Update: Normal head data
            String name = request.getParameter("formula_name");
            String comment = request.getParameter("formula_comment");
            
            if ( formula == null ) {
                throw new IllegalArgumentException("Formula with ID not found: " + id);
            }
            if ( name != null )
                formula.setName(name);
             
            if ( comment != null ) {
                formula.setComment(comment);
            }
            
            
            // Now the references
            String[] refCounters = request.getParameterValues("formula_reference");
            formula.clearReferences();
            
            if ( refCounters != null )
            {
                for ( String ref : refCounters ) {
                    String abbr = request.getParameter("formula_reference_" + ref + "_abbr");
                    String authors = request.getParameter("formula_reference_" + ref + "_authors");
                    String title = request.getParameter("formula_reference_" + ref + "_title");
                    String year = request.getParameter("formula_reference_" + ref + "_year");
                    
                    
                    if ( "".equals(abbr))
                        throw new IllegalArgumentException("Empty abbreviation is now allowed");
                    
                    
                    int y = -1;
                    try {
                        y = Integer.valueOf(y);
                    } catch(NumberFormatException exc2) {
                        ; // IGNORE
                    }
                    
                    FormulaReference obj = new FormulaReference(
                        abbr,
                        authors,
                        title,
                        y
                    );
                    formula.addReference(obj);
                }
                
            }
            repo.update(formula);
            
        } catch (Exception exc) {
            log("Error in POST /formula/", exc);
            addMessage(exc.getMessage(), request);
            
        }
        includeView("formula/update-result.jsp", request, response);
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String requestURI = request.getRequestURI();
        if ( requestURI.endsWith("/formula/delete")) {
            doDeleteForm(request, response);
        }
        else
        {
            int i = requestURI.indexOf("/formula/");
            String id = requestURI.substring(i + "/formula/".length());
            String format = "jsp";
            if ( id.endsWith(".pdf")) {
                format = "pdf"; 
                id = id.substring(0,id.length()-4);
            } else if ( id.endsWith(".mathml")) {
                format = "mathml";
                id = id.substring(0, id.length()-7);
            } else if ( id.endsWith(".json")) {
                format = "json";
                id = id.substring(0, id.length() - 5);
            }
            doShowForm(id, format, request, response);
        }
    }
    public void doDeleteForm(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        FormulaRepository repo = FormulaRepository.getInstance();
        
        Long id = Long.valueOf(request.getParameter("formula_id"));
        
        Formula f = repo.get(id);
        request.setAttribute("formula", f);
        
        includeView("formula/delete.jsp", request, response);
    }
    
    public void doShowForm(String id, String format, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // The variables
        Formula formula = null;
        Set<Algebra> algebren, formulaAxiomAlgebren = null, formulaTheoremAlgebren = null;
        Set<Proof> proofs = null;
        
        // Where the values come from
        FormulaRepository fRepo = FormulaRepository.getInstance();
        AlgebraRepository repo = AlgebraRepository.getInstance();
        ProofRepository pRepo = ProofRepository.getInstance();
        
        // Get them
        algebren = repo.getAll();
        request.setAttribute("algebren", algebren);
        
        if ( !"".equals(id))
        {
            try {
                formula = fRepo.get(Long.valueOf(id));
                formulaAxiomAlgebren = repo.getAlgebrenForAxiom(formula);
                if ( formulaAxiomAlgebren.size() == 0 )
                    formulaAxiomAlgebren = null;
                    
                formulaTheoremAlgebren = repo.getAlgebrenForTheorem(formula);
                if ( formulaTheoremAlgebren.size() == 0 )
                    formulaTheoremAlgebren = null;
                    
                proofs = pRepo.getProofsForFormula(formula);
                if ( proofs.size() == 0 )
                    proofs = null;
            } catch (Exception exc) {
                addMessage("Error while loading object: "+ exc.getMessage(), request);
                log("Error in /formula/<id>", exc);
                
                formula = null;
                formulaAxiomAlgebren = null;
                formulaTheoremAlgebren = null;
                proofs = null;
            }
        }
        
        request.setAttribute("formula", formula);
        request.setAttribute("formula_axiom_algebras", formulaAxiomAlgebren);
        request.setAttribute("formula_theorem_algebras", formulaTheoremAlgebren);
        request.setAttribute("formula_proofs", proofs);
        
        if ( format.equals("json")) {
            includeView("formula/json.jsp", request, response);
        } else {
            includeView("formula/index.jsp", request, response);    
        }
        
    }
}