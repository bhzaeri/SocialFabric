package com.bahram.socialfabric.topology

import org.apache.log4j.Logger

import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks

class MeshTopology(n: Int) extends Topology {

  val h: Int = Math.floor(Math.sqrt(n / 2)).asInstanceOf[Int]
  val logger = Logger.getLogger(classOf[MeshTopology])
  private var m: Array[Array[Int]] = _

  override def getNeighbors(index: Int): Array[Int] = {
    if (m == null)
      createMatrix()
    m(index)
  }

  override def createMatrix(): Array[Array[Int]] = {
    m = new Array[Array[Int]](n)
    var l: Int = n / h
    if (n > h * l)
      l += 1

    val outer = new Breaks()
    val inner = new Breaks()

    var counter = 0
    outer.breakable {
      for (i <- 0 until h) {
        inner.breakable {
          for (j <- 0 until l) {
            val list: ArrayBuffer[Int] = ArrayBuffer.empty[Int]
            if (i - 1 >= 0)
              list += counter - l
            if (j - 1 >= 0)
              list += counter - 1
            if (i + 1 < h && counter + l < n)
              list += counter + l
            if (j + 1 < l && counter + 1 < n)
              list += counter + 1

            m(counter) = list.toArray
            counter += 1
            if (counter >= n) {
              inner.break()
            }
          }
        }
        if (counter >= n) {
          outer.break()
        }
      }
    }
    m
  }
}