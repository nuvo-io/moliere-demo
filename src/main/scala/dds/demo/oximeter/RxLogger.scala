package dds.demo.oximeter


import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}
import dds.prelude._
import dds._

import dds.demo.{Pleth, Oximetry}
import scala.collection.JavaConversions._

import rx.observables._

object RxLogger {

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

    val opdr = DdsObservable.fromDataReaderData(pdr)
    opdr.filter(_.pleth > 95).subscribe(d => println("OK ["+ d.deviceId + ", "+ d.pleth +"]"))


    opdr.filter(_.pleth <= 95).subscribe(d => println("NOTOK ["+ d.deviceId + ", "+ d.pleth +"]"))

    val epdr = DdsObservable.fromDataReaderEvents(pdr)
    epdr.subscribe(e => println(e))
    val h = 10
    val s = 1

    opdr.buffer(h,s).map(b => b.foldLeft(0F)(_ + _.pleth)).map(_/h).subscribe(f => println(s"FoldLeft Average = $f"))

    opdr.buffer(h,s).subscribe(s => {
      val avg = s.seq.foldLeft(0F)(_ + _.pleth)/ h
      println(s"User average pleth = $avg")
    })


  }

}
