<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Analysis Result</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes, height=device-height">
    <meta charset="UTF-8">
    <style>
        .content-wrapper {
            font: normal normal normal 16px/1.2 monospace;
        }
        .channel-title {
            font-size: 1.5em;
            margin-left: 20px;
        }
        .result-channel-wrapper {
            display: flex;
            align-items: center;
            margin-left: 44px;
        }
        .video-wrapper {
            margin-left: 20px;
            margin-top: 10px;
        }
        .video-main-container {
            display: flex;
            align-items: center;
            gap: 20px;
        }
        .video-icon {
            width: 140px;
            height: 100px;
        }
        .video-views {
            font-size: 14px;
        }
        .company-wrapper {
            margin-left: 80px;
            display: flex;
            gap: 20px;
            margin-top: -20px;
        }
        .company-title {
            font-weight: bold;
        }
        .top-5 {
            margin-left: 40px;
        }
    </style>
</head>
<body>
<div class="content-wrapper">
    <h1>Результат</h1>
    <div class="result-channel-wrapper">
        <img class="channel-icon" src="${result.channel.thumbnailUrl}" alt="profile pic" style="width: 80px;height: 80px">
        <p class="channel-title">${result.channel.userName}</p>
    </div>
    <div class="top-5">
        <p>Top-5 компаний:</p>
        <%--@elvariable id="topCompanies" type="java.util.List"--%>
        <c:forEach items="${topCompanies}" var="company">
            <t:company companyLink="${company}"></t:company>
        </c:forEach>
    </div>
    <%--@elvariable id="result" type="ru.renett.models.result.ChannelVideosAds"--%>
    <h2>Видео и компании:</h2>
    <c:forEach var="video" items="${result.orderedVideos}">
        <t:video video="${video}" companies="${result.map.get(video)}"></t:video>
    </c:forEach>
    <%--@elvariable id="message" type="java.lang.String"--%>
    <c:if test="${not empty message}">
        <div class="message-wrapper">
            <br>
            <h3>${message}</h3>
            <br>
        </div>
    </c:if>
</div>
</body>
</html>
