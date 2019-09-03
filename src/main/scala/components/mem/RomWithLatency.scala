package components.mem

import java.nio.file.{Files, Paths}
import spinal.core._

class RomWithLatency(latency: Int, size: Int, wordSize: Int, content: Option[String]) extends Component {
  assert(latency >= 3, "Latency should be over 2 clock cycles at least.")
  val mem = content match {
    case Some(path) => {
      val bytes = Files.readAllBytes(Paths.get(path)).map(x => B(x, 8 bits))
      new Mem(Bits(wordSize bits), size) init (bytes)
    }
    case None => new Mem(Bits(wordSize bits), size)
  }

  val io = new Bundle {
    val address = in UInt(log2Up(size) bits)
    val data = out Bits(wordSize bits)
  }

  val dataReg = Reg(Bits(wordSize bits))
  val dataOutReg = (0 until latency - 2).foldLeft(dataReg) {(a, _) => RegNext(a)}

  dataReg := mem.readSync(
    enable = True,
    address = io.address
  )

  io.data := dataOutReg
}
