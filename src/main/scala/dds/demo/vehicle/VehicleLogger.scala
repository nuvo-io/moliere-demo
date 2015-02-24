package dds.demo.vehicle

import dds.demo.play._
import dds._
import dds.config.DefaultEntities._
import dds.prelude._
import scala.collection.JavaConversions._
import scala.language.implicitConversions

object VehicleLogger {
  def main(args: Array[String]) {
    implicit def vehicleStatus2String(vs: VehicleStatus): String = "["+ vs.vid +", ("+ vs.x +", "+ vs.y +")]"
    if (args.length < 1) {
      println("USAGE:\n\tVehicleLogger <history-depth>")
      sys.exit(1)
    }
    val vehicleStatusTopic = Topic[VehicleStatus]("TVehicleStatus")
    val qos = DataReaderQos().withPolicy(History.KeepLast(args(0).toInt))
    val vsDR= DataReader[VehicleStatus](vehicleStatusTopic, qos)

    println("+----------------------------------------------------+")
    println("+----------------------------------------------------+")
    vsDR.listen {
      case e: DataAvailable[_] => {
        history(vsDR) foreach (vs => println(vehicleStatus2String(vs.getData)))
        println("+----------------------------------------------------+")
      }
    }

  }
}