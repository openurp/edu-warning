package org.openurp.edu.warning.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.edu.base.model.{Semester, Squad}
import org.openurp.edu.boot.web.ProjectSupport
import org.openurp.edu.warning.model.{GradeWarning, WarningType}

import scala.math.Numeric.LongIsIntegral

class SquadSummaryAction extends RestfulAction[GradeWarning] with ProjectSupport {

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

		val departId = getInt("departId").get
		val builder = OqlBuilder.from(classOf[GradeWarning].getName, "gw")
		builder.where("gw.semester=:semester", semester)
		builder.where("gw.std.state.department.id=:id", departId)
		builder.select("gw.std.state.squad.id,gw.warningType.id,count(*)")
		builder.groupBy("gw.std.state.squad.id,gw.warningType.id")
		builder.orderBy("gw.std.state.squad.id,gw.warningType.id")
		val datas: Seq[Array[_]] = entityDao.search(builder)
		val newDatas = datas.groupBy(_ (0)).map { e =>
			(e._1, e._2.map(f => (f(1), f(2))).toMap)
		}
		val squads = entityDao.find(classOf[Squad], newDatas.keys.asInstanceOf[Iterable[Long]])
		put("datas", newDatas)
		put("squads", squads)
		put("depart", entityDao.get(classOf[Department], departId))
		put("GREENID", WarningType.green)
		put("REDID", WarningType.red)
		put("YELLOWID", WarningType.yellow)
		forward()
	}

}
