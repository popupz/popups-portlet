/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.portlet.admin.lar

import com.liferay.portal.kernel.lar.{PortletDataHandlerBoolean, PortletDataContext, BasePortletDataHandler}
import javax.portlet.PortletPreferences
import com.github.popupz.popups.service.{PopupLocalService, PopupLocalServiceUtil}
import scala.collection.JavaConversions._
import com.github.popupz.popups.model.Popup
import xml.XML
import com.github.popupz.popups.service.persistence.{PopupPersistence, PopupUtil}

class AdminPortletDataHandler(popupLocalService: PopupLocalService, popupPersistence: PopupPersistence) extends BasePortletDataHandler {

  private[this] val PortletKey = "popups-administration_WAR_popupsportlet"
  private[this] val NameSpace = "popup"

  def this() = this(PopupLocalServiceUtil.getService, PopupUtil.getPersistence)

  override def getExportControls = Array(new PortletDataHandlerBoolean(NameSpace, "popups", true, true))

  override def getImportControls = Array(new PortletDataHandlerBoolean(NameSpace, "popups", true, true))

  override def doDeleteData(context: PortletDataContext, portletId: String, preferences: PortletPreferences) = {
    if (!context.addPrimaryKey(classOf[AdminPortletDataHandler], "deleteData")) {
      popupLocalService.deleteGroupPopups(context.getScopeGroupId)
    }
    null
  }

  override def doExportData(context: PortletDataContext, portletId: String, preferences: PortletPreferences) = {

    val popupsToExport = popupLocalService.getPopupsByGroupId(context.getScopeGroupId)
                          .filter(popup => context.isWithinDateRange(popup.getModifiedDate))
                          .filter(popup => context.isPathNotProcessed(entryPath(context, popup)))


    popupsToExport.foreach(popup => context.addZipEntry(entryPath(context, popup), popup))

    val xml = <popup-data group-id={context.getScopeGroupId.toString}>
              {for (popup <- popupsToExport) yield
                <popup path={entryPath(context, popup)} />
              }
              </popup-data>

    xml.toString()
  }

  override def doImportData(context: PortletDataContext, portletId: String, preferences: PortletPreferences, data: String) = {
    val xml = XML.loadString(data)

    for (popup <- (xml \\ "popup")) {
      val path = (popup \ "@path").text

      val popupObject = context.getZipEntryAsObject(path).asInstanceOf[Popup]

      val serviceContext = context.createServiceContext(path, popupObject, NameSpace)
      val userId = context.getUserId(popupObject.getUserUuid)

      val importedPopup = context.isDataStrategyMirror match {
        case true => {
          Option(popupPersistence.fetchByUUID_G(popupObject.getUuid, context.getScopeGroupId)) match  {
            case Some(existingPopup) => {
              popupLocalService.updatePopup(userId, popupObject.getPopupId, popupObject.getTitle,
                popupObject.getContent, popupObject.getWidth, popupObject.getMustConfirm, popupObject.getDisplayType,
                popupObject.getRules, serviceContext)
            }
            case _ => {
              serviceContext.setUuid(popupObject.getUuid)

              popupLocalService.addPopup(userId, popupObject.getTitle, popupObject.getContent, popupObject.getWidth,
                popupObject.getMustConfirm, popupObject.getDisplayType, popupObject.getRules, serviceContext)
            }
          }
        }
        case _ => {
          popupLocalService.addPopup(userId, popupObject.getTitle, popupObject.getContent, popupObject.getWidth,
            popupObject.getMustConfirm, popupObject.getDisplayType, popupObject.getRules, serviceContext)
        }
      }
      context.importClassedModel(popupObject, importedPopup, NameSpace)
    }
    null
  }

  private def entryPath(context: PortletDataContext, popup: Popup) = s"$PortletKey/popups/${popup.getPopupId}.xml"

}
