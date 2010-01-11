<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="dump" uri="dump" %>
<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="Queue" />
    <jsp:param name="javascript">
        <jsp:attribute name="value">
            
        </jsp:attribute>
    </jsp:param>
</jsp:include>
    <style>
        .status_progress { color:yellow; }
        .status_ok { color:green; }
        .status_error { color:red; font-weight:bold; }
        
        
        .queuejob {
            position:relative;
            height:40px;
        }
        .queuejob div {
            padding-left: 10px;
        }
        
        .queuejob .queuejob_formula {
            padding-left:45px;
            font-weight:bold;
            font-family:monospace;
        }
        .queuejob_actions {
            position:absolute;
            right:5px;
            top:10px;
        }
        .queuejob_message {
            padding-left:10px;
        }
        
    </style>
    <script>
        $(document).ready(function() {
            $("#queue_new").click(function() {
                window.location = "<c:url value='/queue/new' />";    
            });
            
            $(".queuejob_proof button").click(function() {
               id = $(this).attr("id").substring("proof-".length);
               window.location = "<c:url value='/proof/' />" + id;
            });
            
            $("button.queuejob_new").click(function() {
               id = $(this).parent().parent().attr("id").substring("queuejob-".length);
               window.location = "<c:url value='/queue/new' />?queuejob_id=" + id;
            });
        });
    </script>
    
    <div class="list" style="width:100%">
        <div class="title">Queue jobs</div>
        
        <div class="buttonbar">
            <c:if test="${user_role == 'normal' || user_role == 'admin'}">
                <button id="queue_new">New</button>
            </c:if>
        </div>
        <div class="listmodel">
            <c:forEach var="job" items="${jobs}" varStatus="rowCounter">
                <c:choose>
                    <c:when test="${rowCounter.count % 2 == 0}">
                      <c:set var="rowStyle" scope="page" value="even"/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="rowStyle" scope="page" value="odd"/>
                    </c:otherwise>
                </c:choose>
                  
                <div class="listentry queuejob ${rowStyle}" id="queuejob-${job.id}">
                    <div class="top">
                        <span class="queuejob_status">
                            <c:choose>
                                <c:when test="${job.status == 0}"><span class="status_progress">NEW</span></c:when>
                                <c:when test="${job.status == 1}"><span class="status_progress">QUEUED</span></c:when>
                                <c:when test="${job.status == 2}"><span class="status_progress">LOCKED</span></c:when>
                                <c:when test="${job.status == 3}"><span class="status_ok">Done</span></c:when>
                                <c:when test="${job.status == 4}"><span class="status_error">Error</span></c:when>
                                <c:otherwise><span class="status_error">Unknown status</c:otherwise>
                            </c:choose>
                        </span>
                        <span class="queuejob_timestamp"><fmt:formatDate value="${job.createdAt}" type="both" /></span>
                        <span class="queuejob_author">by <dump:dump value="${job.account.displayName}" mode="html" /></span>
                        <span class="queuejob_message"><dump:dump value="${job.message}" mode="html" /></span>
                    </div>
                    <div>
                        <span class="queuejob_formula"><dump:dump value="${job.goalFormula}" mode="html" /></span>
                    </div>
                    
                    <div class="queuejob_actions">
                        <c:if test="${job.status == 3}">
                            <span class="queuejob_proof"><button id="proof-${job.proofId}">View Proof</button></span>
                        </c:if>
                        <c:if test="${user_role == 'normal' || user_role == 'admin'}">
                            <button class="queuejob_new">New proof</button>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    
<jsp:include page="/views/layout/footer.jsp" />