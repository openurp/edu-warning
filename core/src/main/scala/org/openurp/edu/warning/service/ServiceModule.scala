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