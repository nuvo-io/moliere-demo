package dds.demo.shapes

import dds._
import dds.prelude._
import dds.demo.play._
import org.omg.dds.demo.ShapeType
import scala.collection.JavaConversions._
/**
 * This application produces an heat map showing the region of the canvas that are most often
 * covered by a shape
 */
object HeatMap {

  val HEAT_MAX = 99
  val HEAT_MIN = 0

  val LEVELS = 4
  val STEP = (HEAT_MAX + 1) / LEVELS
  val colors =
    Array(
      new ColorRGB(0xff, 0xff, 0xff), // White
      new ColorRGB(0xf7, 0xa5, 0x20), // Yellow
      new ColorRGB(0xd8, 0x65, 0x1d), // Orange
      new ColorRGB(0xa7, 0x2a, 0x48)) // Red



  def heat2Color(h: Int): ColorRGB = {
    val idx = (h / STEP)
    colors(idx)
  }

  def coolDown(hmap: Array[Array[Int]]): Array[Array[Int]] = {
    hmap.foreach(xs => {
      (0 to xs.length-1).foreach(i => {
        xs(i) = Math.max(HEAT_MIN, xs(i) - 1)
      })
    })
    hmap
  }

  def heatUp(i: Int, j: Int, hmap: Array[Array[Int]]): Array[Array[Int]] = {
    val rows = hmap.length
    val cols = hmap(0).length
    if ((i < rows) && (j < cols))
      hmap(i)(j) = Math.min(HEAT_MAX, hmap(i)(j) + 1)
    else {
      println(s"Out of bounds ($rows, $cols): (i,j) = ($i, $j)")
    }

    hmap
  }

  def drawHeapMap(dw: org.omg.dds.pub.DataWriter[Shape2D], hmap: Array[Array[Int]], hu: Int,  vu: Int,
                  cmap: Array[Array[Int]]): Unit = {
    // This assumes the hmap is a proper matrix
    val rows = hmap.length
    val cols = hmap(0).length
    for (i <- 0 to rows-1 ; j <-  0 to cols-1) yield {
      val x = j * hu
      val y = i * vu
      val clevel = (hmap(i)(j)/ STEP)
      val olevel = cmap(i)(j)
      if (clevel != olevel) {
        cmap(i)(j) = clevel
        val c = heat2Color(hmap(i)(j))
        val cell = new Shape2D(i, j, hu, vu, new Coord2D(x, y), c)
        print('-')
        dw ! cell
      }
    }
  }
  def main(args: Array[String]): Unit = {
    if (args.length > 4) {
      val rows = args(0).toInt
      val cols = args(1).toInt
      val width = args(2).toInt
      val height = args(3).toInt
      val COOLING_PERIOD =  args(4).toInt

      val psub= if (args.length > 5) args(5) else ""
      val ppub = if (args.length > 6) args(6) else psub

      println(s"In partition: $psub")
      println(s"Out partition: $ppub")
      val entities = Entities(psub, ppub)


      val hu = width / cols
      val vu = height / rows

      val mcols = width / hu
      val mrows = height / vu

      val hmap = new Array[Array[Int]](mrows + 1)
      val cmap = new Array[Array[Int]](mrows + 1)

      (0 to mrows).foreach(i => {
        hmap(i) = new Array[Int](mcols + 1)
        cmap(i) = new Array[Int](mcols + 1)
      })


      val fshapeDW = entities.fshapeDW
      val shapeDW = entities.shapeDW


      val circleDR = entities.circleDR
      val squareDR = entities.squareDR
      val triangleDR = entities.triangleDR

      shapeDW ! new Shape2D(width, height, hu*mcols, vu*mrows, new Coord2D(0,0), new ColorRGB(0x66,0x66, 0x66))

      val handleShape = (s: ShapeType) => {
        val x0 = s.x
        val y0 = s.y
        val x1 = x0 + s.shapesize
        val y1 = y0 + s.shapesize
        val j0 = x0 / hu
        val i0 = y0 / vu
        val (i1, j1) = (y1/vu, x1/hu)
        for (i <- i0 to i1; j <- j0 to j1)
          heatUp(i, j, hmap)
      }

      circleDR.listen {
        case DataAvailable(_) => {
          circleDR.read().foreach(s => handleShape(s.getData()))
          print('c')
        }
      }
      squareDR.listen {
        case DataAvailable(_) => {
          squareDR.read().foreach(s => handleShape(s.getData()))
          print('s')
        }
      }


      triangleDR.listen{
        case DataAvailable(_) => {
          squareDR.read().foreach(s => handleShape(s.getData()))
          print('t')
        }
      }

      val cooler = new Runnable() {
        def run(): Unit = {
          while(true) {
            coolDown(hmap)
            Thread.sleep(COOLING_PERIOD)
          }
        }
      }
      val tcooler = new Thread(cooler)
      tcooler.start()

      val drawer= new Runnable() {
        def run(): Unit = {
          while (true) {
            drawHeapMap(fshapeDW, hmap, hu, vu, cmap)
            Thread.sleep(250)
          }
        }
      }
      val tdrawer = new Thread(drawer)
      tdrawer.start()
    }
    else
      println("USAGE:\n\tHeapMap <rows> <cols>  <width> <height> [<in-partition> <out-partition>]")
  }
}
