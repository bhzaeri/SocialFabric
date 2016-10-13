package com.bahram

import java.io.PrintWriter

import com.bahram.pso.Particle
import com.bahram.socialfabric.{Individual, Neighborhood}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by zaeri on 23/08/16.
  */
package object ca {


  object Config {
    val maxFEs = 150000
    val boutSize = 5
    val lowerBound = -100
    val upperBound = 100
    val maxTopoDepth = 50
    val dimension = 10
    var mThresh = 3
    var wSize = 5
    var topologyType = 3
    //mesh
    var printCount = 1
    var populationSize = 9
    var tribeNumber = 10
    var phaseIndex = 0

    var normativeUpdate: (Individual, Individual, Individual, Normative, Double) => Unit = _
    var topographicUpdate: (Individual, Individual, Individual, Cell) => Unit = _
    var psoStrategy: ((Particle, Array[Double]) => Unit) = _
    var countFEs = 0
    var iter = 0
    var secondPhase = false
    var thirdPhase = false
    var logString = new StringBuilder

    var filePrinter: PrintWriter = _
    var fitness: (Array[Double] => Double) = _
    var makePopulation: (Int => Array[Individual]) = _
    var extra: (Individual => Unit) = _
    var tieBreakingRule: (Neighborhood, Individual) => KSEnum.Value = _
    var calculateNewPopulation: (Int, Neighborhood, (Array[Double]) => Double) => Unit = _
    var applyNewPosition: (Neighborhood, (Array[Double]) => Double) => Unit = _
    var applyCA: (Int, Neighborhood, Boolean, (Array[Double] => Double)) => Boolean = _
    var epGenerateStrategy: (Int, Neighborhood, Boolean, (Array[Double] => Double)) => Boolean = _
    var neighborhoodRestructuring: () => Unit = _
    var resetNeighborhood: (ArrayBuffer[Individual]) => Unit = (ids) => {}
    var stepUpNsk: (Individual) => Unit = _

    def compareAsc(i1: Individual, i2: Individual): Boolean = i1.fitnessValue < i2.fitnessValue
  }

}