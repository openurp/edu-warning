package org.openurp.edu.warning.service.impl

import java.time.{Instant, LocalDate}

import org.beangle.cdi.Container
import org.beangle.cdi.ContainerAware
import org.beangle.commons.collection.Collections
import org.beangle.data.dao.EntityDao
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.dao.impl.BaseServiceImpl
import org.openurp.edu.base.model.{Project, Semester, Student}
import org.openurp.edu.warning.model.GradeWarning
import org.openurp.edu.warning.model.StatMethod
import org.openurp.edu.warning.model.WarningType
import org.openurp.edu.warning.service.GradeWarningService
import org.openurp.edu.warning.service.UnpassedCreditsStatService

class GradeWarningStat extends BaseServiceImpl with GradeWarningService with ContainerAware {

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
      var methodBean = container.getBeans(classOf[UnpassedCreditsStatService])(method.serviceName)
      val credits = methodBean.stat(std, semester)
      ruleMap(method).foreach(statRule => {
        if (credits >= statRule.minValue && credits <= statRule.maxValue) {
          typeName = statRule.warningType.name
          typeList += statRule.warningType
        }
      })
      detailString.append(brString)
      detailString.append(method.name).append(" ").append(credits).append(" ").append(typeName)
      brString = ";"
    })
    val a = typeList.sortBy(f => f.level).reverse
    gradeWarning.semester = semester
    gradeWarning.std = std
    gradeWarning.project = std.project
    gradeWarning.updatedAt = Instant.now()
    gradeWarning.warningType = typeList.sortBy(f => f.level).reverse.head
    gradeWarning.detail = detailString.toString()
    entityDao.saveOrUpdate(gradeWarning)
  }

}