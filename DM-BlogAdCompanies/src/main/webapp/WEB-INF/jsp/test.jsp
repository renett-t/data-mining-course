<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
  <title>Test Result</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes, height=device-height">
  <meta charset="UTF-8">
</head>
<body>
<h1>Результат</h1>

<h1>Видео:</h1>
<%--@elvariable id="companies" type="java.util.List"--%>
<c:forEach var="company" items="${companies}">
  <t:company companyLink="${company}"></t:company>
</c:forEach>
<%--@elvariable id="message" type="java.lang.String"--%>
<c:if test="${not empty message}">
  <div class="message-wrapper">
    <br>
    <h3>${message}</h3>
    <br>
  </div>
</c:if>
</body>
</html>