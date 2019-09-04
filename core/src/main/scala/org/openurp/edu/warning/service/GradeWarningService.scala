package org.openurp.edu.warning.service

import org.openurp.edu.base.model.Student
import org.openurp.edu.base.model.Semester

trait GradeWarningService {

  def autoStat(std: Student, semester: Semester): Unit

}