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

import org.beangle.data.model.pojo.Named
import org.beangle.data.model.pojo.Coded
import org.beangle.data.model.IntId

class WarningType extends IntId with Named {
  
  /**
   * 预警级别，分别对应0,2,1
   */
  var level: Int = _

  def this(id: Int) {
    this()
    this.id = id
  }

  def this(id: Int, level: Int, name: String) {
    this()
    this.id = id
    this.level = level
    this.name = name
  }

}

object WarningType {
  val green = 1 //未预警

  val red = 2
  val yellow = 3
}
