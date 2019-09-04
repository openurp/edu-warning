package org.openurp.edu.warning.service

import org.beangle.commons.bean.Initializing
import org.beangle.commons.logging.Logging
import org.beangle.commons.lang.Throwables

class StatJobStarter extends Initializing with Logging{

  var autoBatchStat: AutoBatchStat = _
  /** 间隔 60 secs 自动刷新 */
  val refreshInterval = 1000 * 60;

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