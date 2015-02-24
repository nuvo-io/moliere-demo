package dds.demo.vehicle

import dds._
import dds.prelude._
import dds.config.DefaultEntities._
import dds.demo.geometry.{Vector2D, Region2D}
import dds.demo.play._
import jline.console.ConsoleReader

class Versors(var i: Float, var j: Float, val MA: Float) {

  def accelerate =  (x: Float) => x + Math.abs(x/2) + 1
  def decelerate =  (x: Float) => x - Math.abs(x/2) - 1
  def accelerateX(): Unit = {
    i = Math.min(MA, accelerate(i))
    println(s"accelerateX($i)")
  }
  def accelerateY(): Unit = {
    j = Math.min(MA, accelerate(j))
    println(s"accelerateY($j)")
  }
  def decelerateX(): Unit = {
    i = Math.max(-MA, decelerate(i))
    println(s"decelerateX($i)")
  }
  def decelerateY(): Unit = {
    j = Math.max(-MA, decelerate(j))
    println(s"decelerateY($j)")
  }
}

object VehicleDriver {

  val Exit: Byte = 113
  val Up: Byte = 65
  val Down: Byte = 66
  val Left: Byte = 68
  val Right: Byte = 67
  val Stop: Byte = 32
  val MA = 5

  def main(args: Array[String]): Unit = {
    if (args.length > 4) {
      val vid = args(0).toInt
      val size = args(1).toInt
      val speed = args(2).toInt
      val width = args(3).toInt
      val height = args(4).toInt
      val widthB = width - size
      val heightB = height - size
      val topic = Topic[VehicleStatus]("TVehicleStatus")
      val dw = DataWriter[VehicleStatus](topic)

      val x0 = width / 2
      val y0 = height / 2

      val vs = new VehicleStatus(vid, x0, y0, size, size, speed, speed)
      dw ! vs
      val reader = new ConsoleReader()
      reader.clearScreen()
      println("Use arrows to move the car, [space] to stop it  and [q] to exit.")
      val versors = new Versors(0, 0, MA)

      val runner = new Runnable() {
        def run(): Unit = {
          try {
            while (true) {
              vs.x = (vs.x + versors.i).toInt
              vs.y = (vs.y + versors.j).toInt

              if (vs.x > widthB) {
                vs.x = widthB
                versors.i = -versors.i
              }
              if (vs.x < 0) {
                vs.x = 0
                versors.i = -versors.i
              }

              if (vs.y > heightB) {
                vs.y = heightB
                versors.j = -versors.j
              }
              if (vs.y < 0)  {
                vs.y = 0
                versors.j = -versors.j
              }

              dw ! vs
              Thread.sleep(30)
            }
          } catch {
            case ie: InterruptedException => println("Interrupted")
          }
        }
      }
      val th = new Thread(runner)
      th.start()
      var c = 0
      do {
        c = reader.readCharacter()

        c match {
          case Up => {
            versors.decelerateY()
          }
          case Down => {
            versors.accelerateY()
          }
          case Left => {
            versors.decelerateX()
          }
          case Right => {
            versors.accelerateX()
          }
          case Stop => {
            versors.i = 0
            versors.j = 0
          }
          case _ =>
        }
      }
      while (c != Exit);
      th.interrupt()
      sys.exit(0)
    }
    else
      println("USAGE: VehicleDriver <vid> <v-size> <speed> <width> <height> ")
  }
}
