<%@tag description="Youtuber Option Tag" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@attribute name="channel" required="true" type="ru.renett.models.Channel" %>
<%@attribute name="ref" required="true" type="java.lang.String" %>
<div class="option-wrapper">
    <img class="option-icon icon" src="${channel.thumbnailUrl}" alt="profile pic">
    <label class="form-check-label" for="tag-1">${channel.userName}</label>
    <input class=form-check" type="checkbox" id="tag-1" name="channelId" value="${channel.youtubeId}">
    <input type="hidden" name="ref" value="${ref}">
</div>
