    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="java.time.Year, java.util.Enumeration" %>
    <%
        // Cố gắng set year để EL ${year} có giá trị nếu trang được mở trực tiếp
        try {
            int y = Year.now().getValue();
            request.setAttribute("year", y);
        } catch (Throwable t) {
            request.setAttribute("debugError", t.toString());
        }
    %>
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <title>Debug ex6_1.jsp</title>
        <style> body{font-family: Arial, sans-serif; padding:20px;} pre{background:#f4f4f4;padding:10px;border-radius:6px;} a.button{display:inline-block;padding:10px 14px;background:#6a5acd;color:#fff;border-radius:6px;text-decoration:none;} </style>
    </head>
    <body>
        <h1>Debug: ex6_1.jsp</h1>
        <p><a class="button" href="index.html">Return</a></p>
        <h2>Giá trị ${year}</h2>
        <p><strong>${year}</strong></p>
        <pre>
    <%
        Enumeration<String> en = request.getAttributeNames();
        if (!en.hasMoreElements()) {
    %>
No request attributes.
    <%
        } else {
            while (en.hasMoreElements()) {
                String n = en.nextElement();
    %>
<%= n %> = <%= String.valueOf(request.getAttribute(n)) %>
    <%
            }
        }
    %>
        </pre>
        <h2>Context / Paths</h2>
        <p>Context path: <%= request.getContextPath() %></p>
        <p>Request URI: <%= request.getRequestURI() %></p>
        <h2>Debug errors (nếu có)</h2>
        <p><strong>${debugError}</strong></p>
        <hr>
    </body>
    </html>