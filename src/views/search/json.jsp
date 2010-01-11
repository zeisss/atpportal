<%@ page contentType="text/javascript; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
[<c:forEach var="formula" items="${formulas}" varStatus="status">{
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
        ]
    }${not status.last ? ',' : ''}</c:forEach>]