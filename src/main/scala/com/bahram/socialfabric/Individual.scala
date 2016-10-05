package com.bahram.socialfabric

import com.bahram.ca.{Cell, Config}
import com.bahram.util.RandomUtil

import scala.collection.mutable.ArrayBuffer

abstract class Individual(size: Int) {
  private val _vector = new Array[Double](size)
  var cell: Cell = _
  var ksType: com.bahram.ca.KSEnum.Value = _
  var nextKsType: com.bahram.ca.KSEnum.Value = _
  var fitnessValue: Double = -1.0
  var otherNeighbors: ArrayBuffer[Int] = _
  var neighbors: ArrayBuffer[Int] = _
  resetNeighborhood(Config.populationSize)
  var nsk = 0

  def resetNeighborhood(popSize: Int): Unit = {
    var i = -1
    otherNeighbors = ArrayBuffer.fill[Int](popSize) {
      i += 1
      i
    }
    neighbors = ArrayBuffer.fill[Int]((popSize / 2.5).toInt) {
      val rand = RandomUtil.nextInt(otherNeighbors.length)
      val temp = otherNeighbors(rand)
      otherNeighbors.remove(rand)
      temp
    }
  }

  def vector: Array[Double] = {
    this._vector
  }

  def vector_(vector: Array[Double]) {
    for (i <- this._vector.indices)
      this._vector(i) = vector(i)
  }

  def copy(): Individual
}