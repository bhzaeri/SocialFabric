package com.bahram.socialfabric.topology

import scala.collection.mutable.ArrayBuffer

class GlobalTopology(n: Int) extends Topology {

  override def createMatrix(): Array[Array[Int]] = {
    val m = new Array[Array[Int]](n);

    for (i <- 0 until n) {
      m(i) = new Array[Int](n - 1);

      var counter = 0;
      for (j <- 0 until m(i).length) {
        if (i == j)
          counter = counter + 1;
        m(i)(j) = counter;
        counter = counter + 1;
      }
    }

    m
  }

  override def getNeighbors(index: Int): Array[Int] = {
    var m = new ArrayBuffer[Int];
    for (i <- 0 until n) {
      if (i != index)
        m += i
    }
    m.toArray
  }
}