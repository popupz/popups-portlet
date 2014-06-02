<%@ include file="../init.jsp" %>

<%--@elvariable id="redirect" type="java.lang.String"--%>
<%--@elvariable id="popupId" type="java.lang.long"--%>

<liferay-portlet:actionURL var="actionUrl">
    <liferay-portlet:param name="action" value="savePopup"/>
    <liferay-portlet:param name="popupId" value="${popup.popupId}"/>
    <liferay-portlet:param name="redirect" value="${redirect}"/>
</liferay-portlet:actionURL>

<aui:form action="${actionUrl}" method="post" name="fm">

    <aui:fieldset>
        <spring:nestedPath path="popup">

            <spring:bind path="title" htmlEscape="true">
                <aui:input label="title" name="${status.expression}" value="${status.value}" size="150"/>
            </spring:bind>

            <spring:bind path="content">
                <script type="text/javascript">
                    function <portlet:namespace />initEditor() {
                        return "<popups:unicode-formatter message="${status.value}" />";
                    }
                </script>

                <aui:field-wrapper label="popup-message">
                    <liferay-ui:input-editor width="100%" toolbarSet="popups" />

                    <aui:input name="${status.expression}" type="hidden"/>
                </aui:field-wrapper>
            </spring:bind>

            <spring:bind path="mustConfirm" htmlEscape="true">
                <aui:input label="must-confirm" name="${status.expression}" type="checkbox" value="${status.value}"/>
            </spring:bind>

            <spring:bind path="width" htmlEscape="true">
                <aui:input label="width" name="${status.expression}" type="text" value="${status.value}" size="5"/>
            </spring:bind>

            <aui:field-wrapper label="popup-rules" cssClass="rules-field-wrapper">

                <c:set var="displayType">
                    <spring:bind path="displayType" htmlEscape="true">
                        <select name="${renderResponse.namespace}displayType" class="aui-field-input aui-field-input-select aui-field-input-menu">
                            <option value="once"
                                    <c:if test="${status.value eq 'once'}">
                                        selected
                                    </c:if>
                                    >
                                <liferay-ui:message key="once"/>
                            </option>
                            <option value="every-visit"
                                    <c:if test="${status.value eq 'every-visit'}">
                                        selected
                                    </c:if>
                                    >
                                <liferay-ui:message key="every-visit"/> </option>
                        </select>
                    </spring:bind>
                </c:set>

                <spring:bind path="rules" htmlEscape="true">
                    <input type="hidden" name="${renderResponse.namespace}${status.expression}" value="${status.value}"/>
                </spring:bind>

                <c:set var="conditionType">
                    <select name="${renderResponse.namespace}condition-type" class="aui-field-input aui-field-input-select aui-field-input-menu">
                        <option value="any"> <liferay-ui:message key="any"/> </option>
                        <option value="all"> <liferay-ui:message key="all"/> </option>
                    </select>
                </c:set>

                <spring:message code="display-if-x-of-the-following-conditions-are-met" arguments="${displayType},${conditionType}"/>

                <div id="${renderResponse.namespace}rules-set" class="rules-set">

                </div>

            </aui:field-wrapper>


            <aui:button-row>
                <aui:button type="submit" cssClass="submit-button"/>
                <aui:button cssClass="preview-button" value="preview"/>
                <aui:button href="${redirect}" value="cancel"/>
            </aui:button-row>
        </spring:nestedPath>
    </aui:fieldset>
</aui:form>


<script id="${renderResponse.namespace}on-public-pages-template" type="text/x-template">
    <popups:rule/>
</script>

<script id="${renderResponse.namespace}on-private-pages-template" type="text/x-template">
    <popups:rule/>
</script>

<script id="${renderResponse.namespace}page-url-template" type="text/x-template">
    <popups:rule>
        <div class="url-match-type">
            <select class="aui-field-input aui-field-input-select aui-field-input-menu" id="${renderResponse.namespace}match-type" name="${renderResponse.namespace}match-type">
                <option value="equals"> <liferay-ui:message key="equals"/> </option>
                <option value="starts-with"> <liferay-ui:message key="starts-with"/> </option>
                <option value="ends-with"> <liferay-ui:message key="ends-with"/> </option>
                <option value="matches-regular-expression"> <liferay-ui:message key="matches-regular-expression"/> </option>
            </select>
        </div>
        <div class="url-match-argument">
            <input name="${renderResponse.namespace}value" class="field expand" type="text" />
        </div>
    </popups:rule>
</script>

<script id="${renderResponse.namespace}user-is-logged-in-template" type="text/x-template">
    <popups:rule/>
</script>

<script id="${renderResponse.namespace}user-is-a-member-of-organisation-template" type="text/x-template">
    <liferay-portlet:resourceURL id="findOrganisations" var="findOrganisationUrl"/>

    <popups:rule>
        <input name="${renderResponse.namespace}organization" class="field expand selectbox" type="text" dataUrl="${findOrganisationUrl}" />
        <input name="${renderResponse.namespace}organizationId" type="hidden"/>
    </popups:rule>
</script>

<script id="${renderResponse.namespace}user-is-a-member-of-user-group-template" type="text/x-template">
    <liferay-portlet:resourceURL id="findUserGroups" var="findUserGroupsUrl"/>

    <popups:rule>
        <input name="${renderResponse.namespace}userGroup" class="field expand selectbox" type="text" dataUrl="${findUserGroupsUrl}" />
        <input name="${renderResponse.namespace}userGroupId" type="hidden"/>
    </popups:rule>
</script>

<script id="${renderResponse.namespace}user-has-regular-role-template" type="text/x-template">
    <liferay-portlet:resourceURL id="findRoles" var="findRegularRolesUrl">
        <liferay-portlet:param name="roleType" value="1"/>
    </liferay-portlet:resourceURL>

    <popups:rule>
        <input name="${renderResponse.namespace}regularRole" class="field expand selectbox" type="text" dataUrl="${findRegularRolesUrl}" />
        <input name="${renderResponse.namespace}regularRoleId" type="hidden"/>
    </popups:rule>
</script>

<script id="${renderResponse.namespace}user-has-site-role-template" type="text/x-template">
    <liferay-portlet:resourceURL id="findRoles" var="findSiteRolesUrl">
        <liferay-portlet:param name="roleType" value="2"/>
    </liferay-portlet:resourceURL>

    <popups:rule>
        <input name="${renderResponse.namespace}siteRole" class="field selectbox expand" type="text" dataUrl="${findSiteRolesUrl}" />
        <input name="${renderResponse.namespace}siteRoleId" type="hidden"/>
    </popups:rule>
</script>

<script type="text/javascript">

    AUI().use('ac-selectbox', 'plugin-popup', 'node', 'json-parse', 'json-stringify', function (A) {

        var portletNamespace = '${renderResponse.namespace}';
        var popupForm = A.one('#${renderResponse.namespace}fm');
        var rulesContainer = A.one('#${renderResponse.namespace}rules-set');

        var rulesSetJson = popupForm.one('input[name="${renderResponse.namespace}rules"]').get('value');

        var rulesSet = A.JSON.parse(rulesSetJson);


        popupForm.one('select[name="${renderResponse.namespace}condition-type"]').set('value',rulesSet['condition-type']);

        for (var i = 0; i < rulesSet.rules.length; i++) {
            var ruleData = rulesSet.rules[i];

            var rowTemplate = A.one('#${renderResponse.namespace}' + ruleData['rule-type'] + '-template').getContent();
            var rowNode = A.Node.create(rowTemplate);

            for (var fieldName in ruleData) {
                if (fieldName == 'invalid') {
                    rowNode.addClass('error');
                } else {
                    rowNode.one('*[name="${renderResponse.namespace}' + fieldName + '"]').set('value', ruleData[fieldName])
                }
            }

            A.one('#${renderResponse.namespace}rules-set').append(rowNode);
        }

        popupForm.one('.preview-button').on('click', function (e) {
            e.preventDefault();

            Popups.showPopup(
                    {
                        bodyContent: ${renderResponse.namespace}editor.getHTML(),
                        title: popupForm.one('input[name="${renderResponse.namespace}title"]').get('value'),
                        mustConfirm: popupForm.one('input[name="${renderResponse.namespace}mustConfirm"]').get('value') == 'true',
                        width: popupForm.one('input[name="${renderResponse.namespace}width"]').get('value'),
                        strings: {
                            close: '<spring:message code="close"/>',
                            confirm: '<spring:message code="confirm"/>'
                        }
                    }
            );

        });

        rulesContainer.delegate('click', function (e) {
            var clone = A.one('#${renderResponse.namespace}on-public-pages-template').getContent();
            var ruleDiv = e.currentTarget.ancestor("div.rule");
            ruleDiv.insert(clone, 'after');
        }, '.repeatable-field-add');

        rulesContainer.delegate('click', function (e) {
            var isLastRuleRemaining = rulesContainer.all('div.rule').size() == 1;

            if (isLastRuleRemaining) {
                return;
            }
            var ruleDiv = e.currentTarget.ancestor("div.rule");
            ruleDiv.get('parentNode').removeChild(ruleDiv);
        }, '.repeatable-field-delete');

        rulesContainer.delegate('focus', function (e) {
            var inputField = e.currentTarget;
            var ruleDiv = e.currentTarget.ancestor("div.rule");

            if (inputField.as) {
                return;
            }
            var inputFieldName = inputField.get('name');
            var hiddenInputFieldName = inputFieldName + "Id";

            var hiddenInputField = A.one('input[name="' + hiddenInputFieldName + '"]');

            inputField.plug(A.Plugin.ACSelection, {
                requestTemplate: '&${renderResponse.namespace}q={query}',
                dataUrl: inputField.getAttribute('dataUrl'),
                selection: hiddenInputField.get('value')
            });

            // workaround for http://yuilibrary.com/projects/yui3/ticket/2531651
            if (AUI.version === '3.4.0') {
                inputField.as._afterListInputFocus();
            }

            inputField.as.on('selectionChange', function (e) {
                hiddenInputField.set('value', e.newVal);
                ruleDiv.removeClass('error');
            });
        }, '.selectbox');

        rulesContainer.delegate('change', function (e) {
            var inputField = e.currentTarget;

            var value = inputField.get('value');

            var currentRuleDiv = inputField.ancestor("div.rule");
            var rowTemplate = A.one('#${renderResponse.namespace}' + value + '-template').getContent();
            var rowNode = A.Node.create(rowTemplate);

            rowNode.one('.ruletype').set('value', value);

            currentRuleDiv.replace(rowNode);
        }, '.ruletype');

        popupForm.one('.submit-button').on('click', function (e) {
            e.preventDefault();

            var rulesList = [];
            rulesContainer.all('div.rule').each(function (ruleDiv) {
                var ruleData = {};
                var inputs = ruleDiv.all('input, select');
                inputs.each(function (input) {
                    var name = input.get('name');
                    if (name.indexOf(portletNamespace) == 0) {
                        name = name.substring(portletNamespace.length, name.length);
                    }
                    ruleData[name] = input.get('value');
                });
                rulesList.push(ruleData);
            });

            var conditionType = popupForm.one('select[name="${renderResponse.namespace}condition-type"]').get('value');

            popupForm.one('input[name="${renderResponse.namespace}rules"]').set('value', A.JSON.stringify({
                'condition-type': conditionType,
                'rules': rulesList
            }));
            popupForm.one('input[name="${renderResponse.namespace}content"]').set('value', ${renderResponse.namespace}editor.getHTML());

            submitForm(popupForm);
        });

    });
</script>
