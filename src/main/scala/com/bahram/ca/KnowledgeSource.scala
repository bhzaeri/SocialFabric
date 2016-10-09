package com.bahram.ca

import com.bahram.socialfabric.Individual

/**
  * Created by zaeri on 02/08/16.
  */
abstract class KnowledgeSource {

  def update(population: Array[Individual], mergeOutput: Boolean, fitness: (Array[Double] => Double)): Array[Individual]

}
