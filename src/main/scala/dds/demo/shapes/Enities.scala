package dds.demo.shapes

import dds._
import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}
import dds.demo.play.{Shape2D, Line2D}
import org.omg.dds.demo.ShapeType

object Entities {
  def apply(psub: String, ppub:String) = new Entities(psub, ppub)
  def apply(p: String) = new Entities(p, p)
}

class Entities(psub: String, ppub: String) {

  lazy val subQos = SubscriberQos().withPolicy(Partition(psub))
  lazy val pubQos = PublisherQos().withPolicy(Partition(ppub))

  implicit lazy val pub = Publisher(pubQos)
  implicit lazy val sub = Subscriber(subQos)

  lazy val circle = Topic[ShapeType](defaultDomainParticipant, "Circle")
  lazy val square = Topic[ShapeType](defaultDomainParticipant,"Square")
  lazy val triangle = Topic[ShapeType](defaultDomainParticipant,"Triangle")

  lazy val circleDR = DataReader[ShapeType](circle)
  lazy val squareDR = DataReader[ShapeType](square)
  lazy val triangleDR = DataReader[ShapeType](triangle)

  lazy val circleDW = DataWriter[ShapeType](circle)
  lazy val squareDW = DataWriter[ShapeType](square)
  lazy val triangleDw = DataWriter[ShapeType](triangle)

  lazy val line = Topic[Line2D] ("LineShape")
  lazy val lineDW = DataWriter[Line2D](line)
  lazy val ovalFill = Topic[Shape2D] ("OvalFillShape")
  lazy val ofDW = DataWriter[Shape2D](ovalFill)

  lazy val fshape = Topic[Shape2D]("RectFillShape")
  lazy val fshapeDW = DataWriter[Shape2D](fshape)

  lazy val shape = Topic[Shape2D]("RectShape")
  lazy val shapeDW = DataWriter[Shape2D](shape)

  val writerMap = Map(
    "circle" -> circleDW,
    "square" -> squareDW,
    "triangle" -> triangleDw)

  var readerMap = Map (
    "circle" -> circleDR,
    "square" -> squareDR,
    "triangle" -> triangleDR)

}
