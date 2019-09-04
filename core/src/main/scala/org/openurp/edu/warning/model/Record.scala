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
package org.openurp.edu.warning.model

import org.beangle.data.model.LongId
import jdk.nashorn.internal.ir.WithNode
import org.beangle.data.model.pojo.Updated
import org.openurp.edu.base.model.Student
import scala.annotation.varargs
import org.openurp.edu.base.model.Semester
import org.beangle.commons.collection.Collections
import scala.collection.mutable.Buffer

/**
 * 帮扶记录
 */
class Record extends LongId with Updated {

  var semester: Semester = _

  var std: Student = _

  var description: String = _

  var pictures: Buffer[Attachment] = Collections.newBuffer[Attachment]

}
