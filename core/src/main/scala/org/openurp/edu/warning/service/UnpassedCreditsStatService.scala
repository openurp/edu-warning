package org.openurp.edu.warning.service

import org.openurp.edu.base.model.{Semester, Student}
import org.openurp.edu.grade.course.domain.CourseGradeProvider
import org.openurp.edu.grade.course.model.CourseGrade

trait UnpassedCreditsStatService {

  def stat(std: Student, semester: Semester): Float

  def getUnPassedGrades(std: Student, semester: Semester): collection.Seq[CourseGrade]

}