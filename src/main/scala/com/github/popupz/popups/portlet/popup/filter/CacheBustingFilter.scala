package com.github.popupz.popups.portlet.popup.filter

import javax.portlet.filter.{FilterConfig, FilterChain, RenderFilter}
import javax.portlet.{RenderResponse, RenderRequest}
import com.liferay.portal.util.PortalUtil
import com.liferay.portal.kernel.servlet.HttpHeaders

class CacheBustingFilter extends RenderFilter {

  def init(config: FilterConfig) {
    // do nothing
  }

  def doFilter(request: RenderRequest, response: RenderResponse, chain: FilterChain) {

    // because com.liferay.portal.servlet.filters.cache.CacheFilter does not respect expiration-cache settings in portlet.xml
    // probably i should just make the popups call an ajax call because this is not great for mostly content-only sites

    val servletResonse = PortalUtil.getHttpServletResponse(response)
    servletResonse.setHeader(HttpHeaders.CACHE_CONTROL, HttpHeaders.PRAGMA_NO_CACHE_VALUE)

    chain.doFilter(request, response)
  }

  def destroy() {
    // do nothing
  }


}
