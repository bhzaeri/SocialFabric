package com.bahram.socialfabric.topology

abstract class Topology {

  def createMatrix(): Array[Array[Int]]

  def getNeighbors(index: Int): Array[Int]
}