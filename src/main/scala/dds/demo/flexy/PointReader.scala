package dds.demo.flexy

import dds._
import dds.prelude._
import dds.config.DefaultEntities._
import scala.collection.JavaConversions._

case class CPoint (x: Int, y: Int)

object PointReader {
  def main (args: Array[String]): Unit = {
    val topic = Topic[Point]("Circle")
    val dr = DataReader[Point](topic)
    dr.listen {
      case DataAvailable (_) => dr.read().filter(_.getData != null).map(s => s.getData()).foreach(p => println("(" + p.x + ", " + p.y + ")"))
    }
  }
}

