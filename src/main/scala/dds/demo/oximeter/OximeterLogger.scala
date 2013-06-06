package dds.demo.oximeter

import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}
import dds.prelude._
import dds._

import dds.demo.{Pleth, Oximetry}
import scala.collection.JavaConversions._

object OximeterLogger {

  val oximeterTopic = "Oximetry"
  val plethTopic = "Pleth"

  def main(args: Array[String]) {

    if (args.length < 1) {
      println("USAGE\n\tOximeter <device-id>")
      sys.exit
    }

    val did = args(0)

    val otopic = Topic[Oximetry](oximeterTopic)
    val ptopic = Topic[Pleth](plethTopic)
    val sub = Subscriber(SubscriberQos().withPolicy(Partition(did)))

    val odr = DataReader[Oximetry](sub, otopic)
    val pdr = DataReader[Pleth](sub, ptopic)


    val listener: PartialFunction[Any, Unit] = {
      case DataAvailable(_) => {
        odr.read().foreach(s => {
          val d = s.getData
          println("[" + d.deviceId +", " + d.spO2 + ", " + d.bpm + ", " + d.rr + ", " + d.pleth + "]")
        })
      }
    }

    odr.setListener (listener)

    pdr listen {
      case DataAvailable(_)  => {
        pdr read() foreach (s => {
          val d = s.getData
          println("["+ d.deviceId + ", "+ d.pleth +"]")
        })
      }
    }

  }

}
