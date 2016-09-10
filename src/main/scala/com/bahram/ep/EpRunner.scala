package com.bahram.ep

import cec2015.FitnessFactory
import com.bahram.ca._
import com.bahram.pso.PSOFactory
import com.bahram.socialfabric.topology.TopologyFactory
import com.bahram.socialfabric.{Individual, Neighborhood}
import com.bahram.util.RandomUtil

/**
  * Created by zaeri on 01/08/16.
  */
object EpRunner {

  def main(args: Array[String]): Unit = {
    solve(10)
  }

  def solve(popSize: Int) = {
    val fitness = FitnessFactory.factory(1)
    val population = PSOFactory.population(make(popSize), extra, fitness)
    val topology = TopologyFactory.create(Config.topologyType, popSize)
    val neigh = new Neighborhood(population, topology, null)
    EvolutionaryProgramming.search(neigh, fitness)
  }

  def make(size: Int): Array[Individual] = {
    val result = new Array[Individual](size)
    for (i <- 0.until(size)) {
      result(i) = new EpIndividual(TopologyFactory.dimension)
    }
    result
  }

  def extra(individual: Individual): Unit = {
    val p = individual.asInstanceOf[EpIndividual]
    val dim = p.vector.length
    for (i <- 0 until dim) {
      p.strategy(i) = RandomUtil.nextDouble() * 10
    }
  }

}
