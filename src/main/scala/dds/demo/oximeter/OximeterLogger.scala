package dds.demo.oximeter

import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}
import dds.prelude._
import dds._

import dds.demo.Oximetry
import scala.collection.JavaConversions._

object OximeterLogger {

  def main(args: Array[String]) {

    if (args.length < 1) {
      println("USAGE\n\tOximeter <device-id>")
      sys.exit
    }

    val did = args(0)
    val topic = Topic[Oximetry]("Oximetry")

    val sub = Subscriber(SubscriberQos().withPolicy(Partition(did)))

    val dr = DataReader(sub, topic)

    val listener: PartialFunction[Any, Unit] = {
      case DataAvailable(_) => {
        dr.read().foreach(s => {
          val d = s.getData
          println("[" + d.deviceId +", " + d.spO2 + ", " + d.bpm + ", " + d.rr + ", " + d.pleth + "]")
        })
      }
    }

    dr.setListener (listener)

  }

}
