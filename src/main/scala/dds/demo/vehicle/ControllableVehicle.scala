package dds.demo.vehicle


import dds.demo.geometry.{Region2D, Vector2D}

trait ControllableVehicle {

  var position: Vector2D
  var running: Boolean
  var constraint: Region2D
  var motion: Vector2D

  def start = {
    println(">> Stop()")
    running = true
  }

  def stop  =  {
    println(">> Stop()")
    running = false
  }

  def setPosition(x: Int,  y: Int) = position = new Vector2D(x, y)

  def setRegion(x: Int,  y: Int, width: Int,  height: Int) =
    constraint = new Region2D(x, y, width,  height)

  def setMotion(dx: Int,  dy: Int) = {
    println(">> setMotion()")
    motion = new Vector2D(dx, dy)
  }

  def resume {}

  def suspend { }
}