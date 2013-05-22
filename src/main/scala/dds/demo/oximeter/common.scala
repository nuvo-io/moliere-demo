package dds.demo

/**
 * Created with IntelliJ IDEA.
 * User: angelo
 * Date: 5/16/13
 * Time: 11:37 PM
 * To change this template use File | Settings | File Templates.
 */
package object common {
  def genValue() = {
    ((0.5 + math.random/2) * 100).toInt
  }
  //97 + sin(2*pi*x/76) + abs(sin(2*pi*x/76)/2) - exp(sin(0.75*pi*x/76)))
  def genPleth(avg: Int, period: Int, x: Int): Double = {
    val f = 1F/period
    val omega = 2*math.Pi/77
    val sino = math.sin(omega*x)
    val sinlo = math.sin(2*omega*x)
    val o2 = 0.75*math.Pi/77

    avg + + (avg/50)*sinlo + sino + math.abs(sino/2) - math.exp(math.sin(o2*x))
  }

  def genTrend(avg: Int, ampl: Int, modeOneFreq: Float, noiseAmpl: Int, noiseFreq: Float, step: Float) = {
    // val t = (avg + ampl*math.sin(step*1.0/3) + (ampl/10)*math.random)
    // ((avg + ampl) + (ampl*math.random * (if (math.random < 0.5) 1 else -1))).toInt
    avg +  ampl * (math.sin(step) * math.cos(modeOneFreq*step)) + noiseAmpl*math.sin(noiseFreq * step)

  }

}
