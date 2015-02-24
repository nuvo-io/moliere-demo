package dds.demo.shapes

import dds._
import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}
import dds.demo.play.Coord2D
import dds.demo.play.Line2D
import dds.demo.play.Shape2D
import dds.demo.vehicle.VehicleDrawer
import dds.demo.vehicle.VehicleStatus
import dds.prelude._
import dds.demo.play._
import dds.prelude._
import org.omg.dds.demo.ShapeType
import scala.collection.JavaConversions._

object CollisionDetector {

  val colorMap = Map(
    "RED" -> new ColorRGB(0xcc, 0x33, 0x33),
    "GREEN" -> new ColorRGB(0x99, 0xcc, 0x66),
    "BLUE" -> new ColorRGB(0x33, 0x66, 0x99),
    "ORANGE" -> new ColorRGB(0xff, 0x99, 0x33),
    "YELLOW" -> new ColorRGB(0xff, 0xff, 0x66),
    "MAGENTA" ->new ColorRGB(0xcc, 0x99, 0xcc),
    "CYAN" -> new ColorRGB(0x99, 0xcc, 0xff),
    "GRAY" -> new ColorRGB(0x99, 0x99, 0x99)
  )

  def distance (a: ShapeType, b: ShapeType) =
    math.sqrt(math.pow(a.x - b.x, 2) + math.pow(a.y - b.y, 2)).toInt



  def handleShape[T](minDist: Int,
                     dw: org.omg.dds.pub.DataWriter[Shape2D])(dr: org.omg.dds.sub.DataReader[ShapeType]) = {
    val data = history(dr).map(_.getData).toList

    val collisionList =
      for (d <- data;
           minDistList <- data.filter ((s: ShapeType) => {
             val dist = distance (d, s)
             if ((d.color != s.color) && dist < minDist) true else false
           } ) ) yield (d, minDistList)

    collisionList.foreach (tuple => {
      if (tuple._1.color != tuple._2.color) {

        for (color <- colorMap.get(tuple._1.color)) {
          val of = new Shape2D(tuple._1.color.hashCode,
            100 + tuple._1.color.hashCode,
            30, 30,
            new Coord2D(tuple._1.x + (tuple._1.shapesize / 2).toInt, tuple._1.y + (tuple._1.shapesize / 2).toInt),
            color)
          dw ! of
        }
      }
    })
  }


  def main(args: Array[String]) {

    if (args.length < 1) {
      println("USAGE:\n\tCollisionDetector <minDist> [<in-partition> <out-partition>]")
      sys.exit(1)
    }

    val minDist = args(0).toInt
    val psub= if (args.length > 1) args(1) else ""
    val ppub = if (args.length > 2) args(2) else psub

    println(s"In partition: $psub")
    println(s"Out partition: $ppub")

    val entities = Entities(psub, ppub)



    val lineDW = entities.lineDW
    val ofDW = entities.ofDW

    val detectCollision = handleShape[ShapeType](minDist, ofDW) _

    entities.circleDR.listen {
      case DataAvailable(_) => {
        println('c')
        detectCollision(entities.circleDR)
      }
    }
    entities.squareDR.listen {
      case DataAvailable(_) => {
        println('s')
        detectCollision(entities.squareDR)
      }
    }
    entities.triangleDR.listen {
      case DataAvailable(_) => {
        println('t')
        detectCollision(entities.triangleDR)
      }
    }
  }
}