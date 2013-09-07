<%@ include file="../init.jsp" %>

<script type="text/javascript">

    AUI().use('plugin-popup', 'cookie', function (A) {

        <c:choose>
            <c:when test="${themeDisplay.signedIn}">
                <c:set var="userFullName" value="${themeDisplay.user.fullName}"/>
            </c:when>
            <c:otherwise>
                <spring:message code="not-logged-in-user" var="userFullName"/>
            </c:otherwise>
        </c:choose>

        var model = {
            userFullName: '<popups:unicode-formatter message="${userFullName}" />',
            userId: ${themeDisplay.userId},
            groupId: ${themeDisplay.scopeGroupId},
            companyId: ${themeDisplay.companyId}
        };

        <c:forEach var="popup" items="${popups}">

            var ${renderResponse.namespace}dialog${popup.primaryKey} = new A.PopupDialog({
                mustConfirm : ${popup.mustConfirm},
                width: ${popup.width},
                title: '<popups:unicode-formatter message="${popup.title}" />',
                bodyContent: '<popups:unicode-formatter message="${popup.content}" />',
                model: model,
                parseTemplate: true,
                strings: {
                    close: '<spring:message code="close"/>',
                    confirm: '<spring:message code="confirm"/>'
                }
            }).render();

            <c:choose>
                <c:when test="${popup.mustConfirm}">
                    ${renderResponse.namespace}dialog${popup.primaryKey}.on('close', function () {
                        A.Cookie.set('popup_${popup.popupId}', '1', {
                            path: "/"
                            <c:if test="${popup.displayType eq 'once'}">
                                ,expires: new Date(new Date().getTime()+365*24*60*60*1000*10)
                            </c:if>
                        });
                    });
                </c:when>
                <c:otherwise>
                    A.Cookie.set('popup_${popup.popupId}', '1', {
                        path: "/"
                        <c:if test="${popup.displayType eq 'once'}">
                            ,expires: new Date(new Date().getTime()+365*24*60*60*1000*10)
                        </c:if>
                    });
                </c:otherwise>
            </c:choose>

        </c:forEach>
    });
</script>