package com.bahram.util

import scala.util.Random

/**
  * Created by zaeri on 20/08/16.
  */
object RandomUtil {

  private val random = new Random(101)

  def nextDouble(): Double = random.nextDouble()

  def nextInt(): Int = random.nextInt()

  def nextInt(n: Int): Int = random.nextInt(n)

  def nextGaussian(mean: Double, stdDev: Double): Double = random.nextGaussian() * stdDev + mean
}
