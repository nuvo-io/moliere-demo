package dds.demo.ddsplay

import java.awt.event.{WindowEvent, WindowAdapter}
import dds.demo.play._
import dds._
import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}
import dds.prelude._
import javax.swing.{JPanel, JFrame}
import java.awt._
import scala.collection.JavaConversions._

import org.omg.dds.sub.{InstanceState}
import org.omg.dds.sub.Subscriber.DataState
import scala.language.implicitConversions

object DDSPlay {

  def main (args: Array[String]) {
    if (args.length < 2) {
      println ("USAGE:\n\tDDSPlay <widht> <height> [<Partition>]")
      sys.exit (1)
    }
    val width = args(0) toInt
    val height = args(1) toInt
    val partition = if (args.length > 2) args(2) else ""
    val frame = new JFrame ("DDSPlay")
    frame setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE)

    val insets = frame getInsets()
    frame setSize (width + insets.left + insets.right, height + insets.top + insets.bottom)

    println(s"Using partition: $partition ")

    val pubQos = PublisherQos().withPolicy(Partition(partition))
    val subQos = SubscriberQos().withPolicy(Partition(partition))
    implicit val pub = Publisher(pubQos)
    implicit val sub = Subscriber(subQos)

    val ovalShape = Topic[Shape2D] ("OvalShape")
    val ovalFill = Topic[Shape2D] ("OvalFillShape")

    val rectShape = Topic[Shape2D] ("RectShape")
    val rectFill = Topic[Shape2D] ("RectFillShape")

    val line = Topic[Line2D] ("LineShape")


    val rectShapeDR = DataReader[Shape2D] (rectShape)
    val rectFillDR = DataReader[Shape2D] (rectFill)

    val ovalShapeDR = DataReader[Shape2D] (ovalShape)
    val ovalFillDR = DataReader[Shape2D] (ovalFill)

    val lineDR = DataReader[Line2D](line)

    val stroke = new BasicStroke(2)
    val pane = new JPanel(true) {
      override val preferredSize = new Dimension(width, height)

      override def getPreferredSize() = preferredSize
      implicit def toAWTColor(c: ColorRGB): Color = new Color(c.red, c.green, c.blue)

      override def paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g.asInstanceOf[Graphics2D]

        history(ovalShapeDR) foreach  (s => {
          val d = s.getData()
          g2d setColor (d.color)
          g2d drawOval (d.coord.x, d.coord.y, d.width, d.height)
        })



        history(ovalFillDR) foreach  (x => {
          val s = x.getData
          g2d setColor (s.color)
          g2d fillOval (s.coord.x, s.coord.y, s.width, s.height)
        })

        history(rectShapeDR) foreach ( x => {
          val s = x.getData()
          g2d setColor (s.color)
          g2d drawRect (s.coord.x, s.coord.y, s.width, s.height)
        })

        history(rectFillDR) foreach ( x => {
          val s = x.getData()
          g2d setColor (s.color)
          g2d fillRect (s.coord.x, s.coord.y, s.width, s.height)
        })

        (lineDR read) foreach (x =>  {
          val l = x.getData()
          g2d setColor (l.color)
          g2d setStroke (stroke)
          g2d.drawLine(l.begin.x, l.begin.y, l.end.x, l.end.y)
        })
      }
    }
    pane setBackground (Color white)
    frame add (pane)
    frame pack ()
    frame setVisible (true)
    val rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    pane.getGraphics.asInstanceOf[Graphics2D].setRenderingHints(rh);

    while (true) {
      Thread.sleep(20)
      pane repaint()
    }
  }
}