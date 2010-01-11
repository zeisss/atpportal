<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="dump" uri="dump" %>

<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="New Algebra" />
</jsp:include>

    <div class="box">
        <div class="title">Delete Formula <dump:dump value="${formula.name}" mode="html" />?</div>
        <div class="content">
            <div>
                Are you sure you want to delete the formula &quot;<dump:dump value="${formula.name}" mode="html" />&quot; (and all detail data)? 
            </div>
            
            <div class="buttons">
                <form method="POST" action="<c:url value='/formula/delete' />">
                    <a href="<c:url value='/formula/${formula.id}' />">Abort</a>
                    &nbsp;&nbsp;&nbsp;
                    <input type="hidden" name="formula_id" value="${formula.id}" />
                    <input type="submit" value="Delete" name="delete" />
                </form>
            </div>
            
        </div>
    </div>
<jsp:include page="/views/layout/footer.jsp" />