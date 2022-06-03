<%@tag description="Company" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@attribute name="companyLink" required="true" type="ru.renett.models.result.CompanyLink" %>

<div class="company-wrapper">
    <p class="company-title">${companyLink.company.title}</p>
    <p class="company-url">${companyLink.link.rawValue}</p>
</div>
