/**
 * Copyright (c) 2013 The original author or authors All rights reserved.
 */

package com.github.popupz.popups.service.impl;

import com.github.popupz.popups.model.Popup;
import com.github.popupz.popups.service.base.PopupLocalServiceBaseImpl;
import com.github.popupz.popups.service.impl.ScalaPopupLocalServiceImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ServiceContext;

import java.util.List;

/**
 * The implementation of the popup local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.github.popupz.popups.service.PopupLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author The original author or authors
 * @see com.github.popupz.popups.service.base.PopupLocalServiceBaseImpl
 * @see com.github.popupz.popups.service.PopupLocalServiceUtil
 */
public class PopupLocalServiceImpl extends PopupLocalServiceBaseImpl {

    private ScalaPopupLocalServiceImpl scalaPopupLocalService = new ScalaPopupLocalServiceImpl(this);

    public Popup addPopup(long userId, String title, String content, int width, boolean mustConfirm, String displayType, String rules, ServiceContext serviceContext) throws PortalException, SystemException {
        return scalaPopupLocalService.addPopup(userId, title, content, width, mustConfirm, displayType, rules, serviceContext);
    }

    public List<Popup> getGroupPopups(long groupId, int start, int end) throws SystemException {
        return scalaPopupLocalService.getGroupPopups(groupId, start, end);
    }

    public int getGroupPopupsCount(long groupId) throws SystemException {
        return scalaPopupLocalService.getGroupPopupsCount(groupId);
    }

    public Popup updatePopup(long userId, long popupId, String title, String content, int width, boolean mustConfirm, String displayType, String rules, ServiceContext serviceContext) throws PortalException, SystemException {
        return scalaPopupLocalService.updatePopup(userId, popupId, title, content,  width, mustConfirm, displayType, rules, serviceContext);
    }

    public List<Popup> getPopupsByGroupId(long groupId) throws SystemException {
        return scalaPopupLocalService.getPopupsByGroupId(groupId);
    }

    public void deleteGroupPopups(long groupId) throws SystemException, PortalException {
        scalaPopupLocalService.deleteGroupPopups(groupId);
    }

}