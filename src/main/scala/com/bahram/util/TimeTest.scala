package main.scala.com.bahram.util

/**
  * Created by zaeri on 12/10/16.
  */
object TimeTest {
  def main(args: Array[String]): Unit = {
    var t1 = System.currentTimeMillis()
    timeTest
    var t2 = System.currentTimeMillis()
    println(t2 - t1)
  }

  def timeTest: Unit = {
    for (i <- 1 to 1000000) {
      var x = 0.55 + i
      x = x + x; x = x / 2; x = x * x; x = Math.sqrt(x); x = Math.log(x); x = Math.exp(x); x = x / (x + 2);
    }
  }
}
