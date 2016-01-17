package dds.demo.shapes

import dds._
import dds.prelude._
import org.omg.dds.demo.ShapeType
import dds.config.DefaultEntities._
import scala.collection.JavaConversions._

object ShapeLogger {
  def show(s: ShapeType): String = {
    s.color + " " + s.x + " " + s.y
  }
  def main(args: Array[String]): Unit = {
    if (args.length > 0) {
      val topicName = args(0)

      val entities = Entities("")
      val dr = entities.readerMap(topicName)

      dr.listen {
        case DataAvailable(_) => dr.read().filter(s => s.getData != null).map(s => s.getData).foreach(s => show (s))
      }
    }

  }
}
