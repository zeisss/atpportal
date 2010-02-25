<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="dump" uri="dump" %>

<jsp:include page="/views/layout/header.jsp">
   <jsp:param name="title" value="New Queue" />
</jsp:include>
  
 <script>
  /**
   * Helper class for the formula and selected-formula lists.
   */ 
  function listmodel(htmlid, p_add_to_form) {
    obj = {
      "html_id": htmlid,
      "add_to_form": p_add_to_form,
      "formulas": [],
      "formulasHash": {}
    };
    
    /**
     *
     */
    obj.containsFormulaObject = function(jsonObject) {
     if ( this.formulasHash[jsonObject.id] ) return true;
     else return false;
    };
    
    /**
     *
     */
    obj.addFormulaObject = function(jsonObject) {
     this.formulas.push(jsonObject);
     this.formulasHash[jsonObject.id] = jsonObject;
     
     this.render();
    };
    
    /**
     *
     */
    obj.removeById = function(formula_id) {
     newFormulas = [];
     newFormulasHash = {};
     returnObject = false;
     
     $.each(this.formulas, function(i, item) {
      if ( item.id != formula_id) {
        newFormulas.push(item);
        newFormulasHash[item.id] = item;
      } else {
        returnObject = item;
      }
     });
     
     this.formulas = newFormulas;
     this.formulasHash = newFormulasHash;
     
     this.render();
     
     return returnObject;
    };
    
    /**
     *
     */
    obj.setFormulaObjectList = function(arrayList) {
      newFormulas = [];
      newFormulasHash = {};
      
      $.each(arrayList, function(i, item) {
       newFormulas.push(item);
       newFormulasHash[item.id] = item;
      });
      
      this.formulas = newFormulas;
      this.formulasHash = newFormulasHash;
     
      this.render();
    };
    
    obj.render = function() {
      listmodel = $("#" + this.html_id);
      add_to_form = this.add_to_form;
      
      $("#" + this.html_id + " div").remove(); // Clear the model
      $.each(this.formulas, function(i, item) {
        // Create the html snippet
        listmodel.append(
          "<div class=\"listentry formula " + (i % 2 == 0 ? "even" : "odd") + "\" id=\"formula-" + item.id + "\">" +
          (add_to_form ? "  <input type=hidden name=formula_id value=" + item.id + " />" : "" ) + 
          "  <span class=\"formula_name\">" + item.name + "&nbsp;</span>" +
          "  <span class=\"formula_formula\">" + item.formulaText + "&nbsp;</span>" + 
          "</div>"
        );
      });
    };
    return obj;
  }
  // The axioms for the currently selected algebra 
  var algebraAxioms = listmodel("algebra_axioms", false);
  
  // The selected axioms 
  var selectedAxioms = listmodel("selected_axioms", true);
  
  /**
   * Loads all formulas for the given algebra, filters out already selected formulas
   * and shows the rest in the left axiom list.
   */
  function loadAlgebraFormulas(algebra_id) {
    formulaURL = "<c:url value='/search.json' />?algebra_id=" + encodeURIComponent(algebra_id);
    formulaURL += "&filter=" + encodeURIComponent($("#filter").attr("value"));
    formulaURL += "&axioms=" + ($("#axioms").attr("checked") ? "true" : "false");
    formulaURL += "&theorems=" + ($("#theorems").attr("checked") ? "true" : "false");
    
     // url = "<c:url value='/search.json' />?algebra_id=" + algebra_id + "&axioms=true&theorems=true&filter=";
     
     $.getJSON(formulaURL, function(formulas) {
        f = [];
        for ( x in formulas ) {
          // If the formula is NOT selected
          if ( !selectedAxioms.containsFormulaObject(formulas[x]) ) {
            f.push(formulas[x]);
          }
        }
        // Now we have a filtered list
        algebraAxioms.setFormulaObjectList(f);
      }
     );
  }
  
  /**
   * Eventhandler when the user clicks an unselected axiom.
   */
  function left_axiom_clicked() {
    formula_id = $(this).attr("id").substring("formula-".length);
    
    object = algebraAxioms.removeById(formula_id);
    selectedAxioms.addFormulaObject(object);
  }
  
  function right_axiom_clicked() {
    formula_id = $(this).attr("id").substring("formula-".length);
    
    object = selectedAxioms.removeById(formula_id);
    algebraAxioms.addFormulaObject(object);
  }
  
  function filterAlgebras() {
      loadAlgebraFormulas($("#algebra").val());
      return false;
  }
  
  /**
   * Initialization code: Register the event handlers for the several buttons
   */
  $(document).ready(function() {
    // Left axiom table
    $("#algebra_axioms .formula").live("click", left_axiom_clicked);
    
    // Selected/right axiom tabl
    $("#selected_axioms .formula").live("click", right_axiom_clicked);
    
    $("button#formula_search").click(filterAlgebras);
    
    // Prevent pressing enter in the filter box
    $("#filter").keypress(function (e) {
      if ( e.which == 13 ) {
         return filterAlgebras();
      }
      return true;
      
    });
  
    // Algebra selection
    $("#algebra").change(filterAlgebras);
    
    loadAlgebraFormulas($("#algebra").val());
    
    <c:if test="${input != null}">
     <c:forEach var="f" items="${input}">
     object = algebraAxioms.removeById(${f.id});
     selectedAxioms.addFormulaObject({
      "id": ${f.id},
      "name": "${f.name}",
      "formulaText": "${f.formulaText}"
     });
     
     </c:forEach>
    </c:if>
    
    
  });
 </script>
 
 
    <form method="POST" actin="<c:url value='/queue/new' />">
           <div class="box" style="width:100%">
             <div class="title">Goal</div>
             <div class="content">
               <textarea name="goaltext" style="width:95%;"><dump:dump value="${formula}" mode="html" /></textarea>
             </div>
           </div>
           
           <div style="clear:both"></div>
           
           <div class="box" style="width:100%">
            
             <div class="title">Axioms</div>
             
             <div class="content" style="height:300px">
             
               <table style="width:100%; height:300px">
                <tr style="height:30px">
                 <td colspan="2">
                     <div>
                        <label>Algebra</label>
                        <select id="algebra">
                          <c:forEach var="algebra" items="${algebren}">
                            <option value="${algebra.id}">${algebra.name}</option>
                          </c:forEach>
                        </select>
                        <input type="checkbox" name="axioms" id="axioms" checked="checked" />
                        <label for="axiom">Axiom</label>
                        
                        <input type="checkbox" name="theorems" id="theorems" />
                        <label for="theorem">Theorem</label>
                        
                        <input style="width:100px" type="text" name="filter" id="filter" value="" />
                        <button id="formula_search">Search</button>
                     </div>
                  </td>
                </tr>
               
                <tr>
                  <td valign="top" style="width:48%; height:270px; overflow:auto">
                     <div class="list" style="width:100%; height:200px;">
                       <div class="title">Select some</div>
                      <div class="listmodel" id="algebra_axioms">
                       
                          <div class="listentry odd">
                              <span class="formula_name">Loading ...</span>
                          </div>
                          
                      </div>
                     </div>
                  </td>
                  <td valign="top" style="width:47%; height:270px; overflow:auto">
                     <div class="list" style="width:100%; height:200px">
                        <div class="title">Selected formulas</div>
                        <div class="listmodel" id="selected_axioms">
                           <div class="listentry even">
                               <span class="formula_name">(Select an axiom on the left by clicking on it)</span>
                           </div>
                        </div>
                      </div>
                  </td>
                </tr>
               </table>
               
             </div>
           </div>
           
           
           
           <div style="clear:both" ></div><!-- New row -->
        
           <div class="box" style="width:100%">
             <div class="buttonbar">
               <input type="submit" value="Prove / Execute!" />
             </div>
             <span class="title">Options</span>
             <div class="content">
                    <label>ATP:</label>
                    <select name="atp_id">
                      <c:forEach var="atp" items="${atps}">
                        <option value="${atp.id}"${job.atpId == atp.id ? ' selected="selected"' : ''}>${atp.name}</option>
                      </c:forEach>
                    </select>
                    
                    <%-- We only support prefix notation for now (this parameter is not used) --%>
                    <label>Notation:</label>
                    <select disabled="disabled" name="notation">
                     <option value="prefix">prefix notation</option>
                    </select>
                    
             </div>
           </div>
      </form>
    
    
<jsp:include page="/views/layout/footer.jsp" /> 