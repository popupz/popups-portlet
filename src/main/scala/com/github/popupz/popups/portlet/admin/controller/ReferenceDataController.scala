/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.portlet.admin.controller

import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}
import org.springframework.web.portlet.bind.annotation.ResourceMapping
import javax.portlet.ResourceResponse
import com.liferay.portal.kernel.dao.orm._
import com.liferay.portal.kernel.util.PortalClassLoaderUtil
import com.liferay.portal.model.{Role, UserGroup, Organization}
import org.springframework.web.portlet.ModelAndView
import com.liferay.portal.service.OrganizationLocalService

@RequestMapping(Array("VIEW"))
class ReferenceDataController(organisationLocalService: OrganizationLocalService) {

  private val MaxNumberOfAutoCompleteResults = 10

  @ResourceMapping(value = "findOrganisations")
  def findOrganisations(@RequestParam q: String, resourceResponse: ResourceResponse) = {
    val dynamicQuery = DynamicQueryFactoryUtil.forClass(classOf[Organization], PortalClassLoaderUtil.getClassLoader)
      .add(RestrictionsFactoryUtil.ilike("name", q + "%"))
      .addOrder(OrderFactoryUtil.asc("name"))

    val organisations = organisationLocalService.dynamicQuery(dynamicQuery, 0, MaxNumberOfAutoCompleteResults)
    new ModelAndView("json")
      .addObject("results", organisations)
  }

  @ResourceMapping(value = "findUserGroups")
  def findUserGroups(@RequestParam q: String, resourceResponse: ResourceResponse) = {
    val dynamicQuery = DynamicQueryFactoryUtil.forClass(classOf[UserGroup], PortalClassLoaderUtil.getClassLoader)
      .add(RestrictionsFactoryUtil.ilike("name", q + "%"))
      .addOrder(OrderFactoryUtil.asc("name"))

    val userGroups = organisationLocalService.dynamicQuery(dynamicQuery, 0, MaxNumberOfAutoCompleteResults)
    new ModelAndView("json")
      .addObject("results", userGroups)
  }

  @ResourceMapping(value = "findRoles")
  def findRoles(@RequestParam q: String, @RequestParam roleType: Integer, resourceResponse: ResourceResponse) = {

    val dynamicQuery = DynamicQueryFactoryUtil.forClass(classOf[Role], PortalClassLoaderUtil.getClassLoader)
      .add(RestrictionsFactoryUtil.ilike("name", q + "%"))
      .add(RestrictionsFactoryUtil.eq("type", roleType))
      .addOrder(OrderFactoryUtil.asc("name"))

    val roles = organisationLocalService.dynamicQuery(dynamicQuery, 0, MaxNumberOfAutoCompleteResults)
    new ModelAndView("json")
      .addObject("results", roles)
  }


}
