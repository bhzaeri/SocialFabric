package main.scala.com.bahram.util

import com.bahram.ca.Config
import com.bahram.socialfabric.Individual

/**
  * Created by zaeri on 09/10/16.
  */
object UtilMethods {
  def adjustBoundaries(t: Individual): Unit = {
    for (j <- t.vector.indices) {
      if (t.vector(j) < Config.lowerBound)
        t.vector(j) = Config.lowerBound
      if (t.vector(j) > Config.upperBound)
        t.vector(j) = Config.upperBound
    }
  }

}
