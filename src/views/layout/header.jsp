<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
  <c:choose>
    <c:when test="${param.title != null}">
        <title>${param.title} - ATPPortal</title>
    </c:when>
    <c:otherwise>
        <title>ATPPortal</title>
    </c:otherwise>
  </c:choose>
  
  <link rel="stylesheet" type="text/css" href="<c:url value='/css/style.css' />" media="screen" />
  <meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
  <script src="<c:url value='/js/jquery-1.3.2.js' />" type="text/javascript"></script>
  <script src="<c:url value='/js/jquery.once-1.1.js' />" type="text/javascript"></script>
 </head>
 <body>
    <div id="header">
        <h1><a href="<c:url value='/' />">ATPPortal</a></h1>
        <div id="navigation">
            <c:choose>
                <c:when test="${sessionScope.account != null}">
                    <a href="<c:url value='/auth/profile' />">${sessionScope.account.displayName}</a>
                    (<a href="<c:url value='/auth/logout' />">Logout</a>)
                </c:when>
                <c:otherwise>
                    <a href="<c:url value='/auth/login' />">Login</a>&nbsp;
                    <a href="<c:url value='/auth/register' />">Register</a>
                </c:otherwise>
            </c:choose>
            
        </div>
    </div>
    
    <div id="body">
        <ul id="pages">
            <%-- <li><a href="<c:url value='/dashboard' />"><span>Home</span></a></li> --%>
            <li><a href="<c:url value='/search' />"><span>Search</span></a></li>
            <li><a href="<c:url value='/formula/' />"><span>Formulas</span></a></li>
            <li><a href="<c:url value='/queue/list' />"><span>Queue</span></a></li>
            <c:if test="${user_role == 'normal' || user_role == 'admin'}">
                <li style="padding-left:10px"><a href="<c:url value='/queue/new' />"><span>Prover</span></a></li>
            </c:if>
            
            <c:if test="${user_role == 'admin'}">
                <li style="float:right"><a href="<c:url value='/atp/list' />"><span>Atp</span></a></li>
                <li style="float:right"><a href="<c:url value='/account/list' />"><span>Account</span></a></li>
                <li style="float:right"><a href="<c:url value='/algebra/list' />"><span>Algebra</span></a></li>
            </c:if>
        </ul>
        
        <div id="content">
            
            <c:if test="${requestScope.messages != null}">
                <ul>
                    <c:forEach var="message" items="${requestScope.messages}">
                        <li>${message}</li>
                    </c:forEach>
                </ul>
            </c:if>
            