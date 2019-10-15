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
package org.openurp.edu.warning.web.action

import java.io.{File, FileOutputStream}
import java.time.Instant

import javax.servlet.http.Part
import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.mapping
import org.beangle.webmvc.api.context.Params
import org.beangle.webmvc.api.view.{Stream, View}
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.web.ProjectSupport
import org.openurp.edu.warning.Constants
import org.openurp.edu.warning.model.{Attachment, ElectronicFile, GradeWarning, Record}

class RecordAction extends RestfulAction[Record] with ProjectSupport {

	override def indexSetting(): Unit = {
		val semesterId = getInt("semester.id")
		val semester = {
			semesterId match {
				case None => getCurrentSemester
				case _ => entityDao.get(classOf[Semester], semesterId.get)
			}
		}
		put("currentSemester", semester)
		put("departments", getDeparts)
		super.indexSetting()
	}

	override def search(): View = {
		val allGradeWarning = entityDao.getAll(classOf[GradeWarning])
		val gradeWarningMap = allGradeWarning.map(t => (t.semester, t)).toMap
		put("gradeWarningMap", gradeWarningMap)
		val gradeWarningIds = longIds("gradeWarning")
		val fileIds = longIds("file")
		if (!gradeWarningIds.isEmpty) {
			val gradeWarnings = entityDao.find(classOf[GradeWarning], gradeWarningIds)
			put("std", gradeWarnings.head.std)
			val builder = OqlBuilder.from(classOf[Record], "record")
			builder.where("record.file.std=:std", gradeWarnings.head.std)
			val records = entityDao.search(builder)
			put("records", records)
			forward()
		} else if (!fileIds.isEmpty) {
			val builder = OqlBuilder.from(classOf[Record], "record")
			builder.where("record.file.id=:fileId", fileIds)
			val records = entityDao.search(builder)
			put("records", records)
			forward()
		}
		else {
			super.search()
		}
	}

	override def editSetting(record: Record): Unit = {
		val semesterId = getInt("semester.id")
		val semester = {
			semesterId match {
				case None => getCurrentSemester
				case _ => entityDao.get(classOf[Semester], semesterId.get)
			}
		}
		put("currentSemester", semester)
		val gradeWarnings = entityDao.find(classOf[GradeWarning], longIds("gradeWarning"))
		put("gradeWarning", gradeWarnings.head)
	}


	override def saveAndRedirect(record: Record): View = {
		get("gradeWarning.id").foreach(gradeWarningId => {
			val gradeWarnings = entityDao.find(classOf[GradeWarning], gradeWarningId.toLong)
			val builder = OqlBuilder.from(classOf[ElectronicFile], "file")
			builder.where("file.std=:std", gradeWarnings.head.std)
			val files = entityDao.search(builder)
			if (files.isEmpty) {
				val file = new ElectronicFile
				file.std = gradeWarnings.head.std
				file.updatedAt = Instant.now
				record.file = file
				entityDao.saveOrUpdate(file)
			} else {
				record.file = files.head
			}
		})
		val base = Constants.AttachmentBase + "record/"
		val aParts = Params.getAll("attachment").asInstanceOf[List[Part]]
		aParts foreach { part =>
			if (part.getSize.toInt > 0) {
				val attachment = new Attachment()
				attachment.fileSize = part.getSize.toInt
				val ext = Strings.substringAfterLast(part.getSubmittedFileName, ".")
				attachment.path = Digests.md5Hex(part.getSubmittedFileName) + (if (Strings.isEmpty(ext)) "" else "." + ext)
				attachment.name = part.getSubmittedFileName
				attachment.updatedAt = Instant.now()
				IOs.copy(part.getInputStream, new FileOutputStream(base + attachment.path))
				entityDao.saveOrUpdate(attachment)
				record.attachments += attachment
			}
		}
		super.saveAndRedirect(record)
	}


	@mapping("attachment/{attachmentId}")
	def attachment(attachmentId: Long): View = {
		val attach = entityDao.get(classOf[Attachment], attachmentId);
		val base = Constants.AttachmentBase + "record/"
		Stream(new File(base + attach.path), attach.name)
	}


	def deleteAttach(): View = {
		val base = Constants.AttachmentBase + "record/"
		val recordId = get("recordId").orNull
		get("attachmentId").foreach { attachmentId =>
			val attachment = entityDao.get(classOf[Attachment], attachmentId.toLong)
			get("recordId").foreach { recordId =>
				val record = entityDao.get(classOf[Record], recordId.toLong)
				record.attachments -= attachment
				entityDao.saveOrUpdate(record)
			}
			entityDao.remove(attachment)
			val file = new File(base + attachment.path)
			file.delete()
		}
		redirect("info", s"&id=${recordId}", "info.save.success")
	}

}
