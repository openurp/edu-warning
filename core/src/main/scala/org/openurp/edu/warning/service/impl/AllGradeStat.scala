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

import org.beangle.data.dao.OqlBuilder
import org.openurp.base.edu.model.{Semester, Student}
import org.openurp.edu.grade.BaseServiceImpl
import org.openurp.edu.grade.course.model.CourseGrade
import org.openurp.edu.grade.course.service.impl.BestGradeFilter
import org.openurp.edu.grade.model.Grade
import org.openurp.edu.warning.service.UnpassedCreditsStatService

abstract class AllGradeStat extends BaseServiceImpl with UnpassedCreditsStatService {
  var bestGradeFilter: BestGradeFilter = _

  def stat(std: Student, semester: Semester): Float

  def getUnPassed(std: Student, semester: Semester): Iterable[CourseGrade] = {
    val query = OqlBuilder.from(classOf[CourseGrade], "grade")
    query.where("grade.std = :std", std)
    query.where("grade.status =:status", Grade.Status.Published)
    query.where("grade.semester.beginOn <:beginOn", semester.beginOn)
    bestGradeFilter.filter(entityDao.search(query)).filter(!_.passed)
  }

}
