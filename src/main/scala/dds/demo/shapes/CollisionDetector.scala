package dds.demo.shapes

import java.util.concurrent.atomic.AtomicLong

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

import com.vortex.demo._
import scala.concurrent.future
import scala.concurrent.ExecutionContext.Implicits.global

object CollisionDetector {


  val lcd   = Topic[LCD]("LCD")
  val lcdDw = DataWriter[LCD](dds.config.DefaultEntities.defaultPub, lcd)

  val led   = Topic[LED]("LED")
  val ledDw = DataWriter[LED](dds.config.DefaultEntities.defaultPub, led)

  val ledOn = new LED(2, true)
  val ledOff = new LED(2, false)
  val ledid: Short   = 2
  val lcdid: Short   = (0x3e00 | 0x0062).toShort
  var collisions = new AtomicLong(0)
  def lcdText(r: Short, c: Short, txt: String): LCD = new LCD(lcdid, r, c, txt)


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
        ledDw.write(ledOn)
        for (color <- colorMap.get(tuple._1.color)) {
          val of = new Shape2D(tuple._1.color.hashCode,
            100 + tuple._1.color.hashCode,
            30, 30,
            new Coord2D(tuple._1.x + (tuple._1.shapesize / 2).toInt, tuple._1.y + (tuple._1.shapesize / 2).toInt),
            color)
          dw ! of
          lcdDw.write(lcdText(0, 0, collisions.incrementAndGet() + " collisions"))
        }
      }
      ledDw.write(ledOff)
    })

  }


  def main(args: Array[String]) {

    if (args.length < 1) {
      println("USAGE:\n\tCollisionDetector <minDist> [<in-partition> <out-partition>]")
      sys.exit(1)
    }

    lcdDw.write(lcdText(0, 0, collisions.get() + " collisions"))
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