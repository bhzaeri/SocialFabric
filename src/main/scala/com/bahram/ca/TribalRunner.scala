package main.scala.com.bahram.ca

import com.bahram.ca._
import com.bahram.pso.PSOFactory
import com.bahram.socialfabric.topology.TopologyFactory
import com.bahram.socialfabric.{Individual, Neighborhood}
import com.bahram.util.{MyLogger, PhaseChange, RandomUtil}
import org.apache.log4j.Logger

import scala.collection.mutable.ArrayBuffer

/**
  * Created by zaeri on 24/08/16.
  */
object TribalRunner {

  var logger = Logger.getLogger(TribalRunner.getClass)
  var tribes: Array[Neighborhood] = _
  var bestSoFar: Individual = _

  def main(args: Array[String]): Unit = {
    for (i <- 1 to 15) {
      run(i)
      Config.filePrinter.write("\n")
    }
    MyLogger.close()
  }

  def run(funcIndex: Int): Unit = {
    configure(funcIndex)
    tribes = Array.fill[Neighborhood](Config.tribeNumber) {
      new Neighborhood(PSOFactory.population(Config.makePopulation(Config.populationSize), Config.extra, Config.fitness),
        TopologyFactory.create(Config.topologyType, Config.populationSize), new CAModule)
    }

    while (Config.countFEs <= Config.maxFEs) {
      tribes.foreach(tribe => {
        Config.calculateNewPopulation(Config.iter, tribe, Config.fitness)
      })

      tribes.foreach(tribe => {
        Config.applyNewPosition(tribe, Config.fitness)
      })
      Config.iter += 1

      tribes.foreach(tribe => {
        tribe.findBestIndividual()
      })

      logBestSoFar()
      //Macroscopic neighborhood restructuring
      //checking the conditions for stagnation
      if (Config.neighborhoodRestructuring != null)
        Config.neighborhoodRestructuring()

      tribes.foreach(tribe => {
        if (Config.applyCA != null) {
          Config.applyCA(Config.iter, tribe, true, Config.fitness)
        }
      })

      if (Config.thirdPhase && tribes.length > 1) {
        tribes = Array.fill[Neighborhood](1) {
          unify()
        }
        logger.info("unifiedDDDDDDDDDDDDDDDDDDDD")
      }

      logger.info(Config.iter + "   " + Config.countFEs)
    }
  }

  def configure(funcIndex: Int) = {
    Configure.sfep(funcIndex)
  }

  def logBestSoFar(): Unit = {
    val t = findIntraTribalBest()
    bestSoFar = if (bestSoFar == null) t.copy() else bestSoFar
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
    Config.populationSize *= Config.tribeNumber
    tribes.foreach(tribe => {
      all ++= tribe.getIndividuals
    })

    Config.resetNeighborhood(all)

    new Neighborhood(all.toArray, TopologyFactory.create(0, all.size), new CAModule)
  }

  def resetNeighborhood(all: ArrayBuffer[Individual]): Unit = {
    all.foreach(i => {
      i.resetNeighborhood(Config.populationSize)
    })
  }

  def neighborhoodRestructuring2(): Unit = {
    if (Config.secondPhase || Config.thirdPhase) {
      tribes.foreach(tribe => {
        neighborhoodRestructuring2(tribe)
      })
    }
  }

  def neighborhoodRestructuring2(tribe: Neighborhood): Unit = {
    for (index <- tribe.individuals_.indices) {
      val i = tribe.individuals_(index)
      if (i.nsk == Config.mThresh) {
        val best = tribe.getBestOfNeighborhood2(index)
        if (i.fitnessValue > best.fitnessValue) {
          //upgrade
          if (i.otherNeighbors.nonEmpty) {
            val ii = RandomUtil.nextInt(i.otherNeighbors.length)
            val temp = i.otherNeighbors(ii)
            i.neighbors += temp
            i.otherNeighbors.remove(ii)
          }
        }
        else if (i.neighbors.length > 1) {
          //downgrade
          val ii = RandomUtil.nextInt(i.neighbors.length)
          val temp = i.neighbors(ii)
          i.otherNeighbors += temp
          i.neighbors.remove(ii)
        }
        i.nsk = 0
      }
    }
  }

  def neighborhoodRestructuring1(): Unit = {
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
        tribe.setTopology(newTopology)
      else
        tribe.topologyIndex = tempIndex
      tribe.nsk = 0
    }
  }

  def checkCount() = {
    Config.countFEs += 1
    MyLogger.checkPrint()
    PhaseChange.checkChange()
  }

}
