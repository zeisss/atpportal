<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="New ATP" />
</jsp:include>

    <style>
        textarea.atp {
            width:90%;
        }
        
        .atp_option div {
            display:inline-block;
            
        }
        
        .atp_option div.label {
            width:30%;
            height:100%;
        }
        .atp_option div.label label {
            text-align:top;
            height:100%;
        }
        
        .atp_option div.value {
            width:60%;
        }
        .atp_option div.value input {
            width:90%;
        }
        
    </style>
    <script>
            provers = [
                <c:forEach var="prover" items="${provers}" varStatus="varStatus">
                {
                  "name": "${prover.name}",
                  "version": "${prover.version}",
                  "description":"${prover.description}",
                  "prover": "${prover.prover}",
                  "options": [
                    <c:forEach var="option" items="${prover.optionDescriptionMap}">
                    {
                        "name": "${option.key}",
                        "description": "${option.value}",
                        "required": "true"
                    },
                    </c:forEach>
                    <c:forEach var="option" items="${prover.optionalOptionDescriptionMap}">
                    {
                        "name": "${option.key}",
                        "description": "${option.value}",
                        "required": "false"
                    },
                    </c:forEach>
                  ]
                },
                </c:forEach>
            ];
            
            function select_prover(prover) {
                $("#atp_name").attr("value", prover.name);
                $("#atp_version").attr("value", prover.version);
                $("#atp_description").text(prover.description);
                
                $("#atp_listmodel div").remove();
                for (x in prover.options)
                {
                    option = prover.options[x];
                    
                    $("#atp_listmodel").append(
                        "<div class=\"listentry atp_option\">" +
                            "<input type=hidden name=option_key value=\"" + option.name + "\" />\n" + 
                            "<div class=label><label for=option_" + option.name + "_value>" + option.name + ":</label><br/>&nbsp;</div>\n" +
                            "<div class=value>" +
                                (option.required == "true" ? "*":"") +
                                "<input type=\"text\" class=atp_option_value id=\"option_" + option.name + "_value\" name=\"option_" + option.name + "_value\" /><br />\n" +
                                "<span>" + option.description + "</span>\n" +
                            "</div>" +
                        "</div>"
                    );
                    
                }
                
                return false;
            }
            
            $(document).ready(function() {
                $("#atp_prover").change(function() {
                    prover = provers[$("#atp_prover").val()];
                    select_prover(prover);
                });
                
                init = false;
                for (x in provers)
                {
                    p = provers[x];
                    if ( !init ) {
                        select_prover(p);
                        init = true;
                    }
                    $("#atp_prover").append(
                      "<option value=" + x + ">" + p.name + " - " + p.version + "</option>"  
                    );
                }
                
                <c:if test="${atp != null}">
                    $("#name").attr("value", "${atp.name}");
                    
                    for (x in provers) {
                        if ( provers[x].name == "${atp.atpName}" && provers[x].version == "${atp.atpVersion}") {
                            select_prover(provers[x]);
                        }
                    }
                    <c:forEach var="option" items="${atp.options}">
                    $("#option_${option.key}_value").attr("value", "${option.value}");
                    </c:forEach>
                </c:if>
            });
    </script>
    <div class="box" style="width:100%">
        <div class="title">New ATP</div>
        
        <div class="content" style="padding:10px">
            <form method="POST" action="<c:url value='/atp/new' />">
                <input type="hidden" id="atp_name" name="atp_name" value="" />
                <input type="hidden" id="atp_version" name="atp_version" value="" />
                <table>
                    <tr>
                        <td><label for="name">Name:</label></td>
                        <td><input type="text" id="name" name="name" /></td>
                    </tr>
                    <tr>
                        <td><label for="atp_prover">Prover:</label></td>
                        <td>
                            <select id="atp_prover">
                            </select><br />
                            <span id="atp_description"></span>
                        </td>
                    </tr>
                </table>
                
                <div style="margin-top:5px; margin-bottom:5px;">
                    Choose a Theorem Prover and configure its options below.
                </div>
                
                <div class="list" style="width:100%">
                    <div class="title">Options (* = required)</div>
                    
                    <div class="listmodel" id="atp_listmodel">
                    </div>
                </div>
                <div>
                    <input type="submit" value="Save" />
                </div>
            </form>
        </div>
    </div>
<jsp:include page="/views/layout/footer.jsp" />