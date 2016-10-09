package com.bahram.ca

import com.bahram.socialfabric.{Individual, Neighborhood}
import com.bahram.util.RandomUtil

import scala.collection.mutable
import scala.util.control.Breaks

/**
  * Created by zaeri on 03/08/16.
  */
class CAModule {
  val knowledgeSources: Map[KSEnum.Value, KnowledgeSource] = Map(KSEnum.SITUATIONAL -> new Situational, KSEnum.NORMATIVE -> new Normative, KSEnum.TOPOGRAPHIC -> new Topographic)

  def update(neighborhood: Neighborhood, mergeOutput: Boolean, fitness: (Array[Double] => Double)): Array[Individual] = {
    val population = neighborhood.getIndividuals
    val offSprings = update2(neighborhood, mergeOutput, fitness)

    val length = population.length
    val population2 = if (mergeOutput) population ++ offSprings else offSprings
    scala.util.Sorting.stableSort[Individual](population2, Config.compareAsc _)
    //    population2.slice(0, length)
    population2
  }

  private def update2(neighborhood: Neighborhood, mergeOutput: Boolean, fitness: (Array[Double] => Double)): Array[Individual] = {
    val population = neighborhood.getIndividuals
    val temp = new mutable.HashMap[KSEnum.Value, Double]
    var sum = 0.0
    population.foreach(p => {
      var t = 1 / p.fitnessValue
      var tt = 0.0
      assert(p.ksType != null)
      if (temp.keySet.contains(p.ksType))
        tt = temp(p.ksType)
      temp += (p.ksType -> (tt + t))
      sum += t
    })
    assert(sum != 0.0)
    val r = RandomUtil.nextDouble() * sum
    var t = 0.0
    val bb = new Breaks()
    var ks: KSEnum.Value = null
    bb.breakable {
      temp.foreach((pair) => {
        if (r >= t && r <= t + pair._2) {
          ks = pair._1
          bb.break
        }
        else
          t += pair._2
      })
    }
    assert(ks != null)
    neighborhood.ksCount(ks) += 1L
    //    val selected = neighborhood.individuals_ filter (p => p.ksType == ks)
    knowledgeSources(ks).update(population, mergeOutput, fitness)
  }
}

object KSEnum extends Enumeration {
  val SITUATIONAL, NORMATIVE, TOPOGRAPHIC = Value
}