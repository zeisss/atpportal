<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="Reset Password - Account" />
</jsp:include>

    <div class="box">
        <div class="title">Reset password for account ${account.displayName}?</div>
        <div class="content">
            <div>
                Are you sure you want to reset the password for the account &quot;${account.displayName}&quot;?
                A new password is displayed, which you have to tell the user. There is currently NO automated way
                to send it to the user.
            </div>
            
            <div class="buttons">
                <form method="POST" action="<c:url value='/account/reset_password' />">
                    <a href="<c:url value='/account/list' />">Abort</a>
                    &nbsp;&nbsp;&nbsp;
                
                    <input type="hidden" name="account_id" value="${account.id}" />
                    <input type="submit" value="Reset Password" />
                </form>
            </div>
            
        </div>
    </div>
<jsp:include page="/views/layout/footer.jsp" />