/**
 * Copyright (c) 2013 The original author or authors All rights reserved.
 */

package com.github.popupz.popups.service.impl;

import com.github.popupz.popups.model.Popup;
import com.github.popupz.popups.service.base.PopupServiceBaseImpl;
import com.github.popupz.popups.service.impl.ScalaPopupServiceImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the popup remote service.
 * <p/>
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.github.popupz.popups.service.PopupService} interface.
 * <p/>
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author The original author or authors
 * @see com.github.popupz.popups.service.base.PopupServiceBaseImpl
 * @see com.github.popupz.popups.service.PopupServiceUtil
 */
public class PopupServiceImpl extends PopupServiceBaseImpl {
    private ScalaPopupServiceImpl scalaService = new ScalaPopupServiceImpl(this);

    public Popup addPopup(String title, String content, int width, boolean mustConfirm, String displayType,  String rules, ServiceContext serviceContext) throws PortalException, SystemException {
        return scalaService.addPopup(title, content, width, mustConfirm, displayType, rules, serviceContext);
    }

    public Popup updatePopup(long popupId, String title, String content, int width, boolean mustConfirm, String displayType, String rules, ServiceContext serviceContext) throws PortalException, SystemException {
        return scalaService.updatePopup(popupId, title, content, width, mustConfirm, displayType, rules, serviceContext);
    }

    public void deletePopup(long popupId) throws PortalException, SystemException {
        scalaService.deletePopup(popupId);
    }
}