<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/views/layout/header.jsp" />
    <div  style="padding-left:20px; padding-bottom:40px;">
        <h2>Login</h2>        
        <form method="POST" action="<c:url value='/auth/login' />">
            <table>
                <tr>
                    <td><label for="login_name">Username</label></td>
                    <td><input type="text" name="login_name" /></td>
                </tr>
                <tr>
                    <td><label for="login_passsword">Password</label></td>
                    <td><input type="password" name="login_password" /></td>
                </tr>
                <tr>
                    <td colspan="2" align="right">
                        <input type="submit" value="Login" />
                    </td>
                </tr>
            </table>
        </form>
    </div>
<jsp:include page="/views/layout/footer.jsp" />      