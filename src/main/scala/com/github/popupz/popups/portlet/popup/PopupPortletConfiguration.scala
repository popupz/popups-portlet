/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.portlet.popup

import org.springframework.context.annotation.{Bean, Configuration }
import org.springframework.web.servlet.view.{JstlView, InternalResourceViewResolver}
import org.springframework.context.support.ResourceBundleMessageSource
import com.github.popupz.popups.portlet.popup.controller.PopupController
import org.springframework.web.portlet.mvc.annotation.AnnotationMethodHandlerAdapter
import com.github.popupz.popups.portlet.ThemeDisplayArgumentResolver
import com.github.popupz.popups.service.PopupLocalServiceUtil
import com.liferay.portal.service._

@Configuration
class PopupPortletConfiguration {

  @Bean
  def popupController = new PopupController(
    popupLocalService,
    organizationLocalService,
    userLocalService,
    groupLocalService,
    userGroupRoleLocalService
  )

  @Bean
  def internalResourceViewResolver = {
    val viewResolver = new InternalResourceViewResolver
    viewResolver.setContentType("text/html; charset=utf-8")
    viewResolver.setCache(false)
    viewResolver.setViewClass(classOf[JstlView])
    viewResolver.setPrefix("/WEB-INF/jsp/popup/")
    viewResolver.setSuffix(".jsp")
    viewResolver
  }

  @Bean
  def annotationMethodHandlerAdapter = {
    val handlerAdapter = new AnnotationMethodHandlerAdapter()
    handlerAdapter.setCustomArgumentResolver(new ThemeDisplayArgumentResolver)
    handlerAdapter
  }

  @Bean
  def messageSource = {
    val messageSource = new ResourceBundleMessageSource
    messageSource.setBasename("content.Language")
    messageSource
  }

  @Bean
  def popupLocalService = PopupLocalServiceUtil.getService

  @Bean
  def userLocalService = UserLocalServiceUtil.getService

  @Bean
  def groupLocalService = GroupLocalServiceUtil.getService

  @Bean
  def organizationLocalService = OrganizationLocalServiceUtil.getService

  @Bean
  def userGroupRoleLocalService = UserGroupRoleLocalServiceUtil.getService
}
