<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
{
    "id" : "${formula.id}",
    "name": "${formula.name}",
    "formulaText": "${formula.formulaText}",
    "comment": "${formula.comment}",
    "references": [
        <c:forEach var="reference" items="${formula.references}" varStatus="statusRef">
        {
            "abbreviation": "${reference.abbreviation}",
            "authors": "${reference.authors}",
            "title": "${reference.title}",
            "year": "${reference.year}"
        }
        ${not statusRef.last ? ',' : ''}
        </c:forEach>
    ],
    "proofs": [
        <c:forEach var="proof" items="${formula_proofs}" varStatus="varProof">
        {
            "id": "${proof.id}",
            "time": "<fmt:formatDate value="${proof.time}" type="both" />",
            "goalFormula": "${proof.queueJob.goalFormula}",
            
            "steps_count": ${fn:length(proof.proofSteps)},
            "steps": [
                <c:forEach var="step" items="${proof.proofSteps}" varStatus="varStep">
                    {
                        "line": ${step.line},
                        "formula": "${step.formula}",
                        "reasoning": "${step.reasoning}"
                    }${not varStep.last ? ',' : ''}
                </c:forEach>
            ],
            "details_count": ${fn:length(proof.details)},
            <%--
            "details": {
                <c:forEach var="detail" items="${proof.details}" varStatus="varDetail">
                "${detail.key}": "${detail.value}"${not varDetail.last ? ',' : ''}
                </c:forEach>
            }--%>
        }${not varProof.last ? ',' : ''}
        </c:forEach>
    ],
    "axiom_algebras": [
        <c:forEach var="algebra" items="${formula_axiom_algebras}">"${algebra.name}",</c:forEach>
    ],
    "theorem_algebras": [
        <c:forEach var="algebra" items="${formula_theorem_algebras}">"${algebra.name}",</c:forEach>
    ]
}