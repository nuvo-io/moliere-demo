package dds.demo.vehicle

import dds.demo.geometry.{Vector2D, Region2D}
import scala.language.implicitConversions

trait VehicleDynamics {

  var position: Vector2D

  var motion: Vector2D

  def nextPosition: Vector2D

}

trait ConstrainedVehicleDynamics extends VehicleDynamics {

  def bounds:     Region2D
  var constraint: Region2D
}

trait BouncingVehicleDynamics extends ConstrainedVehicleDynamics {

  var running: Boolean

  def bounceMatrix: Array[Array[Int]]

  def nextPosition = {
    if (running) {
      var newPos = position + motion
      var newBound = (v: Vector2D) => new Region2D(v.x, v.y, bounds.width, bounds.height)
      if (constraint contains newBound(newPos)) {
        position = newPos
      }
      else {
        motion = motion rotate bounceMatrix
        newPos= position + motion
      }
      if (constraint contains newBound(newPos)) {
        position = newPos
        position
      }
      else {
        position
      }
    }
    else {
      position
    }
  }
}