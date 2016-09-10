package com.bahram.ep

import com.bahram.socialfabric.Individual

/**
  * Created by zaeri on 31/07/16.
  */
class EpIndividual(size: Int) extends Individual(size) {

  var strategy: Array[Double] = new Array[Double](size)
  var wins: Int = 0;

  override def copy(): Individual = {
    val p = new EpIndividual(size)
    p.fitnessValue = Double.MaxValue
    p
  }
}
