package org.tptp.servlets;

import org.tptp.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.tptp.model.*;

public class DashboardServlet extends AtpPortalServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        
        // Now forward to the interface
        includeView("dashboard/index.jsp", request, response);
    }
    
}