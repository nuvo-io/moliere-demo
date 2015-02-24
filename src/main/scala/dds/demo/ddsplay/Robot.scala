package dds.demo.ddsplay

import dds._
import dds.config.DefaultEntities._
import scala.annotation.tailrec
import dds.demo.geometry._
import dds.demo.play._
import scala.language.implicitConversions

class Robot (val pos: Vector2D, val motion: Vector2D, val region: Region2D, val width: Int, val height: Int) {
  val wheelSize = ((width/3).toInt, (height/3).toInt)
  val midPoint = ((width/2).toInt, (height/2).toInt)

  val northWheel = ( new Vector2D(pos.x + midPoint._1 - (width / 6).toInt, pos.y - wheelSize._2),
                      wheelSize._1, wheelSize._2)

  val southWheel = (new Vector2D(pos.x + midPoint._1 - (width / 6).toInt, pos.y + height),
                      wheelSize._1, wheelSize._2)

  val eastWheel = ( new Vector2D(pos.x + width, pos.y + midPoint._2 - (height/ 6).toInt),
                      wheelSize._1, wheelSize._2)

  val westWheel = ( new Vector2D(pos.x - wheelSize._1, pos.y + midPoint._2 - (height/6).toInt ),
                      wheelSize._1, wheelSize._2)

  val bound = (v: Vector2D) => {
    new Region2D(
      v.x - wheelSize._1,
      v.y -  wheelSize._2,
      width + 2*wheelSize._1,
       height + 2*wheelSize._2
    )
  }

}

object BouncingRobot {

  def main(args: Array[String]) {
    if (args.length < 9) {
      println("USAGE\n\tRobot <sid> <red> <green> <blue> <size> <region-x0> <region-y0> <region-width> <region-height>")
      sys.exit(1)
    }
    val shapeFill = Topic[Shape2D]("RectFillShape")
    val shapeContour = Topic[Shape2D]("RectShape")
    val shapeDW = DataWriter[Shape2D](shapeFill)
    val contourDW = DataWriter[Shape2D](shapeContour)

    val w = args(4).toInt
    val h = w
    val sid = args(0).toInt
    val id = 1

    val x0 = args(5).toInt
    val y0 = args(6).toInt

    val robot = new Robot(pos = new Vector2D(x0+(w/3).toInt, y0+(h/3).toInt),
      motion = new Vector2D(4, 2),
      region = new Region2D(x0 , y0,  args(7).toInt, args(8).toInt),
      width = w,
      height = h)

    val color = new ColorRGB(args(1).toShort, args(2).toShort, args(3).toShort)
    val wheelColor = new ColorRGB (66, 66, 66)

    implicit def robotToShape(r: Robot): Shape2D =
      new Shape2D(sid, id, r.width, r.height, new Coord2D(r.pos.x, r.pos.y), color)

    implicit def Coord2DtoVector2D(c: Coord2D) : Vector2D = new Vector2D(c.x, c.y)

    @tailrec
    def simulateAutoRobot(r: Robot): Unit = {
      shapeDW write r
      shapeDW write new Shape2D(sid, id+1, r.northWheel._2,r.northWheel._3, new Coord2D(r.northWheel._1.x, r.northWheel._1.y), wheelColor)
      shapeDW write new Shape2D(sid, id+2, r.southWheel._2,r.southWheel._3, new Coord2D(r.southWheel._1.x, r.southWheel._1.y), wheelColor)
      shapeDW write new Shape2D(sid, id+3, r.eastWheel._2,r.eastWheel._3, new Coord2D(r.eastWheel._1.x, r.eastWheel._1.y), wheelColor)
      shapeDW write new Shape2D(sid, id+4, r.westWheel._2,r.westWheel._3, new Coord2D(r.westWheel._1.x, r.westWheel._1.y), wheelColor)
      contourDW write new Shape2D(sid, id+5, r.region.width, r.region.height, new Coord2D(r.region.x0, r.region.y0), color)

      val rotation = if (math.random > 0.5) Array(Array(-1, 0),Array(0,-1)) else Array(Array(-1, 0),Array(0,1))
      var (bpos, bmot) = bouncingAnimation(r.coord, r.motion, r.bound, r.region, rotation)
      Thread.sleep(20)
      simulateAutoRobot(new Robot(bpos, bmot, r.region, r.width, r.height))
    }


    simulateAutoRobot(robot)
  }
}