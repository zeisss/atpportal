<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import='org.tptp.model.*' %>

<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="ATPs" />
</jsp:include>
    <style>
        .atp {
            position:relative;
            height:30px;
        }
        .atp_actions {
            position:absolute;
            right:10px;
            top:5px;
        }
    </style>
    <script>
        $(document).ready(function() {
           $("#atp_new").click(function() {
                window.location = "<c:url value='/atp/new' />";
           });
           $(".atp_delete").click(function() {
                id = $(this).parent().parent().attr("id").substring(4);
                window.location = "<c:url value='/atp/delete' />?atp_id=" + id;
           });
           $(".atp_asnew").click(function() {
                id = $(this).parent().parent().attr("id").substring(4);
                window.location = "<c:url value='/atp/new' />?atp_id=" + id;
           });
        });
    </script>
    <div class="list" style="width:97%">
        <div class="title">ATPs</div>
        <div class="buttonbar">
            <button id="atp_new">New ATP</button>
        </div>
        <div class="listmodel">
            <c:forEach var="atp" items="${atps}" varStatus="rowCounter">
                <c:choose>
                  <c:when test="${rowCounter.count % 2 == 0}">
                    <c:set var="rowStyle" scope="page" value="odd"/>
                  </c:when>
                  <c:otherwise>
                    <c:set var="rowStyle" scope="page" value="even"/>
                  </c:otherwise>
                </c:choose>
                
                <div class="listentry atp ${rowStyle}" id="atp-${atp.id}">
                    <span class="atp_name">${atp.name} (${atp.atpName} - ${atp.atpVersion})</span>
                    
                    <div class="atp_actions">
                        <button class="atp_asnew">As new</button>
                        <button class="atp_delete">Delete</button>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
<jsp:include page="/views/layout/footer.jsp" />      