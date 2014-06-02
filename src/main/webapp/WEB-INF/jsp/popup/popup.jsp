<%@ include file="../init.jsp" %>

<script type="text/javascript">
    (function () {
        AUI().use('plugin-popup', 'cookie', function (A) {

            function markCookieViewed(popupId, persistentCookie) {
                var attributes = { path: "/" };
                if (persistentCookie) {
                    attributes.expires = new Date(new Date().getTime() + 365 * 24 * 60 * 60 * 1000 * 10);
                }
                A.Cookie.set('popup_' + popupId, '1', attributes);
            }

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

            Popups.showPopup({
                mustConfirm: ${popup.mustConfirm},
                width: ${popup.width},
                title: '<popups:unicode-formatter message="${popup.title}" />',
                bodyContent: '<popups:unicode-formatter message="${popup.content}" />',
                model: model,
                parseTemplate: true,
                strings: {
                    close: '<spring:message code="close"/>',
                    confirm: '<spring:message code="confirm"/>'
                },
                onConfirm: function () {
                    markCookieViewed(${popup.popupId}, ${popup.displayType == 'once'});
                }
            });

            <c:if test="${not popup.mustConfirm}">
                markCookieViewed(${popup.popupId}, ${popup.displayType == 'once'});
            </c:if>

            </c:forEach>

        })
    })();
</script>
