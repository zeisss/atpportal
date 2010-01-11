<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="New Algebra" />
</jsp:include>

    <div class="box">
        <div class="title">Delete Algebra ${apt.name}?</div>
        <div class="content">
            <div>
                Are you sure you want to delete the algebra &quot;${algebra.name}&quot; (and all connections to the formulas)? The formulas itself
                remain untouched.
            </div>
            
            <div class="buttons">
                <form method="POST" action="<c:url value='/algebra/delete' />">
                    <a href="<c:url value='/algebra/' />">Abort</a>
                    &nbsp;&nbsp;&nbsp;
                
                    <input type="hidden" name="algebra_id" value="${algebra.id}" />
                    <input type="submit" value="Delete" name="delete" />
                </form>
            </div>
            
        </div>
    </div>
<jsp:include page="/views/layout/footer.jsp" />
