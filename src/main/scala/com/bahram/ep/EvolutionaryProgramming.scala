package com.bahram.ep

import com.bahram.ca._
import com.bahram.socialfabric.{Individual, Neighborhood}
import com.bahram.util.{MyLogger, RandomUtil}

import scala.util.Random

/**
  * Created by zaeri on 31/07/16.
  */
object EvolutionaryProgramming {

  def makeStrategy(popSize: Int, min: Int, max: Int): Array[Double] = {
    val rand = Random
    val strategy = new Array[Double](popSize)
    for (i <- 0 until popSize) {
      strategy(i) = rand.nextDouble() * (max - min)
    }
    strategy
  }

  def search(neighborhood: Neighborhood, fitness: Array[Double] => Double): Unit = {
    var iteration = 1
    while (Config.countFEs <= Config.maxFEs) {
      evolutionStep(iteration, neighborhood, fitness)
      iteration += 1
    }
    MyLogger.close()
  }

  def evolutionStep(iteration: Int, neighborhood: Neighborhood, fitness: (Array[Double]) => Double): Unit = {
    calculateNewPopulation(iteration, neighborhood, fitness)
    applyNewPositions(neighborhood, fitness)
  }

  def applyNewPositions(neighborhood: Neighborhood, fitness: (Array[Double]) => Double): Unit = {
    val popSize = neighborhood.getIndividuals.length
    neighborhood.getIndividuals.foreach(c => tournament(c, neighborhood.getIndividuals, Config.boutSize))
    neighborhood.setIndividuals(neighborhood.getIndividuals.sortWith((a, b) => a.asInstanceOf[EpIndividual].wins > b.asInstanceOf[EpIndividual].wins))
    neighborhood.setIndividuals(neighborhood.getIndividuals.slice(0, popSize))
    MyLogger.logInfo(neighborhood.findBestIndividual().fitnessValue)
    //    PSOFactory.applyNormalCA(Config.iter, neighborhood, fitness)
  }

  def tournament(c: Individual, population: Array[Individual], boutSize: Int) = {
    val candidate = c.asInstanceOf[EpIndividual]
    candidate.wins = 0
    for (i <- 0 until boutSize) {
      val other = population(RandomUtil.nextInt(population.length))
      if (candidate.fitnessValue < other.fitnessValue)
        candidate.wins += 1
    }
  }

  def calculateNewPopulation(iteration: Int, neighborhood: Neighborhood, fitness: (Array[Double]) => Double): Unit = {
    val popSize = neighborhood.getIndividuals.length
    //neighborhood.cAModule.update2(neighborhood, fitness) //new Array[EpIndividual](popSize)
    Config.epGenerateStrategy(iteration, neighborhood, false, fitness)
    //    for (i <- 0 until popSize) {
    //      //      children(i) = mutate(neighborhood.getIndividuals(i)).asInstanceOf[EpIndividual]
    //      children(i).fitnessValue = fitness(children(i).vector)
    //      Config.countFEs += 1
    //      MyLogger.checkPrint()
    //      PhaseChange.checkChange()
    //    }
    //    children = children.sortWith((a: Individual, b: Individual) => a.fitnessValue < b.fitnessValue)
    //    neighborhood.setIndividuals(children.++(neighborhood.getIndividuals))
  }

  def mutate(c: Individual): Individual = {
    val candidate = c.asInstanceOf[EpIndividual]
    val child = new EpIndividual(candidate.vector.length)
    child.ksType = candidate.ksType
    for (i <- candidate.vector.indices) {
      val s_old = candidate.strategy(i)
      val v_old = candidate.vector(i)
      var v = v_old + s_old * random_gaussian()
      if (v < -100)
        v = -100
      if (v > 100)
        v = 100
      child.vector(i) = v
      child.strategy(i) = s_old + random_gaussian() * Math.sqrt(Math.abs(s_old))
    }
    child
  }

  def random_gaussian(): Double = {
    val mean = 0.0
    val stdev = 1.0
    var u1, u2, w = 0.0
    val rand = RandomUtil
    do {
      u1 = 2 * rand.nextDouble() - 1
      u2 = 2 * rand.nextDouble() - 1
      w = u1 * u1 + u2 * u2
    } while (w >= 1)
    w = Math.sqrt((-2.0 * Math.log(w)) / w)
    mean + (u2 * w) * stdev
  }
}