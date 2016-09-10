package com.bahram.util

import com.bahram.ca._
import com.bahram.socialfabric.topology.TopologyFactory
import main.scala.com.bahram.ca.TribalRunner

/**
  * Created by zaeri on 24/08/16.
  */
object PhaseChange {
  val phases = Array(0.2, 0.5)
  var index = 0

  def checkChange() = {
    if (Config.countFEs > 30000) {
      var ggg = 0
    }
    if (index < phases.length && Config.countFEs == (phases(index) * Config.maxFEs).asInstanceOf[Int]) {
      Config.secondPhase = !Config.secondPhase
      if (Config.secondPhase) {
        TribalRunner.tribes.foreach(tribe => {
          tribe.topology = TopologyFactory.create(Config.topologyType, TribalRunner.popSize)
        })
      }
      index += 1
      if (index == 2)
        Config.thirdPhase = true
    }
  }
}

