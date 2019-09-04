package org.openurp.edu.warning.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.edu.base.model.Semester
import org.openurp.edu.boot.web.ProjectSupport
import org.openurp.edu.warning.model.{GradeWarning, WarningType}


class DepartSummaryAction extends RestfulAction[GradeWarning] with ProjectSupport {

	override def index(): View = {
		val semesterId = getInt("semester.id")
		val semester = {
			semesterId match {
				case None =>
					getCurrentSemester()
				case _ => entityDao.get(classOf[Semester], semesterId.get)
			}
		}
		put("currentSemester", semester)

		val builder = OqlBuilder.from(classOf[GradeWarning].getName, "gw")
		builder.where("gw.semester=:semester", semester)
		builder.select("gw.std.state.department.id,gw.warningType.id,count(*)")
		builder.groupBy("gw.std.state.department.id,gw.warningType.id")
		builder.orderBy("gw.std.state.department.id,gw.warningType.id")
		val datas: Seq[Array[_]] = entityDao.search(builder)
		val newDatas = datas.groupBy(_ (0)).map { e =>
			(e._1, e._2.map(f => (f(1), f(2))).toMap)
		}
		val departs = entityDao.find(classOf[Department], newDatas.keys.asInstanceOf[Iterable[Int]])
		put("datas", newDatas)
		put("departs", departs)
		put("GREENID", WarningType.green)
		put("REDID", WarningType.red)
		put("YELLOWID", WarningType.yellow)
		forward()
	}

}
