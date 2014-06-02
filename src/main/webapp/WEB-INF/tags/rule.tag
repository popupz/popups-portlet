<%@ tag  %>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<portlet:defineObjects />

<div class="rule">
    <div class="column rule-types">
        <select class="aui-field-input aui-field-input-select aui-field-input-menu ruletype" id="${renderResponse.namespace}rule-type" name="${renderResponse.namespace}rule-type">
            <option value="on-public-pages"> <liferay-ui:message key="on-public-pages"/> </option>
            <option value="on-private-pages"> <liferay-ui:message key="on-private-pages"/> </option>
            <option value="page-url"> <liferay-ui:message key="page-url"/> </option>
            <option value="user-is-logged-in"> <liferay-ui:message key="user-is-logged-in"/> </option>
            <option value="user-is-a-member-of-organisation"> <liferay-ui:message key="user-is-a-member-of-organisation"/> </option>
            <option value="user-is-a-member-of-user-group"> <liferay-ui:message key="user-is-a-member-of-user-group"/> </option>
            <option value="user-has-regular-role"> <liferay-ui:message key="user-has-regular-role"/> </option>
            <option value="user-has-site-role"> <liferay-ui:message key="user-has-site-role"/> </option>
        </select>
    </div>
    <div class="column main">
        <jsp:doBody />
    </div>

    <div class="column rule-controls">
        <liferay-ui:icon cssClass="rule-error" image="../messages/alert" alt="invalid-rule" />
        <liferay-ui:icon cssClass="repeatable-field-add" image="add"/>
        <liferay-ui:icon cssClass="repeatable-field-delete" image="delete"/>
    </div>
</div>
