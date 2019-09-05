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
package org.openurp.edu.base.web

import java.time.LocalDate

import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.Entity
import org.beangle.security.Securities
import org.beangle.webmvc.api.action.ServletSupport
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.entity.action.EntityAction
import org.openurp.app.security.{Profile, ProfileService}
import org.openurp.base.model.Department
import org.openurp.code.Code
import org.openurp.edu.base.model.{Project, Semester, Student, Teacher}

trait ProjectSupport extends ServletSupport {
  this: EntityAction[_] =>

  var profileService: ProfileService = _

  def getCodes[T](clazz: Class[T]): Seq[T] = {
    val query = OqlBuilder.from(clazz, "c")
    if (classOf[Code].isAssignableFrom(clazz)) {
      query.where("c.endOn is null or :now between c.beginOn and c.endOn", LocalDate.now)
    }
    query.cacheable()
    entityDao.search(query)
  }

  def findInSchool[T <: Entity[_]](clazz: Class[T]): Seq[T] = {
    val query = OqlBuilder.from(clazz, "aa")
    query.where("aa.school=:school", getProject.school)
    query.orderBy("code")
    entityDao.search(query)
  }

  def findInProject[T <: Entity[_]](clazz: Class[T], orderBy: String = "code"): Seq[T] = {
    val query = OqlBuilder.from(clazz, "aa")
    query.where("aa.project=:project", getProject)
    query.orderBy(orderBy)
    entityDao.search(query)
  }

  @ignore
  final def getProjectCode: String = {
    val p = getCookieValue("project")
    if (null == p) {
      request.getServerName
    } else {
      p
    }
  }

  @ignore
  final def getProject: Project = {
    val pcode = getProjectCode
    val builder = OqlBuilder.from(classOf[Project], "p").where("p.code=:code", pcode).cacheable()
    val projects = entityDao.search(builder)
    var project: Project = null
    if (projects.isEmpty) {
      val ps = entityDao.getAll(classOf[Project])
      if (ps.size == 1) {
        project = ps.head
      }
    } else {
      project = projects.head
    }

    if (null != project && pcode != project.code) {
      addCookie("project", project.code, -1)
    }
    project
  }

  def getProfile(projectId: Int): Option[Profile] = {
    profileService.get(Securities.user, "edu") find { profile =>
      profile.getProperty("projects") exists { pv =>
        pv == Profile.AllValue || Strings.contains("," + pv + ",", "," + projectId + ",")
      }
    }
  }

  def addDepart(query: OqlBuilder[_], departPath: String): Unit = {
    val project = getProject
    getProfileDepartIds(project) match {
      case None => query.where("1=2")
      case Some(d) =>
        if (d != Profile.AllValue) {
          val departIds = Strings.splitToInt(d)
          if (departPath.endsWith(".id")) {
            query.where(departPath + " in(:profile_depart_ids)", departIds)
          } else {
            query.where(departPath + ".id in(:profile_depart_ids)", departIds)
          }
        }
    }
  }

  private def getProfileDepartIds(project: Project): Option[String] = {
    getProfile(project.id) match {
      case None => None
      case Some(p) => p.getProperty("departments")
    }
  }

  def getDeparts: List[Department] = {
    val project = getProject
    getProfileDepartIds(project) match {
      case None => List.empty
      case Some(d) =>
        val departs =
          if (d == Profile.AllValue) {
            entityDao.getAll(classOf[Department])
          } else {
            entityDao.find(classOf[Department], Strings.splitToInt(d))
          }
        val pds = project.departments.toSet
        val now = LocalDate.now()
        val rs = departs.filter { d =>
          pds.contains(d) && (d.endOn.isEmpty || !d.endOn.get.isAfter(now))
        }
        rs.toList
    }
  }

  @ignore
  final def getStudent: Student = {
    val builder = OqlBuilder.from(classOf[Student], "s")
      .where("s.user.code=:code", Securities.user)
      .where("s.project=:project", getProject)
    val stds = entityDao.search(builder)
    if (stds.isEmpty) {
      throw new RuntimeException("Cannot find student with code " + Securities.user)
    } else {
      stds.head
    }
  }

  @ignore
  final def getTeacher: Teacher = {
    val builder = OqlBuilder.from(classOf[Teacher], "s")
      .where("s.user.code=:code", Securities.user)
      .where("s.project=:project", getProject)
    val teachers = entityDao.search(builder)
    if (teachers.isEmpty) {
      throw new RuntimeException("Cannot find teachers with code " + Securities.user)
    } else {
      teachers.head
    }
  }


  def getCurrentSemester: Semester = {
    val builder = OqlBuilder.from(classOf[Semester], "semester")
      .where("semester.calendar in(:calendars)", getProject.calendars)
    builder.where(":date between semester.beginOn and  semester.endOn", LocalDate.now)
    builder.cacheable()
    val rs = entityDao.search(builder)
    if (rs.isEmpty) { //如果没有正在其中的学期，则查找一个距离最近的
      val builder2 = OqlBuilder.from(classOf[Semester], "semester")
        .where("semester.calendar in(:calendars)", getProject.calendars)
      builder2.orderBy("abs(extract(day from(current_date()-semester.beginOn)))")
      builder2.cacheable()
      builder2.limit(1, 1)
      entityDao.search(builder2).headOption.orNull
    } else {
      rs.head
    }
  }
}
