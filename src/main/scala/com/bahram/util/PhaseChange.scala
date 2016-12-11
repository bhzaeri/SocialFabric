package com.bahram.util

import com.bahram.ca._
import com.bahram.socialfabric.topology.TopologyFactory
import main.scala.com.bahram.ca.TribalRunner

/**
  * Created by zaeri on 24/08/16.
  */
object PhaseChange {
  val phases = Array(0.2, 0.5)

  def checkChange() = {
    if (Config.phaseIndex < phases.length && Config.countFEs == (phases(Config.phaseIndex) * Config.maxFEs).asInstanceOf[Int]) {
      Config.secondPhase = !Config.secondPhase
      if (Config.secondPhase) {
        Config.stepUpNsk = (i) => {
          i.nsk += 1
        }
        TribalRunner.tribes.foreach(tribe => {
          tribe.setTopology(TopologyFactory.create(Config.topologyType, Config.populationSize))
        })
      }
      Config.phaseIndex += 1
      if (Config.phaseIndex == 2)
        Config.thirdPhase = true
    }
  }
}

