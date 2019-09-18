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
package org.openurp.edu.warning.service.impl

import java.time.Instant

import org.beangle.cdi.{Container, ContainerAware}
import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.dao.impl.BaseServiceImpl
import org.openurp.edu.base.model.{Semester, Student}
import org.openurp.edu.warning.model.{GradeWarning, StatMethod, WarningType}
import org.openurp.edu.warning.service.{GradeWarningService, UnpassedCreditsStatService}

class GradeWarningServiceImpl extends BaseServiceImpl with GradeWarningService with ContainerAware {

  var container: Container = _

  def autoStat(std: Student, semester: Semester): Unit = {
    val warningBuilder = OqlBuilder.from(classOf[GradeWarning], "gw")
    warningBuilder.where("gw.std=:std", std).where("gw.semester=:semester", semester)
    val gradeWarnings = entityDao.search(warningBuilder)

    val gradeWarning = if (gradeWarnings.isEmpty) new GradeWarning else gradeWarnings.head
    val methodQuery = OqlBuilder.from(classOf[StatMethod], "sm")
    methodQuery.where("sm.enabled=true")
    val methods = entityDao.search(methodQuery)
    val ruleMap = methods.map { x => (x, x.rules) }.toMap
    val typeList = Collections.newBuffer[WarningType]
    var typeName = ""
    val detailString = new StringBuilder
    var brString = ""
    methods.foreach(method => {
      val methodBean = container.getBeans(classOf[UnpassedCreditsStatService])(method.serviceName)
      val data = methodBean.stat(std, semester)
      var warned = false
      ruleMap(method).foreach(statRule => {
        if (data >= statRule.minValue && data <= statRule.maxValue) {
          if (statRule.warningType.level > 0) {
            warned = true
            typeName = statRule.warningType.name
            typeList += statRule.warningType
          }
        }
      })
      if (warned) {
        detailString.append(brString)
        detailString.append(method.name).append(" ").append(data)
        brString = ";"
      }
    })
    gradeWarning.semester = semester
    gradeWarning.std = std
    gradeWarning.project = std.project
    gradeWarning.updatedAt = Instant.now()
    if (typeList.isEmpty) {
      gradeWarning.warningType = new WarningType(WarningType.green)
    } else {
      gradeWarning.warningType = typeList.sortBy(f => f.level).reverse.head
    }
    gradeWarning.detail = detailString.toString()
    entityDao.saveOrUpdate(gradeWarning)
  }

}