package dds.demo.oximeter

import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}

import dds.demo._
import dds._


import dds.demo.common._
object Oximeter {
  val period = 1000 // 1 sec

  def test(x: Int)(implicit y: String, z: Float = 10.0F) {
    println(s"$x ?= $y ?= $z")
  }

  def main(args: Array[String]) {
    if (args.length < 1) {
      println("USAGE\n\tOximeter <device-id>")
      sys.exit
    }
    implicit val y = "10"

    test(10)

    implicit val z = 20.0F
    test(20)
    val did = args(0)
    val topic = Topic[Oximetry]("Oximetry")

    implicit val pub = Publisher(PublisherQos().withPolicy(Partition(did)))
    val w = DataWriter(topic)

    while (true) {
      val oximetry = new Oximetry(did, genValue(), genValue(), genValue(), genValue(), genValue())
      w.write(oximetry)
      print(".")
      Thread.sleep(period)
    }
  }
}
