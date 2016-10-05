package com.bahram.socialfabric.topology

import scala.collection.mutable.ArrayBuffer

class TreeTopology(n: Int) extends Topology {


  def getNeighbors(index: Int): ArrayBuffer[Int] = {
    if (m == null) {
      createMatrix()
    }
    m(index)
  }

  override def createMatrix(): Array[ArrayBuffer[Int]] = {
    m = new Array[ArrayBuffer[Int]](n)
    m(0) = ArrayBuffer.fill[Int](2) {
      -1
    }
    m(0)(0) = 1
    m(0)(1) = 2
    var parent = 0
    var current = 1
    var child = 3
    while (child < n) {
      m(current) = ArrayBuffer.fill[Int](3) {
        -1
      }
      m(current)(0) = parent
      m(current)(1) = child
      child += 1
      if (child < n)
        m(current)(2) = child
      child += 1
      current += 1
      if (current % 2 != 0)
        parent += 1
    }

    while (current < n) {
      m(current) = ArrayBuffer.fill[Int](1){-1}
      m(current)(0) = parent
      current += 1
      if (current % 2 != 0)
        parent += 1
    }
    m
  }
}