<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Main Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes, height=device-height">
    <meta charset="UTF-8">
    <style>
        .content-wrapper {
            font: normal normal normal 16px/1.2 monospace;
        }
        .option-wrapper {
            display: flex;
            gap: 20px;
            margin-left: 90px;
            align-items: center;
            margin-top: 20px;
            font-size: 1.3em;
        }
        .form {
            display: flex;
            gap: 20px;
            align-items: baseline;
        }
        .option-icon {
            width: 100px;
        }
        .options-wrapper .btn {
            margin-left: 200px;
            margin-top: 20px;
        }
        .btn {
            background-color: #fff;
            border: 1px solid #d5d9d9;
            border-radius: 8px;
            box-shadow: rgba(213, 217, 217, .5) 0 2px 5px 0;
            color: #0f1111;
            cursor: pointer;
            line-height: 29px;
            padding: 0 20px 0 20px;
            touch-action: manipulation;
        }

        .btn:hover {
            background-color: #f7fafa;
        }
        .btn:focus {
            border-color: #008296;
            box-shadow: rgba(213, 217, 217, .5) 0 2px 5px 0;
            outline: 0;
        }
    </style>
</head>
<body>
<div class="content-wrapper">
    <h1>Анализ рекламных партнёров ютубера</h1>
    <form class="form" action="${s:mvcUrl('MC#youtubersRequest').build()}" method="POST"><%--@elvariable id="searchParam" type="java.lang.String"--%>
        <label class="label-name" for="youtuberUserName">Youtube username:</label>
        <input class="input-name" name="youtuberUserName" id="youtuberUserName" type="text" value="${searchParam}" required>
        <br>
        <button class="btn btn-primary" type="submit">Search</button>
    </form>

    <%--@elvariable id="message" type="java.lang.String"--%>
    <c:if test="${not empty message}">
        <div class="message-wrapper">
            <br>
            <h3>${message}</h3>
            <br>
        </div>
    </c:if>

    <c:if test="${not empty youtuberOptions}">
        <div class="options-wrapper">
            <form action="${s:mvcUrl('MC#preAnalyzePage').build()}" method="POST">
                    <%--@elvariable id="youtuberOptions" type="java.util.List"--%>
                <c:forEach var="option" items="${youtuberOptions}">
                    <t:option channel="${option}" ref="${searchParam}"></t:option>
                </c:forEach>
                <button class="btn btn-primary" type="submit">Start Analysing!</button>
            </form>
        </div>
    </c:if>
</div>
</body>
</html>