package com.bahram.util

import com.bahram.ca.{Config, Topographic}
import org.apache.log4j.Logger

/**
  * Created by zaeri on 23/08/16.
  */
object MyLogger {

  val topographicLogger = Logger.getLogger(classOf[Topographic])

  var flag = false

  def checkPrint(): Unit = {
    if (Config.countFEs == (Config.maxFEs * Config.printCount * 0.01).asInstanceOf[Int]) {
      flag = true
    }
  }

  def logInfo(value: Double): Unit = {
    if (flag) {
      //      Config.filePrinter.write(value.toString + " ")
      Config.logString.append(value.toString + " ")
      Config.printCount += 1
      flag = false
    }
  }

  def logInfo(value: String): Unit = {
    Config.logString.append("\n")
  }

  def close(): Unit = {
    Config.filePrinter.write(Config.logString.toString())
    Config.filePrinter.flush()
    Config.filePrinter.close()
  }
}
