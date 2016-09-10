package com.bahram.socialfabric.topology

import scala.collection.mutable


object TopologyFactory {

  val dimension = 10

  def create(index: Int, size: Int): Topology = {
    if (index >= TopologyEnum.values.size || index < 0)
      return null
    val enum = TopologyEnum.values.toSeq(index)
    TopologyPool.getTopology(enum, size)
  }

  private def create(enum: TopologyEnum.Value, size: Int): Topology = {
    enum match {
      case TopologyEnum.GLOBAL => new GlobalTopology(size)
      case TopologyEnum.MESH => new MeshTopology(size)
      case TopologyEnum.TREE => new TreeTopology(size)
      case TopologyEnum.RING => new RingTopology(size)
    }
  }

  object TopologyPool {
    val pool = new mutable.HashMap[(TopologyEnum.Value, Int), Topology]()

    def getTopology(enum: TopologyEnum.Value, size: Int): Topology = {
      val key = (enum, size)
      if (pool.contains(key))
        pool(key)
      else {
        val t = TopologyFactory.create(key._1, key._2)
        pool += ((key, t))
        t
      }
    }
  }

}

object TopologyEnum extends Enumeration {
  val GLOBAL, MESH, TREE, RING = Value
}
