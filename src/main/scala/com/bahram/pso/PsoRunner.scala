package com.bahram.pso

import cec2015.FitnessFactory
import com.bahram.ca._
import com.bahram.socialfabric.topology.TopologyFactory
import com.bahram.socialfabric.{Individual, Neighborhood}

object PsoRunner {

  def main(args: Array[String]) = {
    solve(10)
  }

  def solve(popSize: Int) = {
    val fitness = FitnessFactory.factory(1)
    val population: Array[Individual] = PSOFactory.population(make(popSize), extra, fitness)

    val topology = TopologyFactory.create(Config.topologyType, popSize)
    val neigh = new Neighborhood(population, topology, null)
    PSOAlgorithm.solve(neigh, fitness)
  }

  def extra(individual: Individual): Unit = {
    val p: Particle = individual.asInstanceOf[Particle]
    p.bestVector_(p.vector)
    p.bestSoFarFitness = p.fitnessValue
  }

  def make(size: Int): Array[Individual] = {
    val result = new Array[Individual](size)
    for (i <- 0.until(size)) {
      result(i) = new Particle(Config.dimension)
    }
    result
  }
}