package com.bahram.ca

import com.bahram.socialfabric.Individual
import com.bahram.util.RandomUtil
import main.scala.com.bahram.ca.TribalRunner

/**
  * Created by zaeri on 02/08/16.
  */
class Situational extends KnowledgeSource {
  var best: Individual = _

  override def update(population: Array[Individual], fitness: Array[Double] => Double): Array[Individual] = {
    scala.util.Sorting.stableSort[Individual](population, Config.compareAsc _)
    val l = population.length
    if (best == null || population(0).fitnessValue < best.fitnessValue) {
      best = population(0)
    }

    val offSprings = new Array[Individual](l)
    for (i <- 0 until l) {
      offSprings(i) = population(i).copy()
      val t = offSprings(i)
      for (j <- t.vector.indices) {
        if (population(i).vector(j) < best.vector(j))
          t.vector(j) = population(i).vector(j) + (best.vector(j) - population(i).vector(j)) * RandomUtil.nextDouble()
        else
          t.vector(j) = population(i).vector(j) - (population(i).vector(j) - best.vector(j)) * RandomUtil.nextDouble()

        if (t.vector(j) < Config.lowerBound)
          t.vector(j) = Config.lowerBound
        if (t.vector(j) > Config.upperBound)
          t.vector(j) = Config.upperBound

      }
      t.fitnessValue = fitness(t.vector)
      TribalRunner.checkCount()
      t.ksType = KSEnum.SITUATIONAL
    }

    offSprings
  }
}
