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
package org.openurp.edu.warning.web.action

import java.time.Instant

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.edu.model.{Course, Semester}
import org.openurp.boot.edu.helper.ProjectSupport
import org.openurp.edu.grade.course.model.CourseGrade
import org.openurp.edu.warning.model.CourseWarning

/**
 * 课程预警
 */
class CourseWarningAction extends RestfulAction[CourseWarning] with ProjectSupport {

  override def indexSetting(): Unit = {
    val semesterId = getInt("semester.id")
    val semester = {
      semesterId match {
        case None => getCurrentSemester
        case _ => entityDao.get(classOf[Semester], semesterId.get)
      }
    }
    put("currentSemester", semester)
    put("project",getProject)
    put("departments", getDeparts)
    super.indexSetting()
  }

  override def search(): View = {
    val semesterId = getInt("courseWarning.semester.id")
    val semester = {
      semesterId match {
        case None => getCurrentSemester
        case _ => entityDao.get(classOf[Semester], semesterId.get)
      }
    }
    put("semester", semester)
    super.search()
  }


  def stat(): View = {
    val semesterId = getInt("semester.id")
    val semester = {
      semesterId match {
        case None => getCurrentSemester
        case _ => entityDao.get(classOf[Semester], semesterId.get)
      }
    }
    get("count").foreach(count => {
      val builder = OqlBuilder.from(classOf[CourseWarning], "cw")
      builder.where("cw.semester=:semester", semester)
      val courseWarnings = entityDao.search(builder)
      entityDao.remove(courseWarnings)
      val query = OqlBuilder.from(classOf[CourseGrade].getName, "grade")
      query.where("grade.semester.beginOn <:beginOn", semester.beginOn)
      query.where(":beginOn between grade.std.state.beginOn and grade.std.state.endOn", semester.beginOn.plusDays(60))
      query.where("grade.passed=false")
      query.select("grade.course.id,count(distinct std.id)")
      query.groupBy("grade.course.id")
      query.having("count(distinct std.id) > " + count)
      val datas: Seq[Array[_]] = entityDao.search(query)
      datas.foreach(data => {
        val courseWarning = new CourseWarning
        courseWarning.updatedAt = Instant.now
        courseWarning.count = data(1).toString.toInt
        courseWarning.course = entityDao.get(classOf[Course], data(0).toString.toLong)
        courseWarning.semester = semester
        entityDao.saveOrUpdate(courseWarning)
      })
    })
    redirect("index")
  }

}
