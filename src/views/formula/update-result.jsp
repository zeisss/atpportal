<%@ page contentType="text/javascript; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="dump" uri="dump" %>
<c:choose>
<c:when test="${requestScope.messages != null}">{
    "type":"error",
    "messages":[
<c:forEach var="message" items="${requestScope.messages}">
<dump:dump value="${message}" mode="js" />,
</c:forEach>
]}</c:when>
<c:otherwise>{"type":"OK"}</c:otherwise>
</c:choose>