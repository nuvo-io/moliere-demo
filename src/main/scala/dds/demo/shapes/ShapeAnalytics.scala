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


    val h = 100
    val s = 1

    val avgShapePos = cobs.groupBy(_.color).map(_._2).flatMap(s => s.buffer(h, 1)).map(bs => {
      val n = bs.length
      val as = bs.tail.foldRight(bs.head)((a, s) => new ShapeType(a.color, a.x + s.x, a.y + s.y, a.shapesize + s.shapesize))
      new ShapeType(as.color, as.x/n, as.y/n, as.shapesize/n)
    })
    avgShapePos.subscribe(tdw.write(_))
  
  }
}
