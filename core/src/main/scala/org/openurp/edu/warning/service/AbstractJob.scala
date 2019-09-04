package org.openurp.edu.warning.service

import org.beangle.commons.logging.Logging
import org.beangle.data.hibernate.spring.SessionHolder
import org.beangle.data.hibernate.spring.SessionUtils
import org.hibernate.FlushMode
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.transaction.support.TransactionSynchronizationManager

abstract class AbstractJob extends Logging {

  var sessionFactory: SessionFactory = _

  def getSession(): Session = {
    val session = sessionFactory.openSession()
    session.setFlushMode(FlushMode.MANUAL)
    session
  }

  def execute(): Unit = {
    val session = getSession()
    TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))
    try {
      doExecute()
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      val sessionHolder = TransactionSynchronizationManager.unbindResource(sessionFactory).asInstanceOf[SessionHolder]

      logger.debug("Closing single Hibernate Session in OpenSessionInViewInterceptor")
      SessionUtils.closeSession(sessionHolder.session)
    }
  }

  protected def doExecute(): Unit

}