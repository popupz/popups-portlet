<%--@elvariable id="SEARCH_CONTAINER_RESULT_ROW" type="com.liferay.portal.kernel.dao.search.ResultRow"--%>
<%@ include file="../init.jsp" %>

<liferay-portlet:renderURL var="url" />
<c:set value="${SEARCH_CONTAINER_RESULT_ROW.object}" var="popup"/>

<script type="text/javascript">
    function ${renderResponse.namespace}showPreview${popup.primaryKey}() {
        AUI().use('plugin-popup', function (A) {
            Popups.showPopup({
                mustConfirm : ${popup.mustConfirm},
                width: ${popup.width},
                title: '<popups:unicode-formatter message="${popup.title}" />',
                bodyContent: '<popups:unicode-formatter message="${popup.content}" />',
                strings: {
                    close: '<spring:message code="close"/>',
                    confirm: '<spring:message code="confirm"/>'
                }
            });
        });
        return false;
    }
</script>

<liferay-ui:icon-menu>

    <liferay-ui:icon image="view" url="#" onClick="${renderResponse.namespace}showPreview${popup.primaryKey}()"  />

    <c:if test="${canManagePopups}">
        <liferay-portlet:renderURL var="editURL">
            <liferay-portlet:param name="view" value="popupForm" />
            <liferay-portlet:param name="redirect" value="${url}" />
            <liferay-portlet:param name="popupId" value="${popup.primaryKey}"/>
        </liferay-portlet:renderURL>

        <liferay-ui:icon image="edit" url="${editURL}" />

        <liferay-portlet:actionURL var="deleteURL">
            <liferay-portlet:param name="action" value="deletePopup"/>
            <liferay-portlet:param name="popupId" value="${popup.primaryKey}"/>
            <liferay-portlet:param name="redirect" value="${url}" />
        </liferay-portlet:actionURL>

        <liferay-ui:icon-delete url="${deleteURL}" />
    </c:if>

</liferay-ui:icon-menu>
