package components

import spinal.core._
import spinal.lib._

class TopLevel extends Component {
  val io = new Bundle {
    val cond0 = in  Bool
    val cond1 = in  Bool
    val flag  = out Bool
    val state = out UInt(8 bits)
  }
  val counter = Reg(UInt(8 bits)) init(0)

  when(io.cond0){
    counter := counter + 1
  }

  io.state := counter
  io.flag  := (counter === 0) | io.cond1
}

object TopLevelVerilog {
  def main(args: Array[String]) {
    SpinalConfig(
      mode = Verilog,
      targetDirectory="out"
    ).generate(new TopLevel)
  }
}
