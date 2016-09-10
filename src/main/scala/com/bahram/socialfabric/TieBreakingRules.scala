package main.scala.com.bahram.socialfabric

import com.bahram.ca.KSEnum
import com.bahram.socialfabric.{Individual, Neighborhood}
import com.bahram.util.RandomUtil

/**
  * Created by zaeri on 02/09/16.
  */
object TieBreakingRules {

  def mfu(neighborhood: Neighborhood, individual: Individual): KSEnum.Value = {
    val map = neighborhood.ksCount
    var mfuKS = 0L
    var ks: KSEnum.Value = null
    map.foreach((kv) => {
      if (mfuKS <= kv._2) {
        mfuKS = kv._2
        ks = kv._1
      }
    })
    assert(ks!=null)
    ks
  }

  def lfu(neighborhood: Neighborhood, individual: Individual): KSEnum.Value = {
    val map = neighborhood.ksCount
    var lfuKS = Long.MaxValue
    var ks: KSEnum.Value = null
    map.foreach(kv => {
      if (lfuKS >= kv._2) {
        lfuKS = kv._2
        ks = kv._1
      }
    })
    ks
  }

  def lastUsed(neighborhood: Neighborhood, individual: Individual): KSEnum.Value = {
    individual.ksType
  }

  def random(neighborhood: Neighborhood, individual: Individual): KSEnum.Value = {
    val index = RandomUtil.nextInt(KSEnum.values.size)
    KSEnum.values.toSeq(index)
  }
}