package dds.demo.shapes

import dds._
import dds.prelude._
import dds.config.DefaultEntities._
import rx.observables._
import rx.lang.scala._
import org.omg.dds.demo.ShapeType
import scala.concurrent.duration._

object ShapeAnalytics {
  val circle = "Circle"
  val square = "Square"
  val triangle = "Triangle"

  def main(args: Array[String]) {

    val circleT = Topic[ShapeType](circle)
    val cdw = DataWriter[ShapeType](circleT)
    val cobs = DdsObservable.fromDataReaderData {
      DataReader[ShapeType](circleT)
    }

    val squareT = Topic[ShapeType](square)
    val sobs = DdsObservable.fromDataReaderData {
      DataReader[ShapeType](squareT)
    }
    val sdw = DataWriter[ShapeType](squareT)

    val ttopic = Topic[ShapeType](triangle)
    val tobs = DdsObservable.fromDataReaderData {
      DataReader[ShapeType](ttopic)
    }

    val tdw = DataWriter[ShapeType](ttopic)

    val gcircles: Observable[(String, Observable[ShapeType])] = cobs.groupBy(s => s.color)



    // Compute the average between circle and square of matching color with flatMap
    // val cm = cobs.flatMap(c => sobs.dropWhile(_.color != c.color).take(1).map {
    //      s => new ShapeType(s.color, (s.x + c.x)/2, (s.y + c.y)/2, (s.shapesize + c.shapesize)/4)
    //  })


    // Compute the average between circle and square of matching color with for comprehension
  /*
    val cm = for {
      c <- cobs;
      s <- sobs.dropWhile(_.color != c.color).take(1)
    } yield new ShapeType(s.color, (s.x + c.x)/2, (s.y + c.y)/2, (s.shapesize + c.shapesize)/4)

    cm.subscribe(tdw.write(_))
    */

/*
    val ca = cobs.flatMap(c0 => {
      cobs.takeWhile(_.color != c0.color).foldLeft(c0)((a, s) => (new ShapeType("CYAN", (a.x + s.x)/2, (a.y + s.y)/2, (a.shapesize + s.shapesize)/2)))
    })

    ca.subscribe(tdw.write(_))
    */
    val h = 100
    val s = 1

    val avgShapePos = cobs.groupBy(_.color).map(_._2).flatMap(s => s.buffer(h, 1)).map(bs => {
      val n = bs.length
      val as = bs.tail.foldRight(bs.head)((a, s) => new ShapeType(a.color, a.x + s.x, a.y + s.y, a.shapesize + s.shapesize))
      new ShapeType(as.color, as.x/n, as.y/n, as.shapesize/n)
    })
    avgShapePos.subscribe(tdw.write(_))
    // cobs.takeWhile()
    // cobs.subscribe(c => {sobs.dropWhile(_.color != c.color).take(1).subscribe(s => tdw.write(new ShapeType(s.color, (s.x + c.x)/2, (s.y + c.y)/2, (s.shapesize + c.shapesize)/4)))})

//
//    val ooc: Observable[Observable[ShapeType]] = gcircles.map(_._2)
//    val shapeNumber = ooc.length.subscribe(n => {
//      println("--------------")
//      ooc.take(n).flatten.subscribe(s => println("(" + s.color + ", " + s.x + ", " + s.y) )
//    })

    /*
    val xs: Observable[Int] = Observable.from(List(1,2,3,4,5,6,7,8,9,10))
    val yss: Observable[Observable[Int]] =
      xs.map(x => Observable.interval(x seconds).map(_ => x).take(2))

    val xz: Observable[Int] = yss.flatten
    xz.subscribe(x => println(x))

    val ps: Observable[(Int, Observable[Int])] = xs.groupBy(_ % 2)
    ps.subscribe(x => x._2.subscribe(y => println("Stream: " + x._1 + " value = " + y)))
*/


    //ps.subscribe(ys => for (y <- ys._2) y)
    /*
    gcircles.subscribe(o => o._2.subscribe(s => println(o._1 + "(" + s.x + ", " + s.y + ")")))
    gcircles.map(s => s._2).buffer(1,1).subscribe(s => {

      def merge: Seq[Observable[ShapeType]] => Observable[ShapeType] = (os: Seq[Observable[ShapeType]]) => {
        def accMerge(as: Observable[ShapeType], os: Seq[Observable[ShapeType]]): Observable[ShapeType] = {
        os match {
          case x::List() => as.merge(x)
          case x::xs => accMerge(as.merge(x), xs)
        }
      }
      accMerge(os.head, os.tail)
      }

      val ms = merge(s)
      ms.subscribe(s => println(s.color + " - " + s.x + " - " + s.y))
//      // val fs: Observable[ShapeType] = s
//      val fs: Seq[ShapeType] = s.flatten[ShapeType]
//      // val len = fs.length
//      val acc = fs.foldLeft((0, 0))((a, s) => (a._1 + s.x, a._2 + s.y))
//      // val (ax, ay) = (acc._1/len, acc._2/len)
//      val (ax, ay) = (acc._1, acc._2)
//      println(s"Average Shape Position = ($ax, $ay)")
    })
    // gcircles.subscribe(s => s._2.sub)
    // gcircles.buffer(1,1).flatMap(f => f.foldLeft(f.head._2)((a,b) => a.merge(b._2))).foldLeft((0,0))((a, s) => (a._1 + s.x, a._2 + s.y)).subscribe(s => println(s))
    */
  }
}
