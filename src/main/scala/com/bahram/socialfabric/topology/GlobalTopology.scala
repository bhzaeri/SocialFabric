package com.bahram.socialfabric.topology

import scala.collection.mutable.ArrayBuffer

class GlobalTopology(n: Int) extends Topology {

  override def createMatrix(): Array[ArrayBuffer[Int]] = {
    val m = new Array[ArrayBuffer[Int]](n)

    for (i <- 0 until n) {
      m(i) = new ArrayBuffer[Int](n - 1)

      var counter = 0
      for (j <- 0 until m(i).length) {
        if (i == j)
          counter = counter + 1
        m(i)(j) = counter
        counter = counter + 1
      }
    }

    m
  }

  override def getNeighbors(index: Int): ArrayBuffer[Int] = {
    var m = new ArrayBuffer[Int]
    for (i <- 0 until n) {
      if (i != index)
        m += i
    }
    m
  }
}