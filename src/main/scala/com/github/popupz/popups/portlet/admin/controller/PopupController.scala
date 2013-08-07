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
import com.liferay.portal.service.ServiceContext
import com.github.popupz.popups.model.impl.PopupImpl
import com.github.popupz.popups.model.Popup
import com.liferay.portal.theme.ThemeDisplay

@RequestMapping(Array("VIEW"))
class PopupController (popupService: PopupService, popupLocalService: PopupLocalService) {

  @ModelAttribute("popup")
  def formBean(@RequestParam(defaultValue="0") popupId: Long) = {
    popupId match {
      case id if id > 0 => popupLocalService.getPopup(id)
      case _ => {
        val popup = new PopupImpl()
        popup.setRules("""{"condition-type":"any","rules":[{"rule-type":"on-public-pages"}]}""")
        popup.setDisplayType("once")
        popup.setWidth(650)
        popup.setNew(true)
        popup
      }
    }
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
}
