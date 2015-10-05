package dds.demo.flexy

import dds._
import dds.prelude._
import dds.config.DefaultEntities._

object ShapeWriter {
  def main (args: Array[String]): Unit = {
    val topic = Topic[Shape]("Circle")
    val dw = DataWriter[Shape](topic)
    val genRandomInt = (max: Int) => (Math.random()*max).toInt
    while (true) {
      print(".")
      dw.write(new Shape("CYAN", genRandomInt(640), genRandomInt(480), genRandomInt(100)))
      Thread.sleep(1000)
    }
  }
}
