<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="Reset Password - Account" />
</jsp:include>

    <div class="box">
        <div class="title">Update role for account ${account.displayName}?</div>
        <div class="content">
            <div>
                Are you sure you want to set the role for the account &quot;${account.displayName}&quot; to &quot;${role}&quot;?
            </div>
            
            <div class="buttons">
                <form method="POST" action="<c:url value='/account/update_role' />">
                    <a href="<c:url value='/account/list' />">Abort</a>
                    &nbsp;&nbsp;&nbsp;
                
                    <input type="hidden" name="account_id" value="${account.id}" />
                    <input type="hidden" name="role" value="${role}" />
                    <input type="submit" value="Update role" />
                </form>
            </div>
            
        </div>
    </div>
<jsp:include page="/views/layout/footer.jsp" />