package com.bahram.ca

import com.bahram.socialfabric.Individual
import com.bahram.socialfabric.topology.TopologyFactory
import com.bahram.util.{MyLogger, RandomUtil}
import main.scala.com.bahram.ca.TribalRunner
import org.apache.log4j.Logger

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * Created by zaeri on 02/08/16.
  */
class Topographic extends KnowledgeSource {

  var best: Individual = _
  var cells: ArrayBuffer[Cell] = new ArrayBuffer[Cell]
  cells.+=(new Cell)

  override def update(population: Array[Individual], fitness: (Array[Double]) => Double): Array[Individual] = {
    scala.util.Sorting.stableSort[Individual](population, Config.compareAsc _)
    if (best == null || population(0).fitnessValue < best.fitnessValue) {
      best = population(0)
    }
    val l = population.length
    val s = Math.ceil(0.2 * population.length).asInstanceOf[Int]
    assert(s > 0)
    val newCells = new ArrayBuffer[Cell]()
    population.take(s).foreach(p => {
      var i = 0
      while (i < cells.length) {
        val c = cells(i)
        if (c.exists(p)) {
          if (p.fitnessValue <= c.best)
            if (c.depth < Config.maxTopoDepth) {
              c.best = p.fitnessValue
              var ind = -1
              val a_t = ArrayBuffer.fill[Int](TopologyFactory.dimension)({
                ind += 1
                ind
              })
              val indexes = Array.fill[Int](4)({
                ind = RandomUtil.nextInt(a_t.length)
                a_t.remove(ind)
              })
              indexes.foreach(index => {
                val c1 = new Cell
                val c2 = new Cell
                for (i <- 0 until TopologyFactory.dimension) {
                  c1.u(i) = c.u(i)
                  c2.u(i) = c.u(i)
                  c1.l(i) = c.l(i)
                  c2.l(i) = c.l(i)
                }
                val d1 = c.u(index) - c.l(index)
                c1.u(index) = c1.l(index) + d1 / 2
                c2.l(index) = c1.u(index)
                c1.depth = c.depth + 1
                c2.depth = c.depth + 1
                newCells += c1
                newCells += c2
              })
              cells.remove(i)
            }
            else
              MyLogger.topographicLogger.info("DEPTH is overflow!!!!")
          i = cells.length
        }
        i += 1
      }
    })
    cells ++= newCells

    var avg = 0.0
    var tempLength = 0
    //find the best of each cell
    cells.foreach(c => {
      var b: Individual = null
      var bb = Double.MaxValue
      population.foreach(ind => {
        if (c.exists(ind)) {
          ind.cell = c
          if (ind.fitnessValue < bb) {
            bb = ind.fitnessValue
            b = ind
          }
        }
      })
      if (b != null) {
        avg += bb
        c.best = b.fitnessValue
        b.cell = c
        tempLength += 1
      }
      else
        c.state = StateEnum.NE
    })

    avg /= tempLength
    cells.foreach(c => {
      if (c.state != StateEnum.NE)
        if (c.best <= avg)
          c.state = StateEnum.H
        else if (c.best > avg)
          c.state = StateEnum.L
    })

    var i = 0
    val offSprings = Array.fill[Individual](l) {
      val parent = population(i)
      val child = parent.copy()
      val c = parent.cell
      Config.topographicUpdate(child, best, parent, c)
      child.fitnessValue = fitness(child.vector)
      TribalRunner.checkCount()
      child.ksType = KSEnum.TOPOGRAPHIC
      i += 1
      child
    }

    offSprings
    //    val length = population.length
    //    val population2: Array[Individual] = population ++ offSprings
    //    scala.util.Sorting.stableSort[Individual](population2, compareAsc _)
    //    population2.slice(0, length)
  }
}

object StateEnum extends Enumeration {
  val H, NE, L = Value
}


class Cell {
  val logger = Logger.getLogger(classOf[Cell])

  var u: Array[Double] = Array.fill(TopologyFactory.dimension)(100.0)
  var l: Array[Double] = Array.fill(TopologyFactory.dimension)(-100.0)
  var state: StateEnum.Value = _
  var depth = 0
  var pointer: ListBuffer[Cell] = new ListBuffer[Cell]()
  var best: Double = Double.MaxValue

  def exists(p: Individual): Boolean = {
    var flag = true
    var i = 0
    while (i < p.vector.length && flag) {
      if (p.vector(i) < this.l(i) || p.vector(i) > this.u(i))
        flag = false
      i += 1
    }
    flag
  }

}
