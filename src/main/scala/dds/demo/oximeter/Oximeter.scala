package dds.demo.oximeter

import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}

import dds.demo._
import dds._


import dds.demo.common._
object Oximeter {

  val oximeterPeriod = 1000 // 1 sec
  val oximeterTopic = "Oximetry"
  val plethTopic = "Pleth"

  val avg = 96
  val ampl = 8
  val freq = 4
  val noiseAmpl = 20
  val noiseFreq = 10F


  def main(args: Array[String]) {

    if (args.length < 2) {
      println("USAGE\n\tOximeter <device-id> <pleth-period>")
      sys.exit
    }

    val did = args(0)
    val pp = args(1).toInt

    assert (pp < oximeterPeriod)

    val description = "Pulse Oximeter"


    val discovery = Topic[DeviceInfo]("DeviceInfo")
    val dwQos = DataWriterQos()(dds.config.DefaultEntities.defaultPub).withPolicies(Durability.TransientLocal, Reliability.Reliable)
    val ddw = DataWriter(dds.config.DefaultEntities.defaultPub, discovery, dwQos)
    val dinfo = new DeviceInfo(did, description)
    ddw.write(dinfo)

    val ot = Topic[Oximetry](oximeterTopic)
    val pt = Topic[Pleth](plethTopic)

    implicit val pub = Publisher(PublisherQos().withPolicy(Partition(did)))
    val ow = DataWriter(ot)
    val pw = DataWriter(pub, pt)

    val modulo = oximeterPeriod / pp
    var count = 0
    var index = 0

    while (true) {
      val p = genPleth(avg, pp, index).toFloat
      val pleth = new Pleth(did, p)
      pw write (pleth)

      if (count >= modulo) {
        val oximetry = new Oximetry(did, genValue(), genValue(), genValue(), genValue(), genValue())
        ow write (oximetry)
        count = 0
      } else count = count + 1
      index += 1

      print(".")
      Thread.sleep(pp)
    }
  }
}
