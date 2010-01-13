package org.tptp.servlets;

import org.tptp.*;
import org.tptp.model.*;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class QueueServlet extends AtpPortalServlet {
    public static final int JOBS_PER_PAGE = 25;
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        log("GET /queue | " + request.getRequestURI());
        
        if ( request.getRequestURI().endsWith("/queue/new")) {
            doGetNew(request, response);
        } else if ( request.getRequestURI().endsWith("/queue/list")) {
            doGetIndex(request, response);
        } else {
            String r = request.getRequestURI();
            int i = r.indexOf("/queue/");
            
            if ( i == -1) {
                this.redirectTo("/queue/list", request, response);    
            } else {
                i += "/queue/".length();
                int j = r.indexOf("/", i);
                if ( j == -1 ) {
                    this.redirectTo("/queue/list", request, response);   
                } else {
                    String id = r.substring(i, j);
                    doGetResource(id, request, response);
                    
                    
                }
                
                
            }
        }
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        log("POST /queue | " + request.getRequestURI());
        
        if ( request.getRequestURI().endsWith("/queue/new")) {
            doPostNew(request, response);
        } else {
            this.redirectTo("/queue/list", request, response);   
        }
    }
    
    public void doGetResource(String id, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        QueueJobRepository jobRepo = QueueJobRepository.getInstance();
        
        try {
            QueueJob job = jobRepo.get(Long.valueOf(id));
            response.setContentType("text/plain");
            PrintWriter writer = response.getWriter();
            
            if (request.getRequestURI().endsWith("/input")) {
                writer.println(job.getInputText());
            } else if ( request.getRequestURI().endsWith("/output")) {
                writer.println(job.getOutputText());
            } else {
                this.redirectTo("/queue/list", request, response);   
            }
            
        } catch (Exception exc) {
            this.handleException(exc, request, response, "/queue/list");
        }
    }
    
    public void doGetNew(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        AlgebraRepository repo = AlgebraRepository.getInstance();
        AtpRepository atpRepo = AtpRepository.getInstance();
        
        
        Set<Algebra> algebren = repo.getAll();
        request.setAttribute("algebren", algebren);
        
        List<Atp> atps = atpRepo.getAll();
        request.setAttribute("atps", atps);
        
        if ( request.getParameter("queuejob_id") != null) {
            QueueJobRepository qRepo = QueueJobRepository.getInstance();
            FormulaRepository fRepo = FormulaRepository.getInstance();
            
            QueueJob job = qRepo.get(Long.valueOf(request.getParameter("queuejob_id")));
            request.setAttribute("queuejob", job);
            request.setAttribute("formula", job.getGoalFormula());
            
            Set<Formula> input = fRepo.getFormulasForQueueJob(job.getId());
            request.setAttribute("input", input);
        }
        else if ( request.getParameter("goalText") != null) {
            request.setAttribute("formula", request.getParameter("formula"));
        } else {
            request.setAttribute("formula", "");
        }
        
        includeView("queue/new.jsp", request, response);
    }    
        
    public void doGetIndex(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        QueueJobRepository repo = QueueJobRepository.getInstance();
        List<QueueJob> jobs = repo.getRange(0, JOBS_PER_PAGE);
        request.setAttribute("jobs", jobs);
        
        includeView("queue/list.jsp", request, response);
    }
    
    
    public void doPostNew(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        final String goalText = request.getParameter("goaltext");
        final String [] formula_ids = request.getParameterValues("formula_id");
        final String atpId = request.getParameter("atp_id");
        
        final Account account = (Account) request.getSession().getAttribute("account");
        
        try {
            final AtpRepository atpRepo = AtpRepository.getInstance();
            final QueueJobRepository qRepo = QueueJobRepository.getInstance();
            final FormulaRepository fRepo = FormulaRepository.getInstance();
                
            final Atp atp = atpRepo.get(Long.valueOf(atpId));
            if ( atp == null ) {
                throw new IllegalArgumentException("Invalud AtpID");
            }
            
            // Perform the enqueue steps in a transaction
            WorkManager.run(new Runnable() { public void run() {
                // Create the job object
                QueueJob job = new QueueJob(goalText, atp, account);
                qRepo.save(job);
                
                // Add the input formulas
                if ( formula_ids != null ) {
                    for(String formulaId : formula_ids) {
                        Formula f = fRepo.get(Long.valueOf(formulaId));
                        qRepo.storeInputFormula(job,f);
                    }
                }
                
                // And mark it as ready for execution
                job.setStatus(QueueJob.STATUS_QUEUED);
                qRepo.update(job);
            }});
            
            log("New queuejob enqueued: " + goalText);
            
            redirectTo("/queue/list", request, response);
        }
        catch(Throwable t) {
            this.handleException(t, request, response, "/queue/list");
        }
        
    }
}