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

import org.beangle.commons.bean.Initializing
import org.beangle.commons.logging.Logging
import org.beangle.commons.lang.Throwables

class StatJobStarter extends Initializing with Logging{

  var autoBatchStat: AutoBatchStat = _
  /** 间隔 60 secs 自动刷新 */
  val refreshInterval = 1000 * 15;

  def init(): Unit = {
    System.out.println("gew job starting...");
    new Thread(new Runnable() {

      def run(): Unit = {
        while (true) {
          try {
            System.out.println("gew job started");
            autoBatchStat.execute()
            Thread.sleep(refreshInterval);
          } catch {
            case e: Throwable => logger.error(Throwables.stackTrace(e)) 
          }
        }
      }
    }).start();
  }

}