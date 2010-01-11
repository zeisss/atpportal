<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="New Algebra" />
</jsp:include>

    <style>
        textarea.axiom {
            width:90%;
        }
    </style>
    <script>
            function add_new_axiom() {
                $("#axiom_listmodel").append(
                    "<div class=\"listentry\">" +
                    "<textarea name=\"axiom\" class=\"axiom\"></textarea>" +
                    "</div>"
                );
                return false;
            }
            $(document).ready(function() {
                $("#new_axiom").click(add_new_axiom);
                add_new_axiom();

            });
    </script>
    <div class="box" style="width:100%">
        <div class="title">New Algebra</div>
        
        <div class="content" style="padding:10px">
        
            <form method="POST" action="<c:url value='/algebra/new' />">
                <table>
                    <tr>
                        <td><label for="algebra_name">Name:</label></td>
                        <td><input type="text" name="algebra_name" /></td>
                    </tr>
                    <tr>
                        <td><label for="algebra_comment">Comment:</label></td>
                        <td><textarea cols="50" rows="8" name="algebra_comment"></textarea></td>
                    </tr>
                </table>
                
                <div style="margin-top:5px; margin-bottom:5px;">
                    You can add some new formulas below. These are becoming the axioms for the new algebra. If a formula
                    with exactly the same formula-text exists, this one is used, otherwise a new formla is created.
                    You can later add existing formulas as axioms for this algebra by going to the formulas tab and
                    selecting the formular. For admins there will be the option to &quot;add as axiom to formula&quot;.<br />
                    Leave the boxes empty if you don't need any new formulas.
                </div>
                
                <div class="list" style="width:100%">
                    <div class="title">Axioms</div>
                    <div class="buttonbar">
                        <button id="new_axiom">Add axiom</button>
                    </div>
                    <div class="listmodel" id="axiom_listmodel">
                    </div>
                </div>
                <div>
                    <input type="submit" value="Save" />
                </div>
            </form>
        </div>
    </div>
<jsp:include page="/views/layout/footer.jsp" />