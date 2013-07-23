/**
 * Copyright 2013 The original author or authors
 */
package com.github.popupz.popups.portlet

import org.springframework.web.portlet.context.AbstractRefreshablePortletApplicationContext
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.context.annotation.{ClassPathBeanDefinitionScanner, AnnotatedBeanDefinitionReader}

class AnnotationConfigPortletApplicationContext extends AbstractRefreshablePortletApplicationContext {

  def loadBeanDefinitions(beanFactory: DefaultListableBeanFactory) {

    val reader = new AnnotatedBeanDefinitionReader(beanFactory)

    val scanner = new ClassPathBeanDefinitionScanner(beanFactory)

    val configLocationsOption = Option(getConfigLocations)

    configLocationsOption match {
      case Some(locations) => {
        locations.foreach { configLocation =>
          try {
            val clazz = getClassLoader.loadClass(configLocation)
            reader.register(clazz)
          } catch {
            case e: ClassNotFoundException => {
              logger.debug("Could not load class for config location [" + configLocation +
                "] - trying package scan. " + e)

              val count = scanner.scan(configLocation)
              count match {
                case 0 => logger.info("No annotated classes found for specified class/package [" + configLocation + "]")
                case _ => logger.info("Found " + count + " annotated classes in package [" + configLocation + "]")
              }
            }
          }
        }
      }
      case _ => logger.debug("No config location specified")
    }
  }
}
