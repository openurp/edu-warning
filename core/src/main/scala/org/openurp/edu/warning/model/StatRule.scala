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

import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.Named
import scala.annotation.varargs

/**
 * 统计规则
 */
class StatRule extends IntId with Named {

  /**
   * 预警类别
   */
  var warningType: WarningType = _

  /**
   * 统计办法
   */
  var method: StatMethod = _

  /**
   * 不及格学分上限
   */
  var maxValue: Float = _

  /**
   * 不及格学分下限
   */
  var minValue: Float = _

}
