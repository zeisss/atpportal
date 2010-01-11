<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="Proof" />
</jsp:include>
 
    <style>
        .detail_key {
            font-weight:bold;
        }
        
        .proofstep_line {
            font-family:monospace;
        }
        .prootstep_formula {
            
        }
        .proofstep_reasoning {
            
        }
        
        .formula {
        }
        .formula_name {
            display:inline !important;
        }
        .formula_actions {
            right:5px;
            top:2px;
            position:absolute;
        }
    </style>
    
    <script>
        $(document).ready(function() {
            $("button.formula_view").click(function() {
                id = $(this).parent().parent().attr("id").substring("formula-".length);
                window.location = "<c:url value='/formula/' />" + id;
            });
        });
    </script>
    
    <div class="box" style="width:97%">
        <div class="title">Proof ${proof.id}</div>
        <div class="content">
            <table>
                <tr>
                    <td>Goal formula:</td>
                    <td>${proof.queueJob.goalFormula}
                </tr>
                <tr>
                    <td>Date:</td>
                    <td><fmt:formatDate value="${proof.time}" type="both" /></td>
                </tr>
                <tr>
                    <td>User:</td>
                    <td>${proof.queueJob.account.displayName}</td>
                </tr>
            </table>
        </div>
    </div>
    
    <div class="list" style="width:97%">
        <div class="title">Proof Steps</div>
        <div class="listmodel">
          <c:forEach var="step" items="${proof.proofSteps}" varStatus="rowCounter">
            <c:choose>
                <c:when test="${rowCounter.count % 2 == 0}">
                    <c:set var="rowStyle" scope="page" value="even"/>
                </c:when>
                <c:otherwise>
                    <c:set var="rowStyle" scope="page" value="odd"/>
                </c:otherwise>
            </c:choose>
            <div class="listentry proofstep ${rowStyle}">
                <span class="proofstep_line">${step.line}</span>
                <span class="proofstep_formula">${step.formula}</span>
                <span class="proofstep_reasoning">${step.reasoning}</span>
            </div>
          </c:forEach>
        </div>
    </div>
    
    <div class="list" style="width:97%">
        <div class="title">Proved formulas</div>
        <div class="listmodel">
            <c:forEach var="formula" items="${proved_formulas}" varStatus="rowCounter">
                <c:choose>
                    <c:when test="${rowCounter.count % 2 == 0}">
                      <c:set var="rowStyle" scope="page" value="even"/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="rowStyle" scope="page" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <div class="${rowStyle} listentry formula" id="formula-${formula.id}">
                    <span class="formula_text">${formula.formulaText}</span>
                    <span class="formula_name">
                        <c:if test="${formula.name != null && formula.name != ''}">
                            &nbsp;(${formula.name})
                        </c:if>
                    </span>
                    <span class="formula_actions">
                        <button class="formula_view">View</button>
                    </span>
                </div>
            </c:forEach>
        </div>
    </div>
    
    <div class="list" style="width:97%">
        <div class="title">Proof Details</div>
        <div class="listmodel">
          <c:forEach var="detail" items="${proof.details}" varStatus="rowCounter">
            <c:choose>
                    <c:when test="${rowCounter.count % 2 == 0}">
                      <c:set var="rowStyle" scope="page" value="even"/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="rowStyle" scope="page" value="odd"/>
                    </c:otherwise>
            </c:choose>
            <div class="listentry detail ${rowStyle}">
                <span class="detail_key">${detail.key}</span>: 
                <span class="detail_value">${detail.value}</span>
            </div>
          </c:forEach>
        </div>
    </div>
    
<jsp:include page="/views/layout/footer.jsp" />