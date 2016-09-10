package com.bahram.util

import com.bahram.ca.{Config, Topographic}
import org.apache.log4j.Logger

/**
  * Created by zaeri on 23/08/16.
  */
object MyLogger {

  val topographicLogger = Logger.getLogger(classOf[Topographic])

  var printCount = 1
  var flag = false

  def checkPrint(): Unit = {
    if (Config.countFEs == (Config.maxFEs * printCount * 0.01).asInstanceOf[Int]) {
      flag = true
    }
  }

  def logInfo(value: Double): Unit = {
    if (flag) {
      Config.filePrinter.write(printCount.toString + " ::: " + value.toString + "\n")
      printCount += 1
      flag = false
    }
  }

  def close(): Unit = {
    Config.filePrinter.close()
  }
}
