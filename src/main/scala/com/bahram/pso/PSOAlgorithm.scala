package com.bahram.pso

import com.bahram.ca._
import com.bahram.socialfabric.Neighborhood
import com.bahram.util.{MyLogger, PhaseChange}
import main.scala.com.bahram.ca.TribalRunner
import org.apache.log4j.Logger

object PSOAlgorithm {

  val logger = Logger.getLogger(this.getClass)

  def solve(neighborhood: Array[Neighborhood], fitness: (Array[Double] => Double)): Unit = {

  }

  def solve(neighborhood: Neighborhood, fitness: (Array[Double] => Double)) = {
    while (Config.countFEs <= Config.maxFEs) {
      evolutionStep(neighborhood, fitness)
    }
    MyLogger.close()
    logger.info("Best fitness is :: " + neighborhood.findBestIndividual().fitnessValue)
  }

  def evolutionStep(neighborhood: Neighborhood, fitness: (Array[Double]) => Double): Unit = {
    calculateNewPopulation(neighborhood, fitness)
    applyNewPosition(neighborhood, fitness)
    MyLogger.logInfo(neighborhood.findBestIndividual().fitnessValue)
    logger.debug("iteration ::: " + Config.iter)
  }

  def applyNewPosition(neighborhood: Neighborhood, fitness: (Array[Double]) => Double): Unit = {
    neighborhood.getIndividuals.foreach { individual => {
      val particle = individual.asInstanceOf[Particle]
      moveParticles(particle)
      particle.fitnessValue = fitness(particle.vector)
      Config.countFEs += 1
      MyLogger.checkPrint()
      PhaseChange.checkChange()
      if (particle.bestFitness > particle.fitnessValue) {
        particle.bestFitness = particle.fitnessValue
        particle.bestVector_(particle.vector)
      }
    }
    }
    //    PSOFactory.applyNormalCA(Config.iter, neighborhood, fitness)
  }

  def moveParticles(particle: Particle): Unit = {
    for (i <- particle.vector.indices) {
      particle.vector(i) = particle.vector(i) + particle.velocity(i)
      if (particle.vector(i) > 100) {
        particle.vector(i) = 100
        particle.velocity(i) = 0
      }
      if (particle.vector(i) < -100) {
        particle.vector(i) = -100
        particle.velocity(i) = 0
      }

    }
  }

  def calculateNewPopulation(neighborhood: Neighborhood, fitness: (Array[Double]) => Double): Unit = {
    val individuals = neighborhood.getIndividuals
    for (index <- individuals.indices) {
      val particle = individuals(index).asInstanceOf[Particle]
      var best: Particle = neighborhood.findBestIndividual().asInstanceOf[Particle]
      if (Config.secondPhase && particle == best)
        best = TribalRunner.findIntraTribalBest().asInstanceOf[Particle]
      else
        best = neighborhood.getBestOfNeighborhood(index).asInstanceOf[Particle]
      Config.psoStrategy(particle, best) //calculate the new position of a particle
    }
  }

}