package com.bahram.socialfabric

import com.bahram.ca.Cell

abstract class Individual(size: Int) {
  private val _vector = new Array[Double](size);
  var cell: Cell = null
  var ksType: com.bahram.ca.KSEnum.Value = null
  var nextKsType: com.bahram.ca.KSEnum.Value = null
  var fitnessValue: Double = -1.0;

  def vector: Array[Double] = {
    return this._vector;
  }

  def vector_(vector: Array[Double]) {
    for (i <- 0 until this._vector.length)
      this._vector(i) = vector(i);
  }

  def copy(): Individual
}