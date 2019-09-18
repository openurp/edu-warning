/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
    session.setHibernateFlushMode(FlushMode.MANUAL)
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