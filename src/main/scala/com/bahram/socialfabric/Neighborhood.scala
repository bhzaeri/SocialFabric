package com.bahram.socialfabric

import com.bahram.ca._
import com.bahram.socialfabric.topology.Topology

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class Neighborhood(_individuals: Array[Individual], t: Topology, caModule: CAModule) {

  val ksCount = new mutable.HashMap[KSEnum.Value, Long]()
  KSEnum.values.foreach(enum => {
    ksCount += (enum -> 0)
  })
  val individuals_ = _individuals
  var tempIndividuals: Array[Individual] = new Array[Individual](_individuals.length)
  var cAModule: CAModule = caModule
  var bestSoFarValue = Double.MaxValue
  var nsk = 0
  var topologyIndex = Config.topologyType
  var best: Individual = _
  var bestIndex = -1
  private var topology: Topology = t
  setTopology(t)

  def getIndividuals = individuals_

  def setIndividuals(population: Array[Individual]) = {
    for (i <- individuals_.indices) {
      individuals_(i) = population(i)
    }
  }

  def setTempIndividuals(population: Array[Individual]) = {
    for (i <- tempIndividuals.indices) {
      tempIndividuals(i) = population(i)
    }
  }

  def getBestValueOfNeighborhood(index: Int): Double = {
    val i = getBestIndexOfNeighborhood(index)
    _individuals(i).fitnessValue
  }

  def getBestOfNeighborhood(index: Int): Individual = {
    val i = getBestIndexOfNeighborhood(index)
    _individuals(i)
  }

  def getBestIndexOfNeighborhood(index: Int): Int = {
    val neighbors = topology.getNeighbors(index)
    var min = Double.MaxValue
    var minIndex = -1
    neighbors foreach { i => {
      if (_individuals(i).fitnessValue < min) {
        minIndex = i
        min = _individuals(i).fitnessValue
      }
    }
    }
    minIndex
  }

  def findBestIndividual(): Individual = {
    var bestValue = Double.MaxValue
    for (i <- _individuals.indices) {
      val x = _individuals(i)
      if (x.fitnessValue < bestValue) {
        bestValue = x.fitnessValue
        best = x
        bestIndex = i
      }
    }
    best
  }

  def getBestOfNeighborhood2(index: Int): Individual = {
    var minValue = Double.MaxValue
    var minIndex = -1
    individuals_(index).neighbors.foreach(i => {
      if (i >= 0 && individuals_(i).fitnessValue < minValue) {
        minValue = individuals_(i).fitnessValue
        minIndex = i
      }
    })
    individuals_(minIndex)
  }

  def getNeighbors(index: Int): ArrayBuffer[Int] = {
    this.topology.getNeighbors(index)
  }

  def getNeighbors2(index: Int): ArrayBuffer[Int] = {
    individuals_(index).neighbors
  }

  def getBestIndividual = best

  def flag = _individuals == individuals_

  def getTopology = topology

  def setTopology(t: Topology): Unit = {
    this.topology = t
    for (i <- this.individuals_.indices) {
      this.individuals_(i).neighbors = t.getNeighbors(i)
      val neighbors = this.individuals_(i).neighbors
      var temp = -1
      val otherNeighbors = ArrayBuffer.fill[Int](Config.populationSize) {
        temp += 1
        temp
      } //this.individuals_(i).otherNeighbors
      otherNeighbors.remove(i)
      for (i <- neighbors) {
        val index = otherNeighbors.indexOf(i)
        if (index >= 0)
          otherNeighbors.remove(index)
      }
      this.individuals_(i).otherNeighbors = otherNeighbors
    }
  }
}