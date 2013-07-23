/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.portlet.admin

import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.servlet.view.{BeanNameViewResolver, JstlView, InternalResourceViewResolver}
import org.springframework.context.support.ResourceBundleMessageSource
import controller.{ReferenceDataController, DeletePopupController, PopupController, PopupListController}
import com.github.popupz.popups.service.{PopupLocalServiceUtil, PopupServiceUtil}
import org.springframework.web.portlet.mvc.annotation.AnnotationMethodHandlerAdapter
import com.github.popupz.popups.portlet.{JsonView, ServiceContextArgumentResolver, ThemeDisplayArgumentResolver}
import com.liferay.portal.service.OrganizationLocalServiceUtil

@Configuration
class AdminPortletConfiguration {

  @Bean
  def beanNameViewResolver = {
    val viewResolver = new BeanNameViewResolver
    viewResolver.setOrder(1)
    viewResolver
  }

  @Bean
  def internalResourceViewResolver = {
    val viewResolver = new InternalResourceViewResolver
    viewResolver.setContentType("text/html; charset=utf-8")
    viewResolver.setCache(false)
    viewResolver.setViewClass(classOf[JstlView])
    viewResolver.setPrefix("/WEB-INF/jsp/admin/")
    viewResolver.setSuffix(".jsp")
    viewResolver.setOrder(2)
    viewResolver
  }

  @Bean
  def messageSource = {
    val messageSource = new ResourceBundleMessageSource
    messageSource.setBasename("content.Language")
    messageSource
  }

  @Bean
  def annotationMethodHandlerAdapter = {
    val handlerAdapter = new AnnotationMethodHandlerAdapter()
    handlerAdapter.setCustomArgumentResolvers(
      Array(new ServiceContextArgumentResolver, new ThemeDisplayArgumentResolver)
    )
    handlerAdapter
  }

  @Bean
  def json = new JsonView

  @Bean
  def popupController = new PopupController(popupService, popupLocalService)

  @Bean
  def popupListController = new PopupListController(popupLocalService)

  @Bean
  def deletePopupController = new DeletePopupController(popupService)

  @Bean
  def referenceDataController = new ReferenceDataController(organisationLocalService)

  @Bean
  def popupService = PopupServiceUtil.getService

  @Bean
  def popupLocalService = PopupLocalServiceUtil.getService

  @Bean
  def organisationLocalService = OrganizationLocalServiceUtil.getService

}
