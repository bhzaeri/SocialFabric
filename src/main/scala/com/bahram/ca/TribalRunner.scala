package main.scala.com.bahram.ca

import java.io.PrintWriter

import cec2015.FitnessFactory
import com.bahram.ca._
import com.bahram.ep.{EpRunner, EvolutionaryProgramming}
import com.bahram.pso.PSOFactory
import com.bahram.socialfabric.topology.TopologyFactory
import com.bahram.socialfabric.{Individual, Neighborhood}
import com.bahram.util.MyLogger
import main.java.com.bahram.ca.{NormativeUpdate, TopographicUpdate}
import main.scala.com.bahram.socialfabric.TieBreakingRules
import org.apache.log4j.Logger

import scala.collection.mutable.ArrayBuffer

/**
  * Created by zaeri on 24/08/16.
  */
object TribalRunner {

  val popSize = 9
  var logger = Logger.getLogger(TribalRunner.getClass)
  var tribes: Array[Neighborhood] = _
  var bestSoFar: Individual = _

  def main(args: Array[String]): Unit = {
    run()
  }

  def run(): Unit = {
    configure()
    tribes = Array.fill[Neighborhood](10) {
      new Neighborhood(PSOFactory.population(Config.makePopulation(popSize), Config.extra, Config.fitness),
        TopologyFactory.create(0, popSize), new CAModule)
    }
    while (Config.countFEs <= Config.maxFEs) {
      tribes.foreach(tribe => {
        Config.calculateNewPopulation(tribe, Config.fitness)
      })

      tribes.foreach(tribe => {
        Config.applyNewPosition(tribe, Config.fitness)
      })
      Config.iter += 1

      tribes.foreach(tribe => {
        tribe.findBestIndividual()
      })

      logBestSoFar()
      //checking the conditions for stagnation
      if (Config.secondPhase || Config.thirdPhase) {
        tribes.foreach(tribe => {
          var flag = false
          tribe.getIndividuals.foreach(i => {
            if (i.fitnessValue < tribe.bestSoFarValue) {
              tribe.bestSoFarValue = i.fitnessValue
              tribe.nsk = 0
              flag = true
            }
          })
          if (!flag)
            tribe.nsk += 1
        })
      }

      if (Config.secondPhase || Config.thirdPhase) {
        tribes.foreach(tribe => {
          neighborhoodRestructure(tribe)
        })
      }

      tribes.foreach(tribe => {
        if (Config.applyCA != null) {
          Config.applyCA(Config.iter, tribe, Config.fitness)

          val ks = tribe.individuals_(0).ksType
          var flag = true
          for (i <- 1 until tribe.individuals_.length) {
            if (tribe.individuals_(i).ksType != ks)
              flag = false
          }
          if (flag) {
            val gggg = 0
          }
        }
      })



      if (Config.thirdPhase && tribes.length > 1) {
        tribes = Array.fill[Neighborhood](1) {
          unify()
        }
        logger.info("unifiedDDDDDDDDDDDDDDDDDDDD")
      }

      logger.info(Config.iter)
    }
    MyLogger.close()
  }

  def configure() = {

    Config.filePrinter = new PrintWriter("sfep.txt")
    Config.makePopulation = EpRunner.make
    Config.extra = EpRunner.extra
    Config.calculateNewPopulation = EvolutionaryProgramming.calculateNewPopulation
    Config.applyNewPosition = EvolutionaryProgramming.applyNewPositions

    //        Config.filePrinter = new PrintWriter("sfpso.txt")
    //        Config.makePopulation = PsoRunner.make
    //        Config.extra = PsoRunner.extra
    //        Config.calculateNewPopulation = PSOAlgorithm.calculateNewPopulation
    //        Config.applyNewPosition = PSOAlgorithm.applyNewPosition

    Config.normativeUpdate = NormativeUpdate.update2
    Config.topographicUpdate = TopographicUpdate.update2
    Config.psoStrategy = PSOFactory.pso1
    //    Config.evolutionStep = PSOAlgorithm.evolutionStep
    Config.fitness = FitnessFactory.factory(1)
    Config.applyCA = PSOFactory.applySocialFabric
    Config.tieBreakingRule = TieBreakingRules.mfu
  }

  def neighborhoodRestructure(tribe: Neighborhood): Unit = {
    if (tribe.nsk == Config.mThresh) {
      //      logger.info("restructuring!!!")
      val tempIndex = tribe.topologyIndex
      val tBest = tribe.findBestIndividual()
      val gBest = findIntraTribalBest()
      if (tBest.fitnessValue == gBest.fitnessValue) {
        //downgrade
        tribe.topologyIndex += 1
      }
      else {
        //upgrade
        tribe.topologyIndex -= 1
      }
      val newTopology = TopologyFactory.create(tribe.topologyIndex, tribe.getIndividuals.length)
      if (newTopology != null)
        tribe.topology = newTopology
      else
        tribe.topologyIndex = tempIndex
      tribe.nsk = 0
    }
  }

  def logBestSoFar(): Unit = {
    val t = findIntraTribalBest()
    bestSoFar = if (bestSoFar == null) {
      t.copy()
    } else bestSoFar
    if (t.fitnessValue < bestSoFar.fitnessValue) {
      bestSoFar.fitnessValue = t.fitnessValue
      bestSoFar.vector_(t.vector)
    }
    MyLogger.logInfo(bestSoFar.fitnessValue)
  }

  def findIntraTribalBest(): Individual = {
    var minValue = Double.MaxValue
    var best: Individual = null
    tribes.foreach(tribe => {
      val temp = tribe.best
      if (temp.fitnessValue < minValue) {
        minValue = temp.fitnessValue
        best = temp
      }
    })
    best
  }

  def unify(): Neighborhood = {
    var all = new ArrayBuffer[Individual]()
    tribes.foreach(tribe => {
      all ++= tribe.getIndividuals
    })
    new Neighborhood(all.toArray, TopologyFactory.create(0, all.size), new CAModule)
  }

}
