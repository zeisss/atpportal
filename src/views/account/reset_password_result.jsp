<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="Reset Password - Account" />
</jsp:include>

    <div class="box">
        <div class="title">Password has been reseted for account ${account.displayName}</div>
        <div class="content">
            <div>
                The password has been resetted. The new password is &quot;${new_password}&quot;.
            </div>
            
            <div class="buttons">
                <a href="<c:url value='/account/list' />">Back</a>
            </div>
            
        </div>
    </div>
<jsp:include page="/views/layout/footer.jsp" />