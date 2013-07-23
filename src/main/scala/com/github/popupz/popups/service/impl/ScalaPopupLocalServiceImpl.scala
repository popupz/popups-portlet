/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.service.impl

import com.liferay.portal.kernel.exception.{SystemException, PortalException}
import com.liferay.portal.service.ServiceContext
import com.github.popupz.popups.model.Popup
import java.util.{List => JList, Date}
import scala.collection.JavaConversions._
import com.github.popupz.popups.PopupValidationException
import org.json.{JSONException, JSONObject}

class ScalaPopupLocalServiceImpl(popupLocalService: PopupLocalServiceImpl) {

  private def userPersistence = popupLocalService.getUserPersistence
  private def popupPersistence = popupLocalService.getPopupPersistence
  private def counterLocalService = popupLocalService.getCounterLocalService

  @throws(classOf[PortalException])
  @throws(classOf[SystemException])
  def addPopup(userId: Long, title: String, content: String, width: Int, mustConfirm: Boolean, displayType: String, rules: String, serviceContext:
               ServiceContext): Popup = {

    validateRules(rules)

    val now = new Date()

    val user = userPersistence.findByPrimaryKey(userId)

    val popupId = counterLocalService.increment()
    val popup = popupPersistence.create(popupId)

    popup.setTitle(title)
    popup.setContent(content)
    popup.setRules(rules)
    popup.setUserId(user.getUserId)
    popup.setUserName(user.getFullName)
    popup.setGroupId(serviceContext.getScopeGroupId)
    popup.setCompanyId(serviceContext.getCompanyId)
    popup.setCreateDate(serviceContext.getCreateDate(now))
    popup.setModifiedDate(serviceContext.getModifiedDate(now))
    popup.setWidth(width)
    popup.setMustConfirm(mustConfirm)
    popup.setDisplayType(displayType)

    popupPersistence.update(popup, false)
  }

  @throws(classOf[PortalException])
  @throws(classOf[SystemException])
  def updatePopup(userId: Long, popupId: Long, title: String, content: String, width: Int, mustConfirm: Boolean,
                  displayType: String, rules: String, serviceContext: ServiceContext): Popup = {

    validateRules(rules)

    val now = new Date()

    val user = userPersistence.findByPrimaryKey(userId)

    val popup = popupPersistence.findByPrimaryKey(popupId)

    popup.setUserId(user.getUserId)
    popup.setUserName(user.getFullName)
    popup.setModifiedDate(serviceContext.getModifiedDate(now))
    popup.setTitle(title)
    popup.setContent(content)
    popup.setRules(rules)
    popup.setWidth(width)
    popup.setMustConfirm(mustConfirm)
    popup.setDisplayType(displayType)


    popupPersistence.update(popup, false)
  }

  @throws(classOf[SystemException])
  def getPopupsByGroupId(groupId: Long): JList[Popup] = popupPersistence.findByGroupId(groupId)

  @throws(classOf[SystemException])
  def getGroupPopups(groupId: Long, start: Int, end: Int) = popupPersistence.findByGroupId(groupId, start, end)

  @throws(classOf[SystemException])
  def getGroupPopupsCount(groupId: Long) = popupPersistence.countByGroupId(groupId)

  @throws(classOf[PortalException])
  @throws(classOf[SystemException])
  def deleteGroupPopups(groupId: Long) { getPopupsByGroupId(groupId).foreach(popupPersistence.remove(_)) }


  private def validateRules(rules:String) {
    // having invalid json would break stuff and may pose a security issue if i decide to disallow active content in
    // popup messages
    try {
      new JSONObject(rules)
    } catch {
      case e:JSONException => throw new PopupValidationException(e)
    }
  }
}
