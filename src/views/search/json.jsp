<%@ page contentType="text/javascript; charset=UTF-8" %>
<%@ taglib prefix="dump" uri="dump" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
[<c:forEach var="formula" items="${formulas}" varStatus="status">
    {
        "id" : <dump:dump value="${formula.id}" mode="js" />,
        "name": <dump:dump value="${formula.name}" mode="js" />,
        "formulaText": <dump:dump value="${formula.formulaText}" mode="js" />,
        "comment": <dump:dump value="${formula.comment}" mode="js" />,
        "references": [
            <c:forEach var="reference" items="${formula.references}" varStatus="statusRef">
            {
                "abbreviation": <dump:dump value="${reference.abbreviation}" mode="js" />,
                "authors":      <dump:dump value="${reference.authors}" mode="js" />,
                "title":        <dump:dump value="${reference.title}" mode="js" />,
                "year":         <dump:dump value="${reference.year}" mode="js" />
            }
            ${not statusRef.last ? ',' : ''}
            </c:forEach>
        ]
    }${not status.last ? ',' : ''}</c:forEach>
]