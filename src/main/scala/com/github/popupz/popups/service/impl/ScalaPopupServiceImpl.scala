/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.service.impl

import com.liferay.portal.kernel.exception.{SystemException, PortalException}
import com.liferay.portal.service.ServiceContext
import com.liferay.portal.security.auth.PrincipalException

class ScalaPopupServiceImpl(popupService: PopupServiceImpl)  {

  private def popupLocalService = popupService.getPopupLocalService
  private def permissionChecker = popupService.getPermissionChecker

  @throws(classOf[PortalException])
  @throws(classOf[SystemException])
  def addPopup(title: String, content: String, width: Int, mustConfirm: Boolean, displayType: String, rules: String, serviceContext: ServiceContext) = {
    checkPermission(serviceContext.getScopeGroupId) {
      popupLocalService.addPopup(popupService.getUserId, title, content, width, mustConfirm, displayType, rules, serviceContext)
    }
  }

  @throws(classOf[PortalException])
  @throws(classOf[SystemException])
  def updatePopup(popupId: Long, title: String, content: String, width: Int, mustConfirm: Boolean, displayType: String, rules: String, serviceContext: ServiceContext) = {
    checkPermission(serviceContext.getScopeGroupId) {
      popupLocalService.updatePopup(popupService.getUserId, popupId, title, content, width, mustConfirm, displayType, rules, serviceContext)
    }
  }

  @throws(classOf[PortalException])
  @throws(classOf[SystemException])
  def deletePopup(popupId: Long) {
    val popup = popupLocalService.getPopup(popupId)

    checkPermission(popup.getGroupId) {
      popupLocalService.deletePopup(popupId)
    }
  }

  private def checkPermission [T](groupId: Long)(fn: => T): T = {
    permissionChecker.hasPermission(groupId, "com.github.popupz.popups.admin", groupId, "MANAGE_POPUPS") match {
      case true => fn
      case false => throw new PrincipalException()
    }
  }

}
