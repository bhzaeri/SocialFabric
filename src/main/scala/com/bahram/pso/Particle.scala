package com.bahram.pso

import com.bahram.socialfabric.Individual

class Particle(n: Int) extends Individual(n) {

  private val _bestVector = new Array[Double](n)
  private val _velocity = new Array[Double](n)
  var bestFitness: Double = Double.MaxValue

  def velocity: Array[Double] = {
    _velocity
  }

  def bestVector: Array[Double] = {
    this._bestVector
  }

  def bestVector_(b: Array[Double]) {
    for (i <- _bestVector.indices)
      this._bestVector(i) = b(i)
  }

  override def copy(): Individual = {
    val p = new Particle(n)
    p.fitnessValue = Double.MaxValue
    p
  }
}