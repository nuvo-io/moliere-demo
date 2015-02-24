package dds.demo.vehicle

object VehicleControl {
  def main(args: Array[String]) {
    println("You need to have DDS RMI Installed to run these kind of examples")
  }
}

                                           /*
import org.opensplice.DDS_RMI._
import DDS_RMI.org.opensplice.demo.VehicleControlInterfaceProxy
import collection.mutable.HashMap

object VehicleControl {
  def main(args: Array[String]) {

    if (args.length < 1) {
      println("USAGE:\n\tVehicleControl <vid>")
      sys.exit(1)
    }
    val name = "Vehicle-"
    var vid = args(0).toInt

    val runtime = CRuntime.getDefaultRuntime
    runtime start(args)
    val vehicleMap = new HashMap[Int,  VehicleControlInterfaceProxy]
    var v: VehicleControlInterfaceProxy =
      DDS_Service.getServerProxy(name + vid, vid, classOf[VehicleControlInterfaceProxy])
    if (v == null) {
      println("Can't resolve service....")
      sys.exit(1)
    }
    vehicleMap += (vid -> v)

    while (true) {
      println("\n 1.  Start")
      println(" 2.  Stop")
      println(" 3.  Set Motion Vector")
      println(" 4.  Set Region")
      println(" 5.  Set Position")
      println(" 6.  Set Vehicle Id")
      println(" 7. Exit")

      print(">> ")
      var selection = readLong()

      selection match {
        case 1 => {
          println(">> ")
          v.start()
        }
        case 2 => {
          println(">> ")
          v.stop()
        }

        case 3 => {
          print(">> New Motion Vector\ndx = ")
          val dx = readInt()
          print("dy = ")
          val dy = readInt()
          v.setMotion(dx, dy)
        }
        case 4 => {
          print(">> New Region (x, y, width, height) = ")
          val s = readLine split (",")
          v.setRegion(s(0).toInt, s(1).toInt, s(2).toInt, s(3).toInt)
        }
        case 5 => {
          println(">> New Vehicle Position")
          print("x = ")
          val x = readInt();
          print("y = ")
          val y = readInt();
          v.setPosition(x, y)
        }
        case 6 => {
          print("New Vehicle Id, vid = ")
          val nvid = readInt()

          v = vehicleMap get (nvid) match {
            case cv:Some[VehicleControlInterfaceProxy] => {
              println("Vehicle cached")
              cv get
            }
            case _ => {
              val dv: VehicleControlInterfaceProxy =
                DDS_Service.getServerProxy(name + nvid, nvid, classOf[VehicleControlInterfaceProxy])
              if (dv == null) {
                println("Can't find vehicle...")
                v
              }
              else {
                print("Found vehicle with vid ="+ nvid)
                vehicleMap += (nvid ->  dv)
                dv
              }
            }

          }


        }
        case 7 => sys.exit(0)

      }
    }

  }
}
                                           */