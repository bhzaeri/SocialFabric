package cec2015

object FitnessFactory {

  val testbed = new CEC15Problems("")

  def factory(number: Int): (Array[Double] => Double) = {
    func1(number)
  }

  def func1(func_num: Int)(vector: Array[Double]): Double = {
//    cigar_bent(vector)
    testbed.eval(vector, vector.length, 1, func_num)(0)
  }

  def cigar_bent(vector: Array[Double]): Double = {
    var result = vector(0) * vector(0)
    for (i <- 1 until vector.length)
      result += (Math.pow(10, 6) * vector(i) * vector(i))
    result
  }

}