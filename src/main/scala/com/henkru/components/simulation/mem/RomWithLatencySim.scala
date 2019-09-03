package com.henkru.components.simulation.mem

import spinal.core._
import spinal.sim._
import spinal.core.sim._

import com.henkru.components.mem.RomWithLatency

object RomWithLatencySim {
  def main(args: Array[String]) {
    val freq = 50 MHz
    val period_ns = ((1 GHz) / freq).toLong
    SimConfig
      .withWave
      .doSim(new RomWithLatency(3, 512, 8, None)){ dut =>

        //Fork a process to generate the reset and the clock on the dut
        dut.clockDomain.forkStimulus(period = period_ns)
        dut.io.address #= 0x08

        for (clk <- 0 until 100) {
          dut.clockDomain.waitRisingEdge()

          if (clk % 30 == 0) {
            dut.io.address #= dut.io.address.toInt + 1
          }
        }
      }
  }
}
