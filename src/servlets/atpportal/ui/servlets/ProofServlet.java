package atpportal.ui.servlets;

import atpportal.ui.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.tptp.model.*;

public class ProofServlet extends AtpPortalServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String proofId = request.getParameter("proof_id");
        String format = "jsp";
        
        if ( proofId == null ) {
            int i = request.getRequestURI().lastIndexOf("/");
            if ( i != -1 ) {
                proofId = request.getRequestURI().substring(i+1);
                
                if ( proofId.endsWith(".pdf")) {
                    proofId = proofId.substring(0, proofId.length()-4);
                    format = "pdf";
                }
                if ( proofId.endsWith(".mathml")) {
                    proofId = proofId.substring(0, proofId.length()-7);
                    format = "pdf";
                }
                
            }
        }
        if ( proofId == null || proofId.length() == 0) {
            addMessage("No proofId found", request);
            forwardTo("/", request, response);
            return;
        }
        
        final ProofRepository pRepo = ProofRepository.getInstance();
        final FormulaRepository fRepo = FormulaRepository.getInstance();
        
        long id = Long.valueOf(proofId);
        Proof proof = pRepo.get(id);
        if ( proof == null ) {
            addMessage("No proof for the given ID found", request);
            forwardTo("/", request, response);
            return;
        } else {
            request.setAttribute("proof", proof);
            
            Set<Formula> provedFormulas = fRepo.getProvedFormulasByProof(proof.getId());
            request.setAttribute("proved_formulas", provedFormulas);
        }
        
        // Now forward to the interface
        includeView("proof/index.jsp", request, response);
    }
    
}