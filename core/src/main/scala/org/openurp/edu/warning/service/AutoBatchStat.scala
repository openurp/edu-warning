package org.openurp.edu.warning.service

import org.beangle.data.dao.EntityDao
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.base.model.Student
import org.openurp.edu.warning.model.GradeWarning
import org.openurp.edu.warning.service.impl.GradeWarningStat
import org.openurp.edu.base.model.Project
import org.openurp.edu.base.model.Semester
import java.time.Instant
import java.time.LocalDate
import java.util.Date

class AutoBatchStat extends AbstractJob {

  var entityDao: EntityDao = _

  var gradeWarningservice: GradeWarningService = _

  val bulkSize = 15

  protected def doExecute(): Unit = {
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
    val query = OqlBuilder.from(classOf[Student], "s")
    query.where("not exists(from " + classOf[GradeWarning].getName + " r where r.std=s and r.updatedAt > :updatedAt)", format.parse(LocalDate.now().toString()).toInstant())
    query.where("s.state.inschool=true")
    query.orderBy("s.user.code")
    query.limit(1, bulkSize)
    val stds = entityDao.search(query)
    val startAt = System.currentTimeMillis()
    logger.info("start auto gws ...")

    stds.foreach(std => {
      gradeWarningservice.autoStat(std, getCurrentSemester(std.project))
    })

    if (stds.size > 0) {
      logger.info("auto gws: " + stds(0).user.code + "~" + stds(stds.size - 1).user.code + "["
        + stds.size + "] using " + (System.currentTimeMillis() - startAt) / 1000.0 + "s")
    } else {
      logger.info("auto gws: all gradeWarningStat is updated today!")
    }

  }

  def getCurrentSemester(project: Project): Semester = {
    val builder = OqlBuilder.from(classOf[Semester], "semester")
      .where("semester.calendar in(:calendars)", project.calendars)
    builder.where(":date between semester.beginOn and  semester.endOn", LocalDate.now)
    builder.cacheable()
    val rs = entityDao.search(builder)
    if (rs.isEmpty) {
      val builder2 = OqlBuilder.from(classOf[Semester], "semester")
        .where("semester.calendar in(:calendars)", project.calendars)
      builder2.orderBy("abs(semester.beginOn - current_date() + semester.endOn - current_date())")
      builder2.cacheable()
      builder2.limit(1, 1)
      val rs2 = entityDao.search(builder2)
      if (rs2.nonEmpty) {
        rs2.head
      } else {
        null
      }
    } else {
      rs.head
    }
  }

}