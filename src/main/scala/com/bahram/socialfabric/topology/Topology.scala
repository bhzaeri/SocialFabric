package com.bahram.socialfabric.topology

import scala.collection.mutable.ArrayBuffer

abstract class Topology {

  protected var m: Array[ArrayBuffer[Int]] = _

  def createMatrix(): Array[ArrayBuffer[Int]]

  def getNeighbors(index: Int): ArrayBuffer[Int]
}