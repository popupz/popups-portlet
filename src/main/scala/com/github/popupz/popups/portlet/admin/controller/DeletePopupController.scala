/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.portlet.admin.controller

import com.github.popupz.popups.service.PopupService
import org.springframework.web.portlet.bind.annotation.ActionMapping
import javax.portlet.{ActionResponse, ActionRequest}
import org.springframework.web.bind.annotation.{RequestMapping, RequestParam}
import com.liferay.portal.kernel.servlet.SessionMessages

@RequestMapping(Array("VIEW"))
class DeletePopupController (popupService: PopupService) {

  @ActionMapping(params = Array("action=deletePopup"))
  def delete(request: ActionRequest, response: ActionResponse,
             @RequestParam redirect: String,
             @RequestParam popupId: Long)  {

    SessionMessages.add(request, "request_processed")

    popupService.deletePopup(popupId)

    response.sendRedirect(redirect)

  }

}
