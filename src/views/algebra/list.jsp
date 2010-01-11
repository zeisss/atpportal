<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="Algebra" />
</jsp:include>
 <style>
    div.algebra {
        height:40px;
        position:relative;
    }
    div.algebra-name {
        position:relative;
        padding:20px;
    }
    .algebra_actions {
        right:10px;
        top:10px;
        position:absolute;
    }
 </style>
 <script>
   $(document).ready(function() {
    $("#new_algebra").click(function() {
        window.location = "<c:url value='/algebra/new' />";
        return false;
    });
    
    
   });
 </script>
 
    <div class="list" style="width:100%">
        <div class="title">Algebras</div>
        <div class="buttonbar">
            <button id="new_algebra">New</button>
        </div>
        <div class="listmodel">
            <c:forEach var="algebra" items="${algebren}" varStatus="rowCounter">
                <c:choose>
                    <c:when test="${rowCounter.count % 2 == 0}">
                      <c:set var="rowStyle" scope="page" value="odd"/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="rowStyle" scope="page" value="even"/>
                    </c:otherwise>
                </c:choose>
                <div class="listentry algebra ${rowStyle}">
                    <div class="algebra_name">${algebra.name}</div>
                    <span class="algebra_actions">
                        <form method="GET" action="<c:url value='/algebra/delete' />">
                            <input type="hidden" name="algebra_id" value="${algebra.id}" />
                            <button class="algebra_action_delete">Delete</button>
                        </form>
                    </span>
                </div>
            </c:forEach>
        </div>
    </div>
    
<jsp:include page="/views/layout/footer.jsp" />