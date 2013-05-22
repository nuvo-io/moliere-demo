package dds.demo.oximeter

import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}
import dds.prelude._
import dds._

import dds.demo.Pleth
import scala.collection.JavaConversions._

object PlethLogger {
  def main(args: Array[String]) {
    if (args.length < 1) {
      println("USAGE:\t\nPlethLogger <device-id>")
      sys.exit(1)
    }
    val did = args(0)
    val t = Topic[Pleth]("Pleth")
    val sub = Subscriber(SubscriberQos().withPolicy(Partition(did)))
    val r = DataReader(sub, t)

    val listener: PartialFunction[Any, Unit] = {
      case DataAvailable(_) => {
        r.read.foreach(s => {
          val d = s.getData
          println("[" + d.deviceId + ", " + d.pleth +"]")
        })
      }
    }
    r.setListener(listener)
  }
}
