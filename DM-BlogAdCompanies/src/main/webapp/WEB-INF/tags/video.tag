<%@tag description="Youtube Video and Companies" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@attribute name="video" required="true" type="ru.renett.models.Video" %>
<%@attribute name="companies" required="true" type="java.util.List" %>
<div class="video-wrapper" data="${video.youtubeId}">
    <div class="video-main-container">
        <img class="video-icon icon" src="${video.thumbnailUrl}" alt="video pic" style="width: 100px;height: 80px">
        <p class="video-title">${video.title}</p>
    </div>
    <p class="video-views">${video.viewCount} просмотров</p>
    <c:forEach items="${companies}" var="company">
        <t:company companyLink="${company}"></t:company>
    </c:forEach>
</div>
