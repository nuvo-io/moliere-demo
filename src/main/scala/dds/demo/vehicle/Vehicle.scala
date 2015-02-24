package dds.demo.vehicle

import dds.demo.geometry.{Vector2D, Region2D}
import dds.demo.play._
import dds._
import dds.config.DefaultEntities._
import dds.prelude._

import scala.language.implicitConversions

/*
import org.opensplice.DDS_RMI._
import DDS_RMI.org.opensplice.demo.VehicleControlInterface
 */
class Vehicle(val vid: Int,
              var position: Vector2D,
              var bounds: Region2D,
              var constraint: Region2D,
              var motion: Vector2D) extends /* VehicleControlInterface with */ BouncingVehicleDynamics with ControllableVehicle  {

  var running = true
  def bounceMatrix =
    if (math.random > 0.5) Array(Array(-1, 0),Array(0,-1)) else Array(Array(-1, 0),Array(0,1))

  override def toString = "["+ vid + ", ("+ position.x + ", "+ position.y +")]"
}

object Vehicle {
  implicit def toVehicleStatus(v: Vehicle):VehicleStatus =
    new VehicleStatus(
      v.vid,
      v.position.x, v.position.y,
      v.bounds.width, v.bounds.height,
      v.motion.x, v.motion.y)

  def main(args: Array[String]) {
    if (args.length < 8) {
      println("USAGE:\n\tVehicle <vid> <size> <constraints = x,y,width,height> <motion = dx, dy>")
      sys.exit(0)
    }
  /*
   val runtime = CRuntime.getDefaultRuntime
   runtime start(args)
   */
    val vehicleStatusTopic = Topic[VehicleStatus]("TVehicleStatus")
    val vsDW = DataWriter[VehicleStatus](vehicleStatusTopic)

    val vehicleRegionTopic = Topic[VehicleRegion]("TVehicleRegion")
    val vrDW = DataWriter[VehicleRegion](vehicleRegionTopic)

    val vid = args(0).toInt
    val w = args(1).toInt
    val bounds = new Region2D(0, 0, w, w)
    val constraints =
      new Region2D(args(2).toInt, args(3).toInt, args(4).toInt, args(5).toInt)
    val motion = new Vector2D(args(6).toInt, args(7).toInt)

    val position =
      new Vector2D(
        constraints.x0 + (constraints.width/2).toInt,
        constraints.y0 + (constraints.height/2).toInt)

    val vehicle = new Vehicle(vid, position, bounds, constraints, motion)
    val name = "Vehicle-"
    /*
      DDS_Service.register_interface(vehicle, name + vid, vid, classOf[VehicleControlInterface])
      println("Registered Vehicle = "+ name + vid)
    */

    while (true) {
      val vregion = new VehicleRegion(vehicle.vid, vehicle.constraint.x0, vehicle.constraint.y0,
        vehicle.constraint.width, vehicle.constraint.height)
      vsDW ! vehicle
      vrDW ! vregion
      vehicle nextPosition

      Thread.sleep(20);
    }
  }
}