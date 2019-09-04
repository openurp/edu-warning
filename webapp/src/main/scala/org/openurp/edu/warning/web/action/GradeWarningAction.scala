package org.openurp.edu.warning.web.action

import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.base.model.Semester
import org.openurp.edu.boot.web.ProjectSupport
import org.openurp.edu.warning.model.{GradeWarning, WarningType}
import org.openurp.edu.warning.service.GradeWarningService
import org.openurp.edu.warning.service.impl.UnpassedCreditsStat

class GradeWarningAction extends RestfulAction[GradeWarning] with ProjectSupport {

	var gradeWarningservice: GradeWarningService = _
	var unpassedCreditsStat: UnpassedCreditsStat = _

	override def indexSetting(): Unit = {
		val semesterId = getInt("semester.id")
		val semester = {
			semesterId match {
				case None =>
					getCurrentSemester()
				case _ => entityDao.get(classOf[Semester], semesterId.get)
			}
		}
		put("currentSemester", semester)
		put("warningTypes", entityDao.getAll(classOf[WarningType]))
		put("departments", getDeparts)
		super.indexSetting()
	}

	override def getQueryBuilder: OqlBuilder[GradeWarning] = {
		val builder = OqlBuilder.from(classOf[GradeWarning], "gradeWarning")
		val a = get("isGreen")
		get("isGreen").foreach(a => a match {
			case "0" => builder.where("gradeWarning.warningType.level=1 or gradeWarning.warningType.level=2")
			case "1" => builder.where("gradeWarning.warningType.level=0")
		})
		populateConditions(builder)
		builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
	}

	def autoStat(): View = {
		val gradeWarnings = entityDao.find(classOf[GradeWarning], longIds("gradeWarning"))
		gradeWarnings.foreach(gradeWarning => {
			gradeWarningservice.autoStat(gradeWarning.std, gradeWarning.semester)
		})
		redirect("search", "统计完成")
	}

	def courseGradeInfo(): View = {
		val gradeWarnings = entityDao.find(classOf[GradeWarning], longIds("gradeWarning"))
		//    val std = gradeWarnings.head.std
		val grades = unpassedCreditsStat.getUnPassedGrades(gradeWarnings.head.std, getCurrentSemester())
		put("grades", grades)
		forward()
	}

}