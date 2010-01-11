<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="Search" />
    <jsp:param name="javascript">
        <jsp:attribute name="value">
     
        </jsp:attribute>
    </jsp:param>
</jsp:include>
    <style>
        div.selected {
            background-color:yellow;
        }
    </style>
    <script>
        function selectAlgebra() {
            id = $(this).attr("id").substring("algbera-".length);
            name = $(".algebra_name", this).text()
            
            
            // Update the form
            // $("#search_title").text("Formula (Algebra: " + name + ")");
            $("#algebra_id").attr("value", id);
            
            $("div.algebra").removeClass("selected");
            $(this).addClass("selected");
            
            $("#search_form").submit();
        }
        
        // Open the selected formula
        function selectFormula() {
            id = $(this).attr("id").substring("formula-".length);
            
            window.location = "<c:url value='/formula' />/" + id;
        }
        $(document).ready(function() {
            $(".algebra").click(selectAlgebra);
            $(".formula").click(selectFormula);
        });
    </script>   
        
   <div class="list" style="width:25%; height:500px;">
    <div class="title">Algebra</div>
    <div class="listmodel">
        <div class="listentry algebra even ${requestScope.algebra_id == 'all' ? 'selected' : ''}" id="algebra_all">
            <span class="algebra_name">All algebras</span>
        </div>
        <c:forEach var="algebra" items="${algebren}" varStatus="rowCounter">
            <c:choose>
              <c:when test="${rowCounter.count % 2 == 0}">
                <c:set var="rowStyle" scope="page" value="even"/>
              </c:when>
              <c:otherwise>
                <c:set var="rowStyle" scope="page" value="odd"/>
              </c:otherwise>
            </c:choose>
            <div class="listentry algebra ${rowStyle} ${requestScope.algebra_id == algebra.id ? 'selected' : ''}" id="algebra-${algebra.id}">
                <span class="algebra_name">${algebra.name}</span>
            </div>
        </c:forEach>
    </div>
   </div>
   
   <div class="list" style="width:73%; height:500px;">
    <div class="buttonbar">
        <form method="GET" id="search_form" action="<c:url value='/search' />">
            <input type="hidden" name="algebra_id" id="algebra_id" value="${requestScope.algebra_id}" />
            
            <input type="checkbox" name="axioms" value="true" ${requestScope.axioms == 'true' ? 'checked="checked"' : ''} />
            <label for="Axiom">Axiom</label>
           
            <input type="checkbox" name="theorems" value="true" ${requestScope.theorems == 'true' ? 'checked="checked"' : ''} />
            <label for="theorem">Theorem</label>
           
            <spacer style="width:10px" />
           
            <input style="width:100px" type="text" name="filter" id="filter" value="${requestScope.filter}" />
           
            <input type="submit" value="Search" />
        </form>
     </div>
     <div class="title" id="search_title">Formulas</div>
     <div class="listmodel">
        
        <c:forEach var="formula" items="${formulas}" varStatus="rowCounter">
            <c:choose>
              <c:when test="${rowCounter.count % 2 == 0}">
                <c:set var="rowStyle" scope="page" value="odd"/>
              </c:when>
              <c:otherwise>
                <c:set var="rowStyle" scope="page" value="even"/>
              </c:otherwise>
            </c:choose>
            
            <div class="listentry formula ${rowStyle}" id="formula-${formula.id}">
                <span class="formula_name">&nbsp;${formula.name}</span>
                <span class="formula_formula">${formula.formulaText}</span>
                
            </div>
        </c:forEach>
     </div>
   </div>
   
   <div style="clear:both" ></div><!-- New row -->
   

<jsp:include page="/views/layout/footer.jsp" />