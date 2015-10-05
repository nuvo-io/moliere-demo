package dds.demo.flexy

import dds._
import dds.prelude._
import dds.config.DefaultEntities._
import scala.collection.JavaConversions._


object ShapeReader {
  def main (args: Array[String]): Unit = {
    val topic = Topic[Shape]("Circle")
    val dr = DataReader[Shape](topic)

    dr.listen {
      case DataAvailable(_) => {
        dr.read()
          .filter(_.getData != null)
          .map(s => s.getData())
          .foreach(p => println("(" + p.color + ", " + p.x + ", " + p.y + ", " + p.size + ")"))
      }
    }
  }
}
