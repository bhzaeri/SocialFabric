package com.bahram.pso

import com.bahram.ca.{Config, KSEnum}
import com.bahram.socialfabric.topology.{GlobalTopology, RingTopology, TopologyFactory}
import com.bahram.socialfabric.{Individual, Neighborhood}
import com.bahram.util.RandomUtil
import main.scala.com.bahram.ca.TribalRunner
import main.scala.com.bahram.socialfabric.TieBreakingRules
import org.apache.log4j.Logger

import scala.collection.mutable

object PSOFactory {

  val logger = Logger.getLogger(this.getClass)

  def pso1(self: Particle, best: Particle) = {
    val dimSize = self.vector.length
    val c1 = 2.0
    val c2 = 2.0
    for (d <- 0 until dimSize) {
      val r1 = RandomUtil.nextDouble()
      val r2 = RandomUtil.nextDouble()
      self.velocity(d) = 0.6 * self.velocity(d) + r1 * c1 * (self.bestVector(d) - self.vector(d)) + r2 * c2 * (best.vector(d) - self.vector(d))
    }
  }

  def applySocialFabric(iteration: Int, neighborhood: Neighborhood, fitness: (Array[Double] => Double)): Boolean = {
    if (iteration % Config.wSize == 0) {
      if (Config.secondPhase || Config.thirdPhase) {
        val individuals = neighborhood.getIndividuals
        for (i <- individuals.indices) {
          val neighbors = neighborhood.topology.getNeighbors(i)
          val map = new mutable.HashMap[KSEnum.Value, Int]()

          if (neighborhood.topology.isInstanceOf[RingTopology]) {
            val l = individuals.length
            val ks1 = individuals((i - 1 + l) % l).ksType
            val ks2 = individuals((i + 1) % l).ksType
            var newKS: KSEnum.Value = null
            if (ks1 == ks2) {
              newKS = Config.tieBreakingRule(neighborhood, individuals(i))
            }
            else {
              newKS = ks1
            }
            individuals(i).nextKsType = newKS
          } else {
            map(individuals(i).ksType) = 1
            for (j <- neighbors.indices) {
              if (map.keySet.contains(individuals(j).ksType))
                map(individuals(j).ksType) += 1
              else map(individuals(j).ksType) = 1
            }


            if (TribalRunner.tribes.length > 1 && individuals(i) == neighborhood.best) {
              val tribes = TribalRunner.tribes
              tribes.foreach(tribe => {
                if (map.keySet.contains(tribe.best.ksType))
                  map(tribe.best.ksType) += 1
                else map(tribe.best.ksType) = 1
              })
            }

            val sorted = map.toSeq.sortWith((a, b) => a._2 > b._2)
            if (sorted.length == 1 && neighborhood.topology.isInstanceOf[GlobalTopology]) {
              individuals(i).nextKsType = TieBreakingRules.random(neighborhood, individuals(i))
            } else if (sorted.length == 1 || sorted.head._2 > sorted(1)._2)
              individuals(i).nextKsType = sorted.head._1
            else
              individuals(i).nextKsType = Config.tieBreakingRule(neighborhood, individuals(i))
          }
          assert(individuals(i).nextKsType != null)
        }
        individuals.foreach(i => {
          i.ksType = i.nextKsType
          i.nextKsType = null
        })
      }
      applyNormalCA(iteration, neighborhood, fitness)
      return true
    }
    false
  }

  def applyNormalCA(iteration: Int, neighborhood: Neighborhood, fitness: (Array[Double] => Double)): Boolean = {
    if (iteration % Config.wSize == 0) {
      val newPop = neighborhood.cAModule.update(neighborhood, fitness)
      neighborhood.setIndividuals(newPop)
//      assert(neighborhood.getIndividuals.length % 2 == 0)
      return true
    }
    false
  }


  def population(result: Array[Individual], extra: (Individual => Unit), fitness: (Array[Double] => Double)): Array[Individual] = {
    val rand = RandomUtil
    val size = result.length
    for (i <- 0 until size) {
      val p = result(i) // new Particle(dimension);
      val vector = new Array[Double](TopologyFactory.dimension)
      for (j <- vector.indices) {
        vector(j) = rand.nextDouble() * Config.upperBound + Config.lowerBound
      }
      p.vector_(vector)
      p.fitnessValue = fitness(vector)
      extra(p)
      p.ksType = KSEnum.values.toList(RandomUtil.nextInt(KSEnum.values.size))
    }
    var j = 0
    for (i <- 0 until size) {
      result(i).ksType = KSEnum.values.toList(j)
      j = (j + 1) % KSEnum.values.size
    }
    result
  }
}