<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="dump" uri="dump" %>

<jsp:include page="/views/layout/header.jsp">
 <jsp:param name="title" value="Fomula" />
</jsp:include>
 <style>
  #reference_body input {
   width:95%;
  }
  .selected {
   background-color:yellow;
  }
 </style>
 <script>
  var ref_cnt = 1;
  
  // Delete the current line in the reference model
  function reference_delete() {
   $(this).parent().parent().remove();
  }
  function reference_add(abbr, authors, title, year) {
   cnt = ref_cnt++;
   $("#reference_body").append("<tr><input type=hidden name=formula_reference value=\"" + cnt + "\" />" +
     "<td><input type=\"text\" class=ref_abbr name=\"formula_reference_" + cnt + "_abbr\" value=\"" + (abbr ? abbr : "") + "\" /></td>" +
     "<td><input type=\"text\" class=ref_authors name=\"formula_reference_" + cnt + "_authors\" value=\"" + (authors ? authors : "") + "\" /></td>" +
     "<td><input type=\"text\" class=ref_title name=\"formula_reference_" + cnt + "_title\" value=\"" + (title ? title : "") + "\" /></td>" +
     "<td><input type=\"text\" class=ref_year name=\"formula_reference_" + cnt + "_year\" value=\"" + (year && year > 0 ? year : "") + "\" /></td>" +
     "<td><button class=\"reference_delete\">Delete</button></td>" +
     "</tr>");
  }
  
  function executeSearch() {
    // Clear formula listmodel
    $(".formula").remove();
    
    // Fetch the new formulas
    formulaURL = "<c:url value='/search.json' />?algebra_id=" + encodeURIComponent($("#algebra_id").attr("value"));
    formulaURL += "&filter=" + encodeURIComponent($("#filter").attr("value"));
    formulaURL += "&axioms=" + ($("#axioms").attr("checked") ? "true" : "false");
    formulaURL += "&theorems=" + ($("#theorems").attr("checked") ? "true" : "false");
    
    $.getJSON(formulaURL, function(data) {
     $.each(data, function(i, item){
       // Add the new entry to the listmodel
       $("#formula_listmodel").append(
        "<div class=\"listentry formula " + (i % 2 == 0 ? "even" : "odd") + "\" id=\"formula-" + item.id + "\">" +
        " <span class=\"formula_name\">&nbsp;" + item.name + "</span>" +
        " <span class=\"formula_formula\">" + item.formulaText + "</span>" + 
        "</div>"
       );
     });
    });
    return false;
  }
  
  function formulaSelected() {
    
      id = $(this).attr("id").substring("formula-".length);
      
      url = "<c:url value='/formula/' />" + id + ".json";
      $.getJSON(url, function(data) {
        loadFormula(data); 
      }); 
    
  }
  
  // Saves the current form to the server
  function saveFormula() {
    callback = function(data, textStatus) {
      $("#formula_save").attr("disabled", "");
     
      if ( data.type != "OK" ) {
        // Whoops, error
        msg = "Error while saving:\n";
        for(x in data.messages) {
         msg += data.messages[x] + "\n";
        }
        alert(msg);
      } else {
       executeSearch();
      }
     
    };
    form = $("form").serialize();

    if ( $("#formula_id").attr("value") == "" || $("#formula_id").attr("value") == "-1")
     return;
    
    $("#formula_save").attr("disabled", "disabled");
    
    $.post("<c:url value='/formula/update' />", form, callback, "json"); 
    return false;
  }
  
  function loadFormula(formula)
  {
    // Main data
    $("#formula_id").attr("value", formula.id);
    $("#formula_name").attr("value", formula.name);
    
    text = $("#formula_text");
    text.text(formula.formulaText);
    text.attr("disabled", (formula.id == -1 ? "" : "disabled"));
    
    $("#formula_comment").text(formula.comment);
    
    // Detail data
    $("#reference_body tr").remove();
    for ( x in formula.references ) {
     reference_add(
      formula.references[x].abbreviation,
      formula.references[x].authors,
      formula.references[x].title,
      formula.references[x].year
     );
    }
    
    // Algebra connections
    $("#formula_axiom_algebras span").text("");
    $("#formula_theorem_algebras span").text("");
    
    if ( formula.axiom_algebras.length == 0 ) {
     $("#formula_axiom_algebras").css("display", "none");
    } else {
     $("#formula_axiom_algebras").css("display", "block");
     
     span = $("#formula_axiom_algebras span");
     for(x in formula.axiom_algebras) {
      span.append(formula.axiom_algebras[x]);
     }
    }
    
    if ( formula.theorem_algebras.length == 0 ) {
     $("#formula_theorem_algebras").css("display", "none");
    } else {
     $("#formula_theorem_algebras").css("display", "block");
     
     span = $("#formula_theorem_algebras span");
     for(x in formula.theorem_algebras) {
      span.append(formula.theorem_algebras[x]);
     }
    }
    
    // Proof    
    $(".proof_box").remove();
    
    box = $("#reference_box");
    for ( x in formula.proofs ) {
      proof = formula.proofs[x];
      
      box.after("<div style=\"clear:both;\"></div>");
      
      box.after("<div class=\"box proof_box\" style=\"width:98%\" id=\"proof-" + proof.id + "\">" +
         "<div class=\"buttonbar\">" +
         " <button class=\"proof_view\">View</button>" +
         " <button class=\"proof_export_pdf\">PDF</button>" + 
         " <button class=\"proof_export_mathhmal\">MathML</button>" +
         "</div>" +
         "<span class=\"title\">Prove from " + proof.time + "</span>" +
         "<div class=\"content\">" +
         "  <span class=\"proove_formula\">" + proof.goalFormula + "</span>" +
         "  <span class=\"proove_details\">Steps: " + proof.steps_count + "</span>" +
         "</div>" +
        "</div>");

    }
  }
   
   
  
  $(document).ready(function() {
   $("div.proof_box button.proof_view").live("click", function() {
     id = $(this).parent().parent().attr("id").substring("proof-".length);
     window.location = "<c:url value='/proof/' />" + id;
     return false;
   });
   $("div.proof_box button.proof_export_pdf").live("click", function() {
     id = $(this).parent().parent().attr("id").substring("proof-".length);
     window.location = "<c:url value='/proof/' />" + id + ".pdf";
     return false;
   });
   $("div.proof_box button.proof_export_mathml").live("click", function() {
     id = $(this).parent().parent().attr("id").substring("proof-".length);
     window.location = "<c:url value='/proof/' />" + id + ".mathml"
     return false;
   });
   
   
   $("#formula_delete").click(function() {
     window.location = "<c:url value='/formula/delete' />?formula_id=" + $("#formula_id").attr("value");
     return false;
   })
   $("#formula_export_pdf").click(function() {
     window.location = "<c:url value='/formula' />/" + $("#formula_id").attr("value") + ".pdf";
     return false;
   });
   $("#formula_export_mathml").click(function() {
     window.location = "<c:url value='/formula' />/" + $("#formula_id").attr("value") + ".mathml";
     return false;
   });
   $("#formula_proof").click(function() {
    window.location = "<c:url value='/queue/new' />?goalText=" + encodeURIComponent($("#formula_text").text());
    return false;
   });
   $("#formula_save").click(saveFormula);
   
   $("#reference_add").click(function() { reference_add(); return false; });
   $(".reference_delete").live("click", reference_delete);
   $(".algebra").click(function() {
     id = $(this).attr("id").substring("algebra-".length);
     $("#algebra_id").attr("value", id);
      
      // Mark the current algebra as selected
     $("div.algebra").removeClass("selected");
     $("#algebra-" + id).addClass("selected");
     
     return executeSearch();
   });
   $("#formula_search").click(executeSearch);
   $(".formula").live("click", formulaSelected);
   
   reference_add();
   reference_add();
   
   <c:if test="${formula != null}">
    loadFormula(<jsp:include page='json.jsp' />);
   </c:if>
   
  });
 </script>




 <%-- Algebra List --%>
 <div class="list" style="width:25%; height:200px;">
  <div class="title">Algebra</div>
  <div class="listmodel">
      <div class="listentry algebra even selected" id="algebra-all">
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
         <div class="listentry algebra ${rowStyle}" id="algebra-${algebra.id}">
             <span class="algebra_name"><dump:dump value="${algebra.name}" mode="html" /></span>
         </div>
     </c:forEach>
  </div>
 </div>
 
 <%-- Formula List --%>
 <div class="list" style="width:73%; height:200px">
   <div class="buttonbar">
     <input type="hidden" id="algebra_id" name="algebra_id" value="all" />
     
     <input type="checkbox" name="axioms" id="axioms" checked="checked" />
     <label for="axiom">Axiom</label>
     
     <input type="checkbox" name="theorems" id="theorems" />
     <label for="theorem">Theorem</label>
     
     <input style="width:100px" type="text" name="filter" id="filter" value="" />
     <button id="formula_search">Search</button>
   </div>
   <div class="title">Formulas</div>
   <div class="listmodel" id="formula_listmodel">
      <div class="listentry formula">Execute a search for results.</div>
      
   </div>
 </div>
           
 <div style="clear:both" ></div><!-- New row -->


 <form>
 <div id="formula_box" class="box" style="width:98%">
      <span class="title">Formula</span>
      <div class="buttonbar">
          <button id="formula_save">Save</button>
          <button id="formula_delete">Delete</button>
          <button id="formula_export_pdf">PDF</button>
          <button id="formula_export_mathml" disabled="disabled">MathML</button>
          <button id="formula_proof">Prove</button>
      </div>
      
      <div class="content">
          <input type="hidden" name="formula_id" id="formula_id" value="${formula.id}" />
          
          <label for="formula_name">Name:</label>
          <input type="text" id="formula_name" name="formula_name" value="${formula.name}"/><br />
          
          <label for="formula_text">Formula: </formula><br />
          <textarea name="formula_text" id="formula_text" ${formula == null ? "" : "disabled=\"disabled\"" } rows="5" style="width:95%;">${formula.formulaText}</textarea><br />
          
          <label for="formula_comment">Comment: </label><br />
          <textarea name="formula_comment" id="formula_comment" rows="5" style="width:95%; ">${formula.comment}</textarea><br />
          
          <div id="formula_axiom_algebras">
            <label>Axiom for algebras:</label>
            <span></span>
          </div>
          
          <div id="formula_theorem_algebras">
            <label>Theorems for algebras:</label>
            <span></span>
          </div>
          
      </div>
 </div>
 
           
 <%-- ------------------------------------------ --%>
 
 <div style="clear:both"></div><!-- New row -->
 
 <div id="reference_box" class="box" style="width:98%">
   <div class="buttonbar">
     <button id="reference_add">Add</button>
   </div>
   <span class="title">References</span>
   <div class="content">
     <table style="width:100%">
      <colgroup>
       <col width="100" />
       <col width="*" />
       <col width="*" />
       <col width="60" />
       <col width="40" />
      </colgroup>
       <thead>
       <tr>
         <th>Abbreviation</th>
         <th>Authors</th>
         <th>Title</th>
         <th colspan=2>Date</th>
       </tr>
       </thead>
       <tbody id="reference_body">
       
       </tbody>
     </table>
   </div>
 </div>

</form>
<jsp:include page="/views/layout/footer.jsp" />
