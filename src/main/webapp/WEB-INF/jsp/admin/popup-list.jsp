<%@ include file="../init.jsp" %>

<%--@elvariable id="searchContainer" type="com.liferay.portal.kernel.dao.search.SearchContainer"--%>

<liferay-portlet:renderURL var="url" />


<c:if test="${canManagePopups}">
    <liferay-portlet:renderURL var="addPopupUrl">
        <liferay-portlet:param name="view" value="popupForm" />
        <liferay-portlet:param name="redirect" value="${url}" />
    </liferay-portlet:renderURL>

    <aui:button-row cssClass="button-row">
        <aui:button onClick="${addPopupUrl}" value="add-popup" />
    </aui:button-row>
</c:if>

<liferay-ui:search-iterator searchContainer="${searchContainer}" />