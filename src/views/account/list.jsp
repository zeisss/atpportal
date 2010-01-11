<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import='org.tptp.model.*' %>
<%@ taglib prefix="dump" uri="dump" %>

<jsp:include page="/views/layout/header.jsp" />
    <style>
        .account {
            position:relative;
            height:40px;
        }
        .account .account_info {
            margin:2px;
        }
        
        .account_display_name {
            font-weight:bold;
            margin:2px;
        }
        .account_login_name {
            margin-right:20px;
        }
        .account_email {
        
        }
        .account_actions {
            position:absolute;
            right:10px;
            top:5px;
        }
    </style>
    <script>
        function update_account_role(accountId, newRole) {
            id = accountId.substring("account-".length);
            
            window.location = "<c:url value='/account/update_role' />?account_id=" + id + "&role=" + newRole;
        }
        
        $(document).ready( function() {
            $(".button_role_locked").click(function() { update_account_role($(this).parent().parent().attr("id"), "locked"); });
            $(".button_role_user").click(function() { update_account_role($(this).parent().parent().attr("id"), "normal"); });
            $(".button_role_admin").click(function() { update_account_role($(this).parent().parent().attr("id"), "admin"); });
            $(".button_reset").click(function() {
                id = $(this).parent().parent().attr("id").substring("account-".length);
                window.location = "<c:url value='/account/reset_password' />?account_id=" + id;
            })
        });
    </script>
    
    <c:set var="adminLevel"><%=org.tptp.model.Account.ADMINISTRATOR %></c:set>
    <c:set var="lockedLevel"><%=org.tptp.model.Account.LOCKED %></c:set>
    
    <div class="list" style="width:97%">
        <div class="title">Accounts</div>
        <div class="listmodel">
            <c:forEach var="account" items="${accounts}" varStatus="rowCounter">
                <c:choose>
                  <c:when test="${rowCounter.count % 2 == 0}">
                    <c:set var="rowStyle" scope="page" value="odd"/>
                  </c:when>
                  <c:otherwise>
                    <c:set var="rowStyle" scope="page" value="even"/>
                  </c:otherwise>
                </c:choose>
                
                <c:choose>
                     <c:when test="${account.level == adminLevel}">
                        <c:set var="role" scope="page" value="admin" />
                    </c:when>
                    <c:when test="${account.level == lockedLevel}">
                        <c:set var="role" scope="page" value="locked" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="role" scope="page" value="normal" />
                    </c:otherwise>
                </c:choose>
                
                <div class="listentry account ${rowStyle}" id="account-${account.id}">
                    <div class="account_info">
                        <div>
                            <span class="account_display_name"><dump:dump value="${account.displayName}" mode="html" /></span>
                            <span class="account_role">(Role: ${role})</span>
                        </div>
                        <span class="account_login_name">Username: <dump:dump value="${account.loginName}" mode="html" /></span>
                        <span class="account_email">Email: <a href="mailto:<dump:dump value="${account.email}" mode="html" />"><dump:dump value="${account.email}" mode="html" /></a></span>
                    </div>
                    <div class="account_actions">
                        <button class="button_role_locked" ${role == 'locked' ? 'disabled="disabled"' : ''}>Lock user</button>
                        <button class="button_role_user" ${role == 'normal' ? 'disabled="disabled"' : ''}>Make normal user</button>
                        <button class="button_role_admin" ${role == 'admin' ? 'disabled="disabled"' : ''}>Make admin</button>
                        &nbsp;&nbsp;
                        <button class="button_reset">Reset password</button>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
<jsp:include page="/views/layout/footer.jsp" />      