package org.openurp.edu.warning.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.warning.model.StatRule
import org.openurp.edu.warning.model.StatMethod
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.warning.model.WarningType

class StatRuleAction extends RestfulAction[StatRule] {

  override def editSetting(statRule: StatRule): Unit = {
    put("methods", entityDao.search(OqlBuilder.from(classOf[StatMethod], "method").where("method.enabled is true")))
    put("warningTypes", entityDao.getAll(classOf[WarningType]))
    super.editSetting(statRule)

  }
}