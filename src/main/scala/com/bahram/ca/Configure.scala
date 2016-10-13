package main.scala.com.bahram.ca

import java.io.PrintWriter

import cec2015.FitnessFactory
import com.bahram.ca.Config
import com.bahram.ep.{EpRunner, EvolutionaryProgramming}
import com.bahram.pso.{PSOAlgorithm, PSOFactory, PsoRunner}
import main.java.com.bahram.ca.{NormativeUpdate, TopographicUpdate}
import main.scala.com.bahram.socialfabric.TieBreakingRules

/**
  * Created by zaeri on 01/10/16.
  */
object Configure {

  def sfep(funcIndex: Int, path: String) = {
    if (Config.filePrinter == null)
      Config.filePrinter = new PrintWriter(path + "/sfep3/sfep3_" + funcIndex + "_" + Config.dimension + ".txt")
    Config.makePopulation = EpRunner.make
    Config.extra = EpRunner.extra
    Config.calculateNewPopulation = EvolutionaryProgramming.calculateNewPopulation
    Config.applyNewPosition = EvolutionaryProgramming.applyNewPositions
    Config.epGenerateStrategy = PSOFactory.applySocialFabric
    Config.neighborhoodRestructuring = TribalRunner.neighborhoodRestructuring1
    //    Config.resetNeighborhood = TribalRunner.resetNeighborhood
    Config.wSize = 1
    Config.applyCA = null

    //    Config.topologyType = 0
    Config.phaseIndex = 0
    Config.populationSize = 9
    Config.tribeNumber = 10
    Config.normativeUpdate = NormativeUpdate.update1
    Config.topographicUpdate = TopographicUpdate.update1
    Config.psoStrategy = PSOFactory.pso1
    Config.fitness = FitnessFactory.factory(funcIndex)
    Config.tieBreakingRule = TieBreakingRules.mfu
    Config.iter = 0
    Config.countFEs = 0
    Config.printCount = 1
    Config.secondPhase = false
    Config.thirdPhase = false
    TribalRunner.bestSoFar = null
  }

  def sfhep(funcIndex: Int, path: String) = {
    if (Config.filePrinter == null)
      Config.filePrinter = new PrintWriter(path + "/sfhep/sfhep_" + funcIndex + "_" + Config.dimension + ".txt")
    Config.makePopulation = EpRunner.make
    Config.extra = EpRunner.extra
    Config.calculateNewPopulation = EvolutionaryProgramming.calculateNewPopulation
    Config.applyNewPosition = EvolutionaryProgramming.applyNewPositions
    Config.epGenerateStrategy = PSOFactory.applySocialFabric2
    Config.neighborhoodRestructuring = TribalRunner.neighborhoodRestructuring2
    Config.resetNeighborhood = TribalRunner.resetNeighborhood
    Config.wSize = 1
    Config.applyCA = null

    Config.phaseIndex = 0
    Config.mThresh = 3
    Config.populationSize = 9
    Config.tribeNumber = 10
    Config.normativeUpdate = NormativeUpdate.update2
    Config.topographicUpdate = TopographicUpdate.update2
    Config.psoStrategy = PSOFactory.pso1
    Config.fitness = FitnessFactory.factory(funcIndex)
    Config.tieBreakingRule = TieBreakingRules.mfu
    Config.iter = 0
    Config.countFEs = 0
    Config.printCount = 1
    Config.secondPhase = false
    Config.thirdPhase = false
    TribalRunner.bestSoFar = null
  }

  def sfpso(funcIndex: Int, path: String) = {
    if (Config.filePrinter == null) {
      Config.filePrinter = new PrintWriter(path + "/sfpso2/sfpso2_" + funcIndex + "_" + Config.dimension + ".txt")
      Config.logString = new StringBuilder
    }
    Config.makePopulation = PsoRunner.make
    Config.extra = PsoRunner.extra
    Config.calculateNewPopulation = PSOAlgorithm.calculateNewPopulation
    Config.applyNewPosition = PSOAlgorithm.applyNewPosition
    Config.wSize = 3
    Config.applyCA = PSOFactory.applySocialFabric2
    Config.neighborhoodRestructuring = TribalRunner.neighborhoodRestructuring2
    Config.resetNeighborhood = TribalRunner.resetNeighborhood
    Config.stepUpNsk = (i) => {}

    Config.mThresh = 1
    Config.topologyType = 0
    Config.phaseIndex = 0
    Config.populationSize = 9
    Config.tribeNumber = 10
    Config.normativeUpdate = NormativeUpdate.update2
    Config.topographicUpdate = TopographicUpdate.update2
    Config.psoStrategy = PSOFactory.pso1
    Config.fitness = FitnessFactory.factory(funcIndex)
    Config.tieBreakingRule = TieBreakingRules.mfu
    Config.iter = 0
    Config.countFEs = 0
    Config.printCount = 1
    Config.secondPhase = false
    Config.thirdPhase = false
    TribalRunner.bestSoFar = null
  }

  def tpso(funcIndex: Int, path: String) = {
    if (Config.filePrinter == null) {
      Config.filePrinter = new PrintWriter(path + "/tpso/tpso_" + funcIndex + "_" + Config.dimension + ".txt")
      Config.logString = new StringBuilder
    }
    Config.makePopulation = PsoRunner.make
    Config.extra = PsoRunner.extra
    Config.calculateNewPopulation = PSOAlgorithm.calculateNewPopulation
    Config.applyNewPosition = PSOAlgorithm.applyNewPosition
    Config.topologyType = 0
    Config.wSize = -1
    Config.applyCA = null
    Config.neighborhoodRestructuring = null
    Config.stepUpNsk = (i) => {}

    Config.phaseIndex = 0
    Config.populationSize = 9
    Config.tribeNumber = 10
    Config.psoStrategy = PSOFactory.pso1
    Config.fitness = FitnessFactory.factory(funcIndex)
    Config.iter = 0
    Config.countFEs = 0
    Config.printCount = 1
    Config.secondPhase = false
    Config.thirdPhase = false
    TribalRunner.bestSoFar = null
  }

  def caep(funcIndex: Int, path: String) = {
    if (Config.filePrinter == null)
      Config.filePrinter = new PrintWriter(path + "/caep/caep_" + funcIndex + "_" + Config.dimension + ".txt")
    Config.makePopulation = EpRunner.make
    Config.extra = EpRunner.extra
    Config.calculateNewPopulation = EvolutionaryProgramming.calculateNewPopulation
    Config.applyNewPosition = EvolutionaryProgramming.applyNewPositions
    Config.epGenerateStrategy = PSOFactory.applyNormalCA
    Config.wSize = 1
    Config.applyCA = null
    Config.neighborhoodRestructuring = null

    Config.phaseIndex = 0
    Config.populationSize = 9
    Config.tribeNumber = 10
    Config.normativeUpdate = NormativeUpdate.update2
    Config.topographicUpdate = TopographicUpdate.update2
    Config.psoStrategy = PSOFactory.pso1
    Config.fitness = FitnessFactory.factory(funcIndex)
    Config.tieBreakingRule = TieBreakingRules.mfu
    Config.iter = 0
    Config.countFEs = 0
    Config.printCount = 1
    Config.secondPhase = false
    Config.thirdPhase = false
    TribalRunner.bestSoFar = null
  }
}
