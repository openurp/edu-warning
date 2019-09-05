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
package org.openurp.edu.warning.service

import org.beangle.cdi.bind.BindModule
import org.openurp.edu.grade.course.service.impl.BestGradeFilter
import org.openurp.edu.grade.course.service.impl.CourseGradeProviderImpl
import org.openurp.edu.program.plan.impl.AlternativeCourseProviderImpl
import org.openurp.edu.warning.service.impl.GradeWarningStat
import org.openurp.edu.warning.service.impl.UnpassedCreditsStat
import org.openurp.edu.warning.service.impl.UnpassedCreditsStatBySemester

class ServiceModule extends BindModule {

  protected override def binding() {
    bind("unpassedCreditsStat", classOf[UnpassedCreditsStat])
    bind("unpassedCreditsStatBySemester", classOf[UnpassedCreditsStatBySemester])
    bind(classOf[GradeWarningStat])

    bind(classOf[StatJobStarter]).lazyInit(false)

    bind(classOf[AutoBatchStat])
    
    bind("courseGradeProvider", classOf[CourseGradeProviderImpl])
    
    bind("bestGradeFilter", classOf[BestGradeFilter])
    
    bind("alternativeCourseProvider", classOf[AlternativeCourseProviderImpl])
    
  }

}