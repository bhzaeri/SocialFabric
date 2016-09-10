package com.bahram.socialfabric.topology

class RingTopology(n: Int) extends Topology {

  private var m: Array[Array[Int]] = null

  override def getNeighbors(index: Int): Array[Int] = {
    if (m == null)
      createMatrix()
    m(index)
  }

  override def createMatrix(): Array[Array[Int]] = {
    m = new Array[Array[Int]](n)
    for (i <- 0 until n) {
      m(i) = new Array[Int](2);
    }
    m(0)(0) = n - 1;
    m(0)(1) = 1;

    for (i <- 1 until n) {
      m(i)(0) = i - 1;
      m(i)(1) = (i + 1) % n;
    }
    m
  }
}