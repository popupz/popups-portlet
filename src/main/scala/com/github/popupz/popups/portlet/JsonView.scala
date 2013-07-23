/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.portlet

import org.springframework.web.servlet.view.AbstractView
import java.util
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.liferay.portal.kernel.json.JSONFactoryUtil
import java.util.{List => JList}
import scala.collection.JavaConversions._

class JsonView extends AbstractView {

  setContentType("application/json")

  type ResultItem = {
    def getName: java.lang.String
    def getPrimaryKey: java.lang.Long
  }

  def renderMergedOutputModel(model: util.Map[String, AnyRef],
                              request: HttpServletRequest,
                              response: HttpServletResponse) {

    val elements = model.get("results").asInstanceOf[JList[ResultItem]]

    val results = JSONFactoryUtil.createJSONArray()

    val json = JSONFactoryUtil.createJSONObject()
    json.put("response", results)

    elements.foreach(element => {
      val listEntry = JSONFactoryUtil.createJSONObject()
      listEntry.put("key", element.getPrimaryKey)
      listEntry.put("name", element.getName)
      results.put(listEntry)
    })

    response.setContentType("application/json")
    val writer = response.getWriter
    writer.println(String.valueOf(json))
  }

}
