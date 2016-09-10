package com.bahram.socialfabric

class DefaultMatrix(n: Int) extends Network {
  private var m = Array.ofDim[Double](n, n);

  def matrix: Array[Array[Double]] = { return m }

}