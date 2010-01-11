<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="dump" uri="dump" %>
{
    "id" : "${formula.id}",
    "name": <dump:dump value="${formula.name}" mode="js" />,
    "formulaText": <dump:dump value="${formula.formulaText}" mode="js" />,
    "comment": <dump:dump value="${formula.comment}" mode="js" />,
    "references": [
        <c:forEach var="reference" items="${formula.references}" varStatus="statusRef">
        {
            "abbreviation": <dump:dump value="${reference.abbreviation}" mode="js" />,
            "authors": <dump:dump value="${reference.authors}" mode="js" />,
            "title": <dump:dump value="${reference.title}" mode="js" />,
            "year": <dump:dump value="${reference.year}" mode="js" />
        }
        ${not statusRef.last ? ',' : ''}
        </c:forEach>
    ],
    "proofs": [
        <c:forEach var="proof" items="${formula_proofs}" varStatus="varProof">
        {
            "id": <dump:dump value="${proof.id}" mode="js" />,
            "time": "<fmt:formatDate value="${proof.time}" type="both" />",
            "goalFormula": <dump:dump value="${proof.queueJob.goalFormula}" mode="js" />,
            
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
        <c:forEach var="algebra" items="${formula_axiom_algebras}"><dump:dump value="${algebra.name}" mode="js" />,</c:forEach>
    ],
    "theorem_algebras": [
        <c:forEach var="algebra" items="${formula_theorem_algebras}"><dump:dump value="${algebra.name}" mode="js" />,</c:forEach>
    ]
}