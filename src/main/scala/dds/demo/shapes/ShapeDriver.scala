package dds.demo.shapes


import dds.demo.geometry.Region2D
import dds.demo.geometry.Vector2D
import dds.prelude._
import dds.demo.geometry.{Vector2D, Region2D}
import dds.demo.play._
import jline.console.ConsoleReader
import org.omg.dds.demo.ShapeType

class Speed(var i: Float, var j: Float, val MA: Float) {
  val delta = 1F

  def accelerate =  (x: Float) => x + delta// x + Math.abs(x/2) + 1
  def decelerate =  (x: Float) => x - delta //x - Math.abs(x/2) - 1
  def accelerateX(): Unit = {
    i = Math.min(MA, accelerate(i))
  }
  def accelerateY(): Unit = {
    j = Math.min(MA, accelerate(j))
  }
  def decelerateX(): Unit = {
    i = Math.max(-MA, decelerate(i))
  }
  def decelerateY(): Unit = {
    j = Math.max(-MA, decelerate(j))
  }
}


object ShapeDriver {

  val Exit: Byte = 113
  val Up: Byte = 65
  val Down: Byte = 66
  val Left: Byte = 68
  val Right: Byte = 67
  val Stop: Byte = 32
  val MA = 5

  val dt = 40 * 10E-3 // 40 msec

  def main(args: Array[String]): Unit = {
    if (args.length > 4) {
      val shape = args(0).toLowerCase()
      val color = args(1).toUpperCase()
      val size = args(2).toInt
      val widthB = args(3).toInt
      val heightB = args(4).toInt
      val partition = if (args.length > 5) args(5) else ""

      val (x0, y0) = (widthB/2, heightB/2)
      val entities = Entities(partition)
      val m = size

      for (dw <- entities.writerMap.get(shape.toLowerCase())) {
        
        val s = new ShapeType(color, x0, y0, size)
        dw ! s
        val reader = new ConsoleReader()
        reader.clearScreen()
        println("Use arrows to move the shape, [space] to stop it  and [q] to exit.")
        val speed = new Speed(0, 0, MA)

        val runner = new Runnable() {
          def run(): Unit = {
            try {
              // not dealing with round-up... that's a simple demo.
              // In any case it would not be very hard to deal with it... perhaps
              // will add it as some point

              while (true) {
                s.x = (s.x + speed.i).toInt
                s.y = (s.y + speed.j).toInt


                if (s.x > widthB) {
                  s.x = widthB
                  speed.i = -speed.i
                }
                if (s.x < 0) {
                  s.x = 0
                  speed.i = -speed.i
                }

                if (s.y > heightB) {
                  s.y = heightB
                  speed.j = -speed.j

                }
                if (s.y < 0)  {
                  s.y = 0
                  speed.j = -speed.j
                }

                dw ! s
                Thread.sleep(40)
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
              speed.decelerateY()
            }
            case Down => {
              speed.accelerateY()
            }
            case Left => {
              speed.decelerateX()
            }
            case Right => {
              speed.accelerateX()
            }
            case Stop => {
              speed.i = 0
              speed.j = 0
            }
            case _ =>
          }
        }
        while (c != Exit);
        th.interrupt()
        sys.exit(0)
      }
      
    }
    else
      println("USAGE: ShapeDriver <shape> <color> <size> <width> <height> [<partition>]")
  }
}
