package dds.demo

import org.omg.dds.demo.ShapeType
import scala.language.implicitConversions

/**
 * Created by kydos on 12/01/15.
 */
package object preamble {
  implicit def shapeType2String(s: ShapeType) = "("+ s.color +", ("+ s.x + ", "+ s.y +"), "+ s.shapesize +")"
}
