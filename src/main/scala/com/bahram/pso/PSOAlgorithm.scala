package com.bahram.pso

import com.bahram.ca._
import com.bahram.socialfabric.Neighborhood
import com.bahram.util.{MyLogger, RandomUtil}
import main.scala.com.bahram.ca.TribalRunner
import org.apache.log4j.Logger

import scala.collection.mutable.ArrayBuffer

object PSOAlgorithm {

  val logger = Logger.getLogger(this.getClass)

  def solve(neighborhood: Array[Neighborhood], fitness: (Array[Double] => Double)): Unit = {

  }

  def solve(neighborhood: Neighborhood, fitness: (Array[Double] => Double)) = {
    var iteration = 1
    while (Config.countFEs <= Config.maxFEs) {
      evolutionStep(iteration, neighborhood, fitness)
      iteration += 1
    }
    MyLogger.close()
    logger.info("Best fitness is :: " + neighborhood.findBestIndividual().fitnessValue)
  }

  def evolutionStep(iteration: Int, neighborhood: Neighborhood, fitness: (Array[Double]) => Double): Unit = {
    calculateNewPopulation(iteration, neighborhood, fitness)
    applyNewPosition(neighborhood, fitness)
    MyLogger.logInfo(neighborhood.findBestIndividual().fitnessValue)
    logger.debug("iteration ::: " + Config.iter)
  }

  def applyNewPosition(neighborhood: Neighborhood, fitness: (Array[Double]) => Double): Unit = {
    neighborhood.getIndividuals.foreach { individual => {
      val particle = individual.asInstanceOf[Particle]
      moveParticles(particle)
      particle.fitnessValue = fitness(particle.vector)
      TribalRunner.checkCount()
      if (particle.bestSoFarFitness > particle.fitnessValue) {
        particle.bestSoFarFitness = particle.fitnessValue
        particle.bestVector_(particle.vector)
        particle.nsk = 0
      }
      else Config.stepUpNsk(particle)
    }
    }
  }

  def moveParticles(particle: Particle): Unit = {
    for (i <- particle.vector.indices) {
      particle.vector(i) = particle.vector(i) + particle.velocity(i)
    }
    checkBoundaries(particle)
  }

  def checkBoundaries(particle: Particle): Unit = {
    for (i <- particle.vector.indices) {
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

  def calculateNewPopulation(iteration: Int, neighborhood: Neighborhood, fitness: (Array[Double]) => Double): Unit = {
    val individuals = neighborhood.getIndividuals
    for (index <- individuals.indices) {
      val particle = individuals(index).asInstanceOf[Particle]
      var best: Particle = neighborhood.findBestIndividual().asInstanceOf[Particle]
      if (Config.secondPhase && particle == best)
        best = TribalRunner.findIntraTribalBest().asInstanceOf[Particle]
      else
        best = neighborhood.getBestOfNeighborhood2(index).asInstanceOf[Particle]
      Config.psoStrategy(particle, best.vector) //calculate the new position of a particle
    }
  }

  def calculateNewPopulation2(iteration: Int, neighborhood: Neighborhood, fitness: (Array[Double]) => Double): Unit = {
    val individuals = neighborhood.getIndividuals
    for (index <- individuals.indices) {
      val particle = individuals(index).asInstanceOf[Particle]
      val best: Particle = neighborhood.findBestIndividual().asInstanceOf[Particle]
      val velocity = new Array[Double](particle.vector.length)
      for (i <- velocity.indices) {
        for (j <- particle.neighbors) {
          velocity(i) += individuals(j).vector(i) - particle.vector(i)
        }
      }
      if (Config.secondPhase && particle == best) {
        TribalRunner.tribes.foreach(tribe => {
          for (i <- velocity.indices) {
            velocity(i) += tribe.best.vector(i) - particle.vector(i)
          }
        })
      }

      val dimSize = particle.vector.length
      for (d <- 0 until dimSize) {
        particle.velocity(d) = 0.6 * particle.velocity(d) + (2.0 * RandomUtil.nextDouble()) * velocity(d)
      }
    }
  }

  def calculateNewPopulation3(iteration: Int, neighborhood: Neighborhood, fitness: (Array[Double]) => Double): Unit = {
    val individuals = neighborhood.getIndividuals
    for (index <- individuals.indices) {
      val particle = individuals(index).asInstanceOf[Particle]
      val best: Particle = neighborhood.findBestIndividual().asInstanceOf[Particle]
      val vector = new ArrayBuffer[Double](particle.vector.length)
      if (Config.secondPhase && best == particle) {
        val best2 = TribalRunner.findIntraTribalBest().asInstanceOf[Particle]
        for (i <- vector.indices) {
          val mean = (particle.vector(i) + best2.vector(i)) / 2
          val stdDev = Math.abs(particle.vector(i) - best2.vector(i))
          vector(i) = RandomUtil.nextGaussian(mean, stdDev)
        }
      }
      else {
        for (i <- vector.indices) {
          val mean = (particle.vector(i) + best.vector(i)) / 2
          val stdDev = Math.abs(particle.vector(i) - best.vector(i))
          vector(i) = RandomUtil.nextGaussian(mean, stdDev)
        }
      }
      for (i <- vector.indices) {
        particle.vector(i) = vector(i)
      }
    }
  }

  def applyNewPosition3(neighborhood: Neighborhood, fitness: (Array[Double]) => Double): Unit = {
    neighborhood.getIndividuals.foreach { individual => {
      val particle = individual.asInstanceOf[Particle]
      checkBoundaries(particle)
      particle.fitnessValue = fitness(particle.vector)
      TribalRunner.checkCount()
      if (particle.bestSoFarFitness > particle.fitnessValue) {
        particle.bestSoFarFitness = particle.fitnessValue
        particle.bestVector_(particle.vector)
        particle.nsk = 0
      }
      else Config.stepUpNsk(particle)
    }
    }
  }
}