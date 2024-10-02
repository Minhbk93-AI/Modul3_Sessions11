<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<%-- the a co method la GET -> doGet() --%>
<a href="<%=request.getContextPath()%>/user-servlet">Quản lý người dùng</a>
</body>
</html>