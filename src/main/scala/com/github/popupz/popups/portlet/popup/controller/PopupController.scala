/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.portlet.popup.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.portlet.bind.annotation.RenderMapping
import com.github.popupz.popups.service.PopupLocalService
import org.springframework.web.portlet.ModelAndView
import com.liferay.portal.theme.ThemeDisplay
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import javax.portlet.{PortletRequest, RenderRequest}
import com.github.popupz.popups.model.Popup
import annotation.tailrec
import org.json.{JSONObject, JSONArray}
import com.liferay.portal.kernel.util.WebKeys
import com.liferay.portal.service._
import com.liferay.portal.util.PortalUtil
import java.util.regex.PatternSyntaxException
import scala.Some

@RequestMapping(Array("VIEW"))
class PopupController(popupLocalService: PopupLocalService,
                      organizationLocalService: OrganizationLocalService,
                      userLocalService: UserLocalService,
                      groupLocalService: GroupLocalService,
                      userGroupRoleLocalService: UserGroupRoleLocalService) {

  @RenderMapping
  def view(renderRequest: RenderRequest, themeDisplay: ThemeDisplay) = {
    val popups = popupLocalService.getPopupsByGroupId(themeDisplay.getScopeGroupId)
                  .filter(rulesMatch(_)(renderRequest))
                  .filter(hasNotBeenViewed(_)(renderRequest))
                  .asJava

    new ModelAndView("popup")
      .addObject("popups", popups)
  }

  private def hasNotBeenViewed(popup: Popup)(renderRequest: RenderRequest) = {
    Option(renderRequest.getCookies) match {
      case Some(cookies) => !cookies.exists(_.getName == s"popup_${popup.getPopupId}")
      case _ => true
    }
  }

  private def rulesMatch(popup: Popup): (RenderRequest => Boolean) = {

    @tailrec
    def loop(rulesArray: JSONArray, index: Integer, collector: List[RenderRequest => Boolean]): List[RenderRequest => Boolean] = {
      if (index == rulesArray.length()) {
        collector
      } else {
        val config = rulesArray.getJSONObject(index)

        val fn = config.get("rule-type") match {
          case "page-url" => pageUrlMatches(config) _
          case "on-public-pages" => onPublicPages(config) _
          case "on-private-pages" => onPrivatePages(config) _
          case "user-is-logged-in" => signedIn(config) _
          case "user-is-a-member-of-organisation" => memberOfOrganisation(config) _
          case "user-is-a-member-of-user-group" => memberOfUserGroup(config) _
          case "user-has-regular-role" => hasRegularRole(config) _
          case "user-has-site-role" => hasSiteRole(config) _
          case _ => throw new IllegalArgumentException
        }
        loop(rulesArray, index + 1, fn :: collector)

      }
    }

    val json = new JSONObject(popup.getRules)
    val rulesArray = json.getJSONArray("rules")
    val conditionType = json.getString("condition-type")

    val ruleFns = loop(rulesArray, 0, List())

    conditionType match {
      case "any" => any(ruleFns)
      case "all" => all(ruleFns)
      case _ => throw new IllegalArgumentException
    }
  }

  private def getThemeDisplay(request: PortletRequest): ThemeDisplay = {
    request.getAttribute(WebKeys.THEME_DISPLAY).asInstanceOf[ThemeDisplay]
  }

  private def all(rules: List[RenderRequest => Boolean])(renderRequest: RenderRequest) = {
    rules.foldLeft(true) {(result, rule) =>
      result match {
        case false => false
        case _ => rule(renderRequest)
      }
    }
  }

  private def any(rules: List[RenderRequest => Boolean])(renderRequest: RenderRequest) = {
    rules.foldLeft(false) {(result, rule) =>
      result match {
        case true => true
        case false => rule(renderRequest)
      }
    }
  }

  private def pageUrlMatches(config:JSONObject)(renderRequest: RenderRequest): Boolean = {

    val url = PortalUtil.getCurrentURL(renderRequest)

    val matchType = config.getString("match-type")
    val value = config.getString("value")

    matchType match {
      case "equals" => url == value
      case "starts-with" => url.startsWith(value)
      case "ends-with" => url.endsWith(value)
      case "matches-regular-expression" => {
        try {
          value.matches(value)
        } catch {
          case e: PatternSyntaxException => false
        }
      }
      case _ => throw new IllegalArgumentException
    }
  }

  private def signedIn(config:JSONObject)(renderRequest: RenderRequest) = {
    val themeDisplay = getThemeDisplay(renderRequest)
    themeDisplay.isSignedIn
  }

  private def onPublicPages(config:JSONObject)(renderRequest: RenderRequest) = {
    val themeDisplay = getThemeDisplay(renderRequest)
    !themeDisplay.getLayoutSet.getPrivateLayout
  }

  private def onPrivatePages(config:JSONObject)(renderRequest: RenderRequest) = {
    val themeDisplay = getThemeDisplay(renderRequest)
    themeDisplay.getLayoutSet.getPrivateLayout
  }

  private def memberOfOrganisation(config:JSONObject)(renderRequest: RenderRequest): Boolean = {
    val organizationId = config.getLong("organizationId")
    val themeDisplay = getThemeDisplay(renderRequest)
    organizationLocalService.hasUserOrganization(themeDisplay.getUserId, organizationId)
  }

  private def hasRegularRole(config:JSONObject)(renderRequest: RenderRequest) = {
    val regularRoleId = config.getLong("regularRoleId")
    val themeDisplay = getThemeDisplay(renderRequest)
    userLocalService.hasRoleUser(regularRoleId, themeDisplay.getUserId)
  }

  def hasSiteRole(config:JSONObject)(renderRequest: RenderRequest) = {
    val siteRoleId = config.getLong("siteRoleId")
    val themeDisplay = getThemeDisplay(renderRequest)
    userGroupRoleLocalService.hasUserGroupRole(themeDisplay.getUserId, themeDisplay.getScopeGroupId, siteRoleId)
  }

  private def memberOfUserGroup(config:JSONObject)(renderRequest: RenderRequest): Boolean = {
    val userGroupId = config.getLong("userGroupId")
    val themeDisplay = getThemeDisplay(renderRequest)
    userLocalService.hasUserGroupUser(userGroupId, themeDisplay.getUserId)
  }
}
