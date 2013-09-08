/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.portlet.admin.controller

import org.springframework.web.bind.annotation.{RequestParam, ModelAttribute, RequestMapping}
import org.springframework.web.portlet.bind.annotation.{ActionMapping, RenderMapping}
import javax.portlet.{ActionResponse, ActionRequest}
import com.liferay.portal.kernel.servlet.SessionMessages
import com.github.popupz.popups.service.{PopupLocalService, PopupService}
import org.springframework.web.portlet.ModelAndView
import com.liferay.portal.service._
import com.github.popupz.popups.model.impl.PopupImpl
import com.github.popupz.popups.model.Popup
import com.liferay.portal.theme.ThemeDisplay
import org.json.{JSONArray, JSONObject}
import annotation.tailrec
import scala.collection.JavaConverters._

@RequestMapping(Array("VIEW"))
class PopupController (popupService: PopupService,
                       popupLocalService: PopupLocalService,
                       organizationLocalService: OrganizationLocalService,
                       roleLocalService: RoleLocalService,
                       userGroupLocalService: UserGroupLocalService) {

  @ModelAttribute("popup")
  def formBean(@RequestParam(defaultValue="0") popupId: Long) = {
    val popup = popupId match {
      case id if id > 0 => popupLocalService.getPopup(id)
      case _ => {
        val popup = new PopupImpl()
        popup.setDisplayType("once")
        popup.setWidth(650)
        popup.setNew(true)
        popup
      }
    }
    popup.setRules(updatedRulesForPopup(popup))
    popup
  }

  @RenderMapping(params = Array("view=popupForm"))
  def view(@RequestParam redirect: String, themeDisplay: ThemeDisplay) = {
    new ModelAndView("edit-popup")
      .addObject("redirect", redirect)
  }

  @ActionMapping(params = Array("action=savePopup"))
  def submit(request: ActionRequest, response: ActionResponse,
             serviceContext: ServiceContext,
             @RequestParam(defaultValue="0") popupId: Long,
             @RequestParam redirect: String,
             @ModelAttribute("popup") popup: Popup)  {

    SessionMessages.add(request, "request_processed")

    popupId match {
      case id if id > 0 => popupService.updatePopup(id, popup.getTitle, popup.getContent, popup.getWidth, popup.getMustConfirm, popup.getDisplayType, popup.getRules, serviceContext)
      case _ => popupService.addPopup(popup.getTitle, popup.getContent, popup.getWidth, popup.getMustConfirm, popup.getDisplayType, popup.getRules, serviceContext)
    }
    response.sendRedirect(redirect)
  }

  private def updatedRulesForPopup(popup:Popup) = popup.isNew match {
    case true => """{"condition-type":"any","rules":[{"rule-type":"on-public-pages"}]}"""
    case _ => {

      @tailrec
      def loop(rulesArray: JSONArray, index: Integer, collector: Seq[JSONObject]): JSONArray = {
        if (index == rulesArray.length()) {
          new JSONArray(collector.asJava)
        } else {
          val config = rulesArray.getJSONObject(index)

          val rule = config.get("rule-type") match {
            case "user-is-a-member-of-organisation" => updateMemberOfOrganisationRule(config)
            case "user-is-a-member-of-user-group" => updateIsMemberOfUsergroupRule(config)
            case "user-has-regular-role" => updateUserHasRegularRoleRule(config)
            case "user-has-site-role" => updateUserHasSiteRoleRule(config)
            case _ => config
          }

          loop(rulesArray, index + 1, collector :+ rule)
        }
      }
      val json = new JSONObject(popup.getRules)
      json.put("rules", loop(json.getJSONArray("rules"), 0, Seq()))
      json.toString
    }
  }

  private def updateMemberOfOrganisationRule(config: JSONObject) = getLong(config, "organizationId") match {
    case Some(id) => Option (organizationLocalService.fetchOrganization(id)) match {
      case Some(organization) => copy(config).put("organization", organization.getName)
      case _ => markAsInvalid(config)
    }
    case _ => config
  }

  private def updateIsMemberOfUsergroupRule(config: JSONObject) = getLong(config, "userGroupId") match {
    case Some(id) => Option (userGroupLocalService.fetchUserGroup(id)) match {
      case Some(userGroup) => copy(config).put("userGroup", userGroup.getName)
      case _ => markAsInvalid(config)
    }
    case _ => config
  }

  private def updateUserHasRegularRoleRule(config: JSONObject) = getLong(config, "regularRoleId") match {
    case Some(id) => Option (roleLocalService.fetchRole(id)) match {
      case Some(role) => copy(config).put("regularRole", role.getName)
      case _ => markAsInvalid(config)
    }
    case _ => config
  }

  private def updateUserHasSiteRoleRule(config: JSONObject) = getLong(config, "siteRoleId") match  {
    case Some(id) => Option (roleLocalService.fetchRole(id)) match {
      case Some(role) => copy(config).put("siteRole", role.getName)
      case _ => markAsInvalid(config)
    }
    case _ => config
  }

  private def getLong(jsonObject: JSONObject, key: String) = {
    val value = jsonObject.getString(key)
    try {
      Some(java.lang.Long.valueOf(value))
    } catch {
      case e: NumberFormatException => None
    }
  }

  private def copy(jsonObject: JSONObject) = new JSONObject(jsonObject, JSONObject.getNames(jsonObject))

  private def markAsInvalid(jsonObject: JSONObject) =  {
    val result = copy(jsonObject)
    result.put("invalid", "true")
    result
  }
}
