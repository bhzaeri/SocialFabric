package com.bahram.socialfabric.topology

import scala.collection.mutable.ArrayBuffer

class RingTopology(n: Int) extends Topology {

  override def getNeighbors(index: Int): ArrayBuffer[Int] = {
    if (m == null)
      createMatrix()
    m(index)
  }

  override def createMatrix(): Array[ArrayBuffer[Int]] = {
    m = new Array[ArrayBuffer[Int]](n)
    for (i <- 0 until n) {
      m(i) = new ArrayBuffer[Int](2)
    }
    m(0) += (n - 1)
    m(0) += 1

    for (i <- 1 until n) {
      m(i) += i - 1
      m(i) += (i + 1) % n
    }
    m
  }
}