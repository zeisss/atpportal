<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="Delete ATP" />
</jsp:include>

    <div class="box">
        <div class="title">Delete ATP ${algebra.name}?</div>
        <div class="content">
            <div>
                Are you sure you want to delete the atp &quot;${atp.name}&quot;?
                All unexecuted jobs will be marked with an error.
                All proofs remain untouched.
            </div>
            
            <div class="buttons">
                <form method="POST" action="<c:url value='/atp/delete' />">
                    <a href="<c:url value='/atp/list' />">Abort</a>
                    &nbsp;&nbsp;&nbsp;
                
                    <input type="hidden" name="atp_id" value="${atp.id}" />
                    <input type="submit" value="Delete" name="delete" />
                </form>
            </div>
            
        </div>
    </div>
<jsp:include page="/views/layout/footer.jsp" />