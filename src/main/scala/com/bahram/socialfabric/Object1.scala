package com.bahram.socialfabric

import com.bahram.ca.KSEnum
import com.bahram.socialfabric.topology.{RingTopology, TopologyEnum}
import com.bahram.util.RandomUtil

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.Random
import scala.util.control.Breaks

object Object1 {

  def main(args: Array[String]) {
    var age: Long = 1E18.asInstanceOf[Long]
    println(age)
    //    var i = new Individual(80);
    var ii = new Array[Array[Int]](9)
    ii(0) = new Array[Int](9)
    ii(0)(0) = 90
    println("concat arguments = " + foo(args))
    var loop = new Breaks()
    loop.breakable {
      for (i <- 0 until 10) {
        println(i)
        loop.break()
      }
    }
    var m = new RingTopology(10).createMatrix()
    println(m(0)(0))
    textF(func1)
    for (i <- 0 until 5) {
      println(i)
    }
    var ll = new ListBuffer[Int]
    ll.+=(10)
    ll.+=(6)

    var aray = Array(0, 4, 3, 1)
    scala.util.Sorting.quickSort(aray)
    println(ll.mkString)

    println(KSEnum.values.toList(RandomUtil.nextInt(3)))

    //    var aa = ArrayBuffer.fill[Int](5)(101)
    //    for (i <- 0 until aa.length) {
    //      println(aa(i))
    //      aa.remove(i)
    //    }
    val r1 = Random.javaRandomToRandom(new java.util.Random(12))
    println("rand:::::::::::")
    for (i <- 0 until 10)
      println(r1.nextDouble())

    val mm = new mutable.HashMap[String, Int]()
    //    mm += ("asd" -> 12)
    if (mm.keySet.contains("asd"))
      println(mm("asd"))


    val test1 = Array.fill[SortTest](5) {
      new SortTest(Random.nextInt(20))
    }
    test1.foreach(obj => {
      print(obj.index_ + ", ")
    })
    println()
    scala.util.Sorting.stableSort[SortTest](test1, (t1: SortTest, t2: SortTest) => {
      t1.index_ < t2.index_
    })
    test1.foreach(obj => {
      print(obj.index_ + ", ")
    })
    println()

    val map = new mutable.HashMap[String, Int]()
    //    map.+=(("a", 1))
    map("a") = 201
    map("b") = 100
    map("c") = 150
    map("c") += 2
    val ss = map.toSeq.sortWith((a, b) => a._2 > b._2)
    mutable.ListMap(map.toSeq.sortBy(_._2): _*)
    //    println(map("1"))

    for (i <- 0 until TopologyEnum.values.size) {
      println(TopologyEnum.values.toSeq(i))
    }

    val ff1: (TopologyEnum.Value, Int) = (TopologyEnum.GLOBAL, 10)
    val ff2: (TopologyEnum.Value, Int) = (TopologyEnum.GLOBAL, 10)
    println(ff1 == ff2)

    var tt = Iterable(1, 2, 3)
    println(tt.getClass)

    var i = 0
    var ttt = ArrayBuffer.fill[Int](10) {
      i += 1
      i
    }
    println(ttt.slice(0, 4).mkString)
    ttt = ttt.-(2)
    println(ttt(0))
  }

  def foo(x: Array[String]) = x.foldLeft("")((a, b) => a + b)

  def textF(func1: ((Int, Double) => Unit)) = {
    println(func1(10, 10.0))
  }

  def func1(i: Int, d: Double) = {
    println("func1")
  }

  class SortTest(index: Int) {
    val index_ = index
  }

  class Class1 {
    var index: Int = 101
  }

  class Class2 extends Class1 {

  }

}