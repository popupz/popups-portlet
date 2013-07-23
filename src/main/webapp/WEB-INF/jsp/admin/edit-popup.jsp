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
                    <liferay-ui:input-editor width="100%"/>

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
                        <aui:select name="${status.expression}" inlineField="true" label="">
                            <aui:option label="once" value="once" selected="${status.value eq 'once'}"/>
                            <aui:option label="every-visit" value="every-visit" selected="${status.value eq 'every-visit'}"/>
                        </aui:select>
                    </spring:bind>
                </c:set>

                <spring:bind path="rules" htmlEscape="true">
                    <input type="hidden" name="${status.expression}" value="${status.value}"/>
                </spring:bind>

                <c:set var="conditionType">
                    <aui:select name="condition-type" inlineField="true" label="">
                        <aui:option label="any" value="any"/>
                        <aui:option label="all" value="all"/>
                    </aui:select>
                </c:set>

                <spring:message code="display-if-x-of-the-following-conditions-are-met" arguments="${displayType},${conditionType}"/>

                <div class="rules-set">
                    <aui:fieldset id="rules-fieldset">
                    </aui:fieldset>
                </div>
            </aui:field-wrapper>


            <aui:button-row>
                <aui:button type="submit" inputCssClass="main"/>
                <aui:button inputCssClass="preview" value="preview"/>
                <aui:button href="${redirect}" value="cancel"/>
            </aui:button-row>
        </spring:nestedPath>
    </aui:fieldset>
</aui:form>


<c:set var="rulesDropdownColumn">

    <div class="column">
        <aui:select name="rule-type" inlineField="true" label="" inputCssClass="ruletype">
            <aui:option label="on-public-pages" value="on-public-pages"/>
            <aui:option label="on-private-pages" value="on-private-pages"/>
            <aui:option label="page-url" value="page-url"/>
            <aui:option label="user-is-logged-in" value="user-is-logged-in"/>
            <aui:option label="user-is-a-member-of-organisation" value="user-is-a-member-of-organisation"/>
            <aui:option label="user-is-a-member-of-user-group" value="user-is-a-member-of-user-group" />
            <aui:option label="user-has-regular-role" value="user-has-regular-role" />
            <aui:option label="user-has-site-role" value="user-has-site-role" />
            <%--
                <aui:option label="user-has-organization-role" value="user-has-organization-role" />
            --%>
        </aui:select>
    </div>
</c:set>

<c:set var="buttonsColumn">
    <div class="column last">
        <liferay-ui:icon cssClass="repeatable-field-add" image="add"/>
        <liferay-ui:icon cssClass="repeatable-field-delete" image="delete"/>
    </div>
</c:set>

<script id="${renderResponse.namespace}on-public-pages-template" type="text/x-template">
    <div class="rule">
        ${rulesDropdownColumn}
        ${buttonsColumn}
    </div>
</script>

<script id="${renderResponse.namespace}on-private-pages-template" type="text/x-template">
    <div class="rule">
        ${rulesDropdownColumn}
        ${buttonsColumn}
    </div>
</script>

<script id="${renderResponse.namespace}page-url-template" type="text/x-template">
    <div class="rule">
        ${rulesDropdownColumn}

        <div class="column">
            <aui:select name="match-type" inlineField="true" label="" cssClass="rule2">
                <aui:option label="equals" value="equals"/>
                <aui:option label="starts-with" value="starts-with"/>
                <aui:option label="ends-with" value="ends-with"/>
                <aui:option label="matches-regular-expression" value="matches-regular-expression"/>
            </aui:select>
        </div>

        <div class="column main">
            <aui:input name="value" inlineField="true" label="" inputCssClass="expand" />
        </div>

        ${buttonsColumn}
    </div>
</script>

<script id="${renderResponse.namespace}user-is-logged-in-template" type="text/x-template">
    <div class="rule">
        ${rulesDropdownColumn}
        ${buttonsColumn}
    </div>
</script>

<script id="${renderResponse.namespace}user-is-a-member-of-organisation-template" type="text/x-template">
    <liferay-portlet:resourceURL id="findOrganisations" var="findOrganisationUrl"/>

    <div class="rule">
        ${rulesDropdownColumn}
        <div class="column main">
            <aui:input name="organization" inlineField="true" label="" inputCssClass="selectbox expand" dataUrl="${findOrganisationUrl}" />
            <aui:input name="organizationId" type="hidden"/>
        </div>
        ${buttonsColumn}
    </div>
</script>

<script id="${renderResponse.namespace}user-is-a-member-of-user-group-template" type="text/x-template">
    <liferay-portlet:resourceURL id="findUserGroups" var="findUserGroupsUrl"/>

    <div class="rule">
        ${rulesDropdownColumn}
        <div class="column main">
            <aui:input name="userGroup" inlineField="true" label="" inputCssClass="selectbox expand" dataUrl="${findUserGroupsUrl}" />
            <aui:input name="userGroupId" type="hidden"/>
        </div>
        ${buttonsColumn}
    </div>
