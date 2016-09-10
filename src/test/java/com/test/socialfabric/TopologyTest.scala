package com.test.socialfabric

import com.bahram.socialfabric.topology.{GlobalTopology, MeshTopology, RingTopology, TreeTopology}
import com.bahram.util.RandomUtil
import org.scalatest.{BeforeAndAfter, FunSuite}

class TopologyTest extends FunSuite with BeforeAndAfter {

  test("ring test") {
    val m = new RingTopology(10).createMatrix()

    println(m(0)(0));
    for (i <- 0 to 8)
      assert(m(i)(1) == i + 1)
    for (i <- 1 to 9)
      assert(m(i)(0) == i - 1)

    assert(m(0)(0) == 9)
    assert(m(9)(1) == 0)
  }

  test("global test") {
    val m = new GlobalTopology(10).createMatrix()
    assert(m(9)(8) == 8);
  }

  test("tree test") {
    val m = new TreeTopology(10).createMatrix()
    assert(m(1)(0) == 0)
    assert(m(2)(0) == 0)
    assert(m(5).length == 1)
    assert(m(5)(0) == 2)

  }

  test("mesh test") {
    var m = new MeshTopology(10).createMatrix()
  }
  test("random test") {
//    println(RandomUtil.nextInt(100))
    println(RandomUtil.nextInt(100))
    println(RandomUtil.nextDouble())
  }

}