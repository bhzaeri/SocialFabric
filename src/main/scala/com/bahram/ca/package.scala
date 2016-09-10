package com.bahram

import java.io.PrintWriter

import com.bahram.pso.Particle
import com.bahram.socialfabric.{Individual, Neighborhood}

/**
  * Created by zaeri on 23/08/16.
  */
package object ca {


  object Config {
    val maxFEs = 150000
    val boutSize = 5
    val lowerBound = -100
    val upperBound = 100
    val mThresh = 3
    val maxTopoDepth = 50
    val wSize = 5
    var topologyType = 3

    var normativeUpdate: (Individual, Individual, Individual, Normative, Double) => Unit = null
    var topographicUpdate: (Individual, Individual, Individual, Cell) => Unit = null
    //    var evolutionStep: (Neighborhood, (Array[Double]) => Double) => Unit = null
    var psoStrategy: ((Particle, Particle) => Unit) = null
    var countFEs = 0
    var iter = 0
    var secondPhase = false
    var thirdPhase = false
    var filePrinter: PrintWriter = null
    var fitness: (Array[Double] => Double) = null
    var makePopulation: (Int => Array[Individual]) = null
    var extra: (Individual => Unit) = null
    var tieBreakingRule: (Neighborhood, Individual) => KSEnum.Value = null
    var calculateNewPopulation: (Neighborhood, (Array[Double]) => Double) => Unit = null
    var applyNewPosition: (Neighborhood, (Array[Double]) => Double) => Unit = null
    var applyCA: (Int, Neighborhood, (Array[Double] => Double)) => Unit = null

    def compareAsc(i1: Individual, i2: Individual): Boolean = i1.fitnessValue < i2.fitnessValue
  }

}