</script>

<script id="${renderResponse.namespace}user-has-regular-role-template" type="text/x-template">
    <liferay-portlet:resourceURL id="findRoles" var="findRegularRolesUrl">
        <liferay-portlet:param name="roleType" value="1"/>
    </liferay-portlet:resourceURL>

    <div class="rule">
        ${rulesDropdownColumn}
        <div class="column main">
            <aui:input name="regularRole" inlineField="true" label="" inputCssClass="selectbox expand" dataUrl="${findRegularRolesUrl}" />
            <aui:input name="regularRoleId" type="hidden"/>
        </div>
        ${buttonsColumn}
    </div>
</script>

<script id="${renderResponse.namespace}user-has-site-role-template" type="text/x-template">
    <liferay-portlet:resourceURL id="findRoles" var="findSiteRolesUrl">
        <liferay-portlet:param name="roleType" value="2"/>
    </liferay-portlet:resourceURL>

    <div class="rule">
        ${rulesDropdownColumn}
        <div class="column main">
            <aui:input name="siteRole" inlineField="true" label="" inputCssClass="selectbox expand" dataUrl="${findSiteRolesUrl}" />
            <aui:input name="siteRoleId" type="hidden"/>
        </div>
        ${buttonsColumn}
    </div>
</script>

<script type="text/javascript">

    AUI().use('ac-selectbox', 'node', 'json-parse', 'json-stringify', 'plugin-popup', function (A) {

        var portletNamespace = '${renderResponse.namespace}';
        var popupForm = A.one('#${renderResponse.namespace}fm');
        var rulesContainer = A.one('#${renderResponse.namespace}rules-fieldset');

        var rulesSetJson = popupForm.one('input[name="rules"]').get('value');

        var rulesSet = A.JSON.parse(rulesSetJson);

        popupForm.one('select[name="${renderResponse.namespace}condition-type"]').set('value',rulesSet.conditionType);

        for (var i = 0; i < rulesSet.rules.length; i++) {
            var ruleData = rulesSet.rules[i];

            var rowTemplate = A.one('#${renderResponse.namespace}' + ruleData['rule-type'] + '-template').getContent();
            var rowNode = A.Node.create(rowTemplate);

            for (var fieldName in ruleData) {
                rowNode.one('*[name="${renderResponse.namespace}' + fieldName + '"]').set('value', ruleData[fieldName])
            }

            A.one('#${renderResponse.namespace}rules-fieldset').append(rowNode);
        }

        popupForm.one('input.preview').on('click', function (e) {
            e.preventDefault();

            new A.PopupDialog({
                width: popupForm.one('input[name="${renderResponse.namespace}width"]').get('value'),
                mustConfirm: popupForm.one('input[name="${renderResponse.namespace}mustConfirm"]').get('value') == 'true',
                title: popupForm.one('input[name="${renderResponse.namespace}title"]').get('value'),
                bodyContent: ${renderResponse.namespace}editor.getHTML(),
                strings: {
                    close: '<spring:message code="close"/>',
                    confirm: '<spring:message code="confirm"/>'
                }
            }).render();
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

            if (inputField.as) {
                return;
            }
            var inputFieldName = inputField.get('name');
            var hiddenInputFieldName = inputFieldName + "Id";

            inputField.plug(A.Plugin.ACSelection, {
                dataUrl: inputField.getAttribute('dataUrl')
            });

            // workaround for http://yuilibrary.com/projects/yui3/ticket/2531651
            if (YUI.version === '3.4.0') {
                inputField.as._afterListInputFocus();
            }

            inputField.as.on('selectionChange', function (e) {
                var hiddenInputField = A.one('input[name="' + hiddenInputFieldName + '"]');
                hiddenInputField.set('value', e.newVal);
            });
        }, '.selectbox');

        rulesContainer.delegate('change', function (e) {
            var inputField = e.currentTarget;

            var value = inputField.get('value');

            var currentRuleDiv = inputField.ancestor("div.rule");
            var rowTemplate = A.one('#${renderResponse.namespace}' + value + '-template').getContent();
            var rowNode = A.Node.create(rowTemplate);

            rowNode.one('.ruletype').set('value', value)

            currentRuleDiv.replace(rowNode);
        }, '.ruletype');

        popupForm.one('input.main').on('click', function (e) {
            e.preventDefault();

            var rulesList = [];
            rulesContainer.all('div.rule').each(function (ruleDiv) {
                var ruleData = {};
                var inputs = ruleDiv.all('input, select')
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

            popupForm.one('input[name="rules"]').set('value', A.JSON.stringify({
                conditionType: conditionType,
                rules: rulesList
            }));
            popupForm.one('input[name="${renderResponse.namespace}content"]').set('value', ${renderResponse.namespace}editor.getHTML());

            submitForm(popupForm);
        });

    });
</script>
