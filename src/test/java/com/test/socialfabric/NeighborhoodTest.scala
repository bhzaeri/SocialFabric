package com.test.socialfabric

import cec2015.FitnessFactory
import com.bahram.ca.{Config, Situational}
import com.bahram.pso.{PSOFactory, PsoRunner}
import com.bahram.socialfabric.Neighborhood
import com.bahram.socialfabric.topology.TopologyFactory
import org.scalatest.{BeforeAndAfter, FunSuite}

class NeighborhoodTest extends FunSuite with BeforeAndAfter {

  test("Neighborhood") {
    val size = 100;
    val fitness = FitnessFactory.factory(2)
    val population = PSOFactory.population(PsoRunner.make(100), PsoRunner.extra, fitness)
    val topology = TopologyFactory.create(Config.topologyType, size)
    val neigh = new Neighborhood(population, topology, null)
    neigh.getBestOfNeighborhood(50)
  }

  test("a1") {
    val size = 100;
    val fitness = FitnessFactory.factory(2)
    var population = PSOFactory.population(PsoRunner.make(100), PsoRunner.extra, fitness)
    population(0).fitnessValue = 100.0
    population(0).fitnessValue = 150.0
    population = new Situational().update(population, true, fitness)
    assert(population.length == size)
    assert(population(0).fitnessValue > population(1).fitnessValue)
  }

  test("a2") {
    val size = 100;
    val fitness = FitnessFactory.factory(2)
    val population = PSOFactory.population(PsoRunner.make(100), PsoRunner.extra, fitness)
    new Situational().update(population, true, fitness)
  }

  test("funcion") {
    val r = FitnessFactory.factory(10)(Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
    println(r)
  }
}