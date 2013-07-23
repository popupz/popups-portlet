/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.portlet

import org.springframework.web.bind.support.WebArgumentResolver
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest
import javax.portlet.PortletRequest
import com.liferay.portal.service.{ServiceContextFactory, ServiceContext}

class ServiceContextArgumentResolver extends WebArgumentResolver {

  private val ClassOfServiceContext = classOf[ServiceContext]

  def resolveArgument(methodParameter: MethodParameter, webRequest: NativeWebRequest) = {
    methodParameter.getParameterType match {
      case ClassOfServiceContext => {
        val portletRequest = webRequest.getNativeRequest(classOf[PortletRequest])
        ServiceContextFactory.getInstance(portletRequest)
      }
      case _ => WebArgumentResolver.UNRESOLVED
    }
  }
}
