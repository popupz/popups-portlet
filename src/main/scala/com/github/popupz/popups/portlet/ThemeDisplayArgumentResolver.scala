/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.portlet

import org.springframework.web.bind.support.WebArgumentResolver
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest
import javax.portlet.PortletRequest
import com.liferay.portal.kernel.util.WebKeys
import com.liferay.portal.theme.ThemeDisplay

class ThemeDisplayArgumentResolver extends WebArgumentResolver {

  private val ClassOfThemeDisplay = classOf[ThemeDisplay]

  def resolveArgument(methodParameter: MethodParameter, webRequest: NativeWebRequest) = {
    methodParameter.getParameterType match {
      case ClassOfThemeDisplay => {
        val portletRequest = webRequest.getNativeRequest(classOf[PortletRequest])
        portletRequest.getAttribute(WebKeys.THEME_DISPLAY)
      }
      case _ => WebArgumentResolver.UNRESOLVED
    }
  }
}
