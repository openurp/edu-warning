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

import java.time.LocalDate

import org.beangle.data.dao.EntityDao
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.base.model.Project
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Student
import org.openurp.edu.grade.course.domain.CourseGradeProvider
import org.openurp.edu.warning.service.UnpassedCreditsStatService
import org.beangle.data.dao.impl.BaseServiceImpl
import org.openurp.edu.grade.course.model.CourseGrade
import org.openurp.edu.grade.model.Grade

class UnpassedCreditsStatBySemester extends BaseServiceImpl with UnpassedCreditsStatService {

	var courseGradeProvider: CourseGradeProvider = _

	/**
	 * 统计当前学期的前一个学期不通过学分总和
	 */

	def stat(std: Student, semester: Semester): Float = {
		val grades = getUnPassedGrades(std, semester)
		grades.map(grade => grade.course.credits).sum
	}

	def getUnPassedGrades(std: Student, semester: Semester): collection.Seq[CourseGrade] = {
		val query = OqlBuilder.from(classOf[CourseGrade], "grade")
		query.where("grade.std = :std", std)
		query.where("grade.status =:status", Grade.Status.Published)
		query.where("grade.semester =:semester", getLastSemester(semester))
		query.where("grade.passed is false")
		query.orderBy("grade.semester.beginOn")
		entityDao.search(query)
	}


	protected def getLastSemester(semester: Semester): Semester = {
		val builder = OqlBuilder.from(classOf[Semester], "semester")
		builder.where("semester.beginOn <:beginOn", semester.beginOn)
		builder.orderBy("semester.beginOn desc")
		builder.cacheable()
		val semesters = entityDao.search(builder)
		semesters.head
	}


}