<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="Welcome" />
</jsp:include>

   <div class="box" style="width:70%; height:500px">
    <div class="title">Welcome</div>
    <div class="content">
        Bla bla bla bla bla<br />
        <%= request.getServletPath() %><br />
        <%= request.getPathInfo() %><br />
        Request URI: <%= request.getRequestURI() %><br />
    </div>
   </div>
   
   <div class="list" style="width:20%; height:500px;">
     <div class="title">Queue</div>
     <div class="listmodel">
        <div class="listentry queuejob odd">
            <span class="queuejob_formula">x = 2</span>
            <span class="queuejob_account">Zeissler</span>
            <span class="queuejob_timestamp">10 min ago</span>
        </div>
        <div class="listentry queuejob even">
            <span class="queuejob_formula">x = 3</span>
            <span class="queuejob_account">Zeissler</span>
            <span class="queuejob_timestamp">15 min ago</span>
        </div>
        <div class="listentry queuejob odd">
            <span class="queuejob_formula">x = 4</span>
            <span class="queuejob_account">Zeissler</span>
            <span class="queuejob_timestamp">3 days ago</span>
        </div>
     </div>
   </div>
   
   <div style="clear:both" ></div><!-- New row -->
   

<jsp:include page="/views/layout/footer.jsp" />