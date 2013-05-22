package dds.demo.oximeter

import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}
import dds.demo.Pleth
import dds._
import dds.demo.common._

object PlethGenerator {

  val avg = 96
  val ampl = 8
  val freq = 4
  val noiseAmpl = 20
  val noiseFreq = 10F

  // genTrend(avg: Int, ampl: Int, modeOneFreq: Float, noiseAmpl: Int, noiseFreq: Float, step: Float)
  def main(args: Array[String]) {
    if (args.length < 2) {
      println("USAGE:\t\nPlethGenerator <device-id> <period>")
      sys.exit(1)
    }
    val did = args(0)
    val period = args(1).toInt
    println(s"period: $period")

    val t = Topic[Pleth]("Pleth")
    val pub = Publisher(PublisherQos().withPolicy(Partition(did)))
    val w = DataWriter(pub, t)

    var index = 0
    val start = System.nanoTime()
    while (true) {
      // val pleth = new Pleth(did, genTrend(avg, ampl, freq, noiseAmpl, noiseFreq, index).toInt)
      val p = genPleth(avg, period, index).toFloat
      val m = index % 77
      val t = (System.nanoTime() - start) / 1000000000D
      println(s"($m, $p) @ $t ")
      val pleth = new Pleth(did, p)
      w write(pleth)
      index += 1
      Thread.sleep(period)
    }
  }
}
