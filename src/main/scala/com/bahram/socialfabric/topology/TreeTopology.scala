package com.bahram.socialfabric.topology

class TreeTopology(n: Int) extends Topology {

  private var m: Array[Array[Int]] = null

  override def createMatrix(): Array[Array[Int]] = {
    m = new Array[Array[Int]](n)
    m(0) = new Array[Int](2);
    m(0)(0) = 1;
    m(0)(1) = 2;
    var parent = 0;
    var current = 1;
    var child = 3;
    while (child < n) {
      m(current) = new Array[Int](3);
      m(current)(0) = parent;
      m(current)(1) = child;
      child += 1;
      if (child < n)
        m(current)(2) = child;
      child += 1;
      current += 1;
      if (current % 2 != 0)
        parent += 1;
    }

    while (current < n) {
      m(current) = new Array[Int](1);
      m(current)(0) = parent;
      current += 1;
      if (current % 2 != 0)
        parent += 1;
    }
    m
  }

  def getNeighbors(index: Int): Array[Int] = {
    if (m == null) {
      createMatrix()
    }
    m(index)
  }
}