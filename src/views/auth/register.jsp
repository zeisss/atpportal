<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="Register" />
</jsp:include>

   <div class="box" style="width:95%; height:500px">
    <div class="title">Register</div>
    <div class="content">
        <form method="POST" action="<c:url value='/auth/register' />">
            <table>
                <tr>
                    <td><label for="email">Email:</label></td>
                    <td><input type="text" name="email"></td>
                </tr>
                <tr>
                    <td><label for="login_name">Login Name:</label></td>
                    <td><input type="text" name="login_name"></td>
                </tr>
                <tr>
                    <td><label for="login_password">Login Password:</label></td>
                    <td><input type="password" name="login_password"></td>
                </tr>
                <tr>
                    <td><label for="login_password2">Repeat Password:</label></td>
                    <td><input type="password" name="login_password2"></td>
                </tr>
                <tr>
                    <td cols="2"><input type="submit" value="Register" /></td>
                </tr>
            </table>
        </form>
    </div>
   </div>
   
   <div style="clear:both" ></div><!-- New row -->
   

<jsp:include page="/views/layout/footer.jsp" />