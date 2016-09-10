package com.bahram.socialfabric

import com.bahram.ca._
import com.bahram.socialfabric.topology.Topology

import scala.collection.mutable

class Neighborhood(_individuals: Array[Individual], t: Topology, caModule: CAModule) {

  val ksCount = new mutable.HashMap[KSEnum.Value, Long]()
  KSEnum.values.foreach(enum => {
    ksCount += (enum -> 0)
  })
  val individuals_ = _individuals
  var topology: Topology = t
  var cAModule: CAModule = caModule
  var bestSoFarValue = Double.MaxValue
  var nsk = 0
  var topologyIndex = Config.topologyType
  var best: Individual = _
  var bestIndex = -1

//  def evolutionStep(): Unit = {
  //    Config.evolutionStep(this, Config.fitness)
  //  }

  def getIndividuals = individuals_

  def setIndividuals(population: Array[Individual]) = {
    for (i <- individuals_.indices) {
      individuals_(i) = population(i)
    }
  }

  def getBestValueOfNeighborhood(index: Int): Double = {
    val i = getBestIndexOfNeighborhood(index)
    _individuals(i).fitnessValue
  }

  def getBestIndexOfNeighborhood(index: Int): Int = {
    val neighbors: Array[Int] = topology.getNeighbors(index)
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

  def getBestOfNeighborhood(index: Int): Individual = {
    val i = getBestIndexOfNeighborhood(index)
    _individuals(i)
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

  def getBestIndividual = best

  def flag = _individuals == individuals_


}