<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/views/layout/header.jsp" />
    <div style="padding-left:20px; padding-bottom:40px">
        <h1>Profile for ${sessionScope.account.displayName}</h1>
        <table>
            <form method="POST" action="<c:url value='/auth/profile' />">
            
                <tr>
                    <td><label for="display_name">Display Name:</label></td>
                    <td><input type="text" name="display_name" value="${account.displayName}" /></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" value="Save" /></td>
                </tr>
                
            </form>
            
            <form method="POST" action="<c:url value='/auth/resetPassword' />">
                
                <tr>
                    <td><label for="login_password">Password:</label></td>
                    <td><input type="password" name="login_password" value="" /></td>
                </tr>
                <tr>
                    <td><label for="login_password"2>Repeat Password:</label></td>
                    <td><input type="password" name="login_password2" value="" /></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" value="Save" /></td>
                </tr>
            </form>
        </table>
    </div>
    
<jsp:include page="/views/layout/footer.jsp" />      