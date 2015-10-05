package dds.demo.cep

import com.espertech.esper.client._

import dds.config.DefaultEntities._
import dds._
import dds.prelude._
import org.omg.dds.demo.ShapeType
import scala.collection.JavaConversions._

// As a sample rule you can try:
//
//  "select dds.demo.cep.ShapeFactory.createShape(color, cast(avg(x),int), cast(avg(y),int), shapesize)
//    as NewShape  from ShapeType.win:time(1 sec)"
//
object ShapeFactory {
  def createShape(color: String, x: Int, y: Int, shapesize: Int) =
    new ShapeType(color, x, y,shapesize)
}

object EsperShapes {
  def createShape (color: String, x: Int, y: Int, size: Int) = new ShapeType (color, x, y, size)
  def main(args: Array[String]): Unit = {
    if (args.length > 2) {
      val inTopic = args(0)
      val outTopic = args(1)
      val eplExpr = args(2)

      println("EPL Expression: " + eplExpr)

      // -- Esper Configuration
      val config = new Configuration()
      val ddsConf = new ConfigurationEventTypeLegacy()
      ddsConf.setAccessorStyle(ConfigurationEventTypeLegacy.AccessorStyle.PUBLIC)

      config.addImport("org.omg.dds.demo.*")
      config.addEventType("ShapeType", classOf[org.omg.dds.demo.ShapeType].getName, ddsConf)
      val cep: EPServiceProvider = EPServiceProviderManager.getDefaultProvider(config)

      // -- Statement Registration
      val statement = cep.getEPAdministrator.createEPL(eplExpr)

      val inT  = Topic[ShapeType](inTopic)
      val outT = Topic[ShapeType](outTopic)
      val dr = DataReader[ShapeType](inT)
      val dw = DataWriter[ShapeType](outT)

      val listener = new UpdateListener {
        def update(ne: Array[EventBean], oe: Array[EventBean]) {
          if (ne.length > 0) {
            ne(0).get("NewShape") match {
              case s: ShapeType => {
                s.color = "GRAY"
                dw.write(s)
                print("!")
              }
              case _ => println(">> Mismatching Event Type")
            }
          }
        }
      }
      statement.addListener(listener)
      val runtime = cep.getEPRuntime

      dr.listen {
        case DataAvailable (_) => {
          dr.read().foreach(s => runtime.sendEvent(s.getData()))
        }
      }

    } else
      println("Usage:\n\tEsperShapes <inTopic> <outTopic> <epl-expression>")

  }
}
