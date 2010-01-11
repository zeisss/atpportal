<%@page isErrorPage="true" import="java.io.*" %>

<jsp:include page="/views/layout/header.jsp">
    <jsp:param name="title" value="Error" />
</jsp:include>
    <div class="box">
        <div class="title">An error occured</div>
        <div class="buttonbar">
            <form action="${new_url != null && new_url != '' ? new_url : ''}">
                <input type="submit" value="Continue" />
            </form>
        </div>
        <div class="content">
            <div>
                Press the &quot;Continue&quot; button to continue.
            </div>
            <div>
                <c:if test="${exception != null}">
                    Message: ${exception.message}<br />
                    <p>
                        <pre><code><%
                            Throwable t = (Throwable)request.getAttribute("exception");
                            t.printStackTrace(new PrintWriter(out));
                        %></code></pre>
                    </p>
                </c:if>
            </div>
        </div>
    </div>

<jsp:include page="/views/layout/footer.jsp" />