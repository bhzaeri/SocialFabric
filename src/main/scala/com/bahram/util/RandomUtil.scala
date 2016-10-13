package com.bahram.util

import scala.util.Random

/**
  * Created by zaeri on 20/08/16.
  */
object RandomUtil {

  private var random = new Random(101)

  def main(args: Array[String]): Unit = {
    for (i <- 101 to 105) {
      init(i)
      for (j <- 0 until 10)
        print(nextInt() + " ")
      println()
    }
  }

  def init(seed: Int): Unit = {
    random = new Random(seed)
  }

  def nextInt(): Int = random.nextInt()

  def nextDouble(): Double = random.nextDouble()

  def nextInt(n: Int): Int = random.nextInt(n)

  def nextGaussian(mean: Double, stdDev: Double): Double = random.nextGaussian() * stdDev + mean
}
