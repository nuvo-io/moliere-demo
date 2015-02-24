package dds.demo

import geometry.{Region2D, Vector2D}

package object ddsplay {

  def bouncingAnimation(position: Vector2D, motion: Vector2D, bound: Vector2D => Region2D , region: Region2D, rm: Array[Array[Int]]) : (Vector2D, Vector2D) = {
    val r = position + motion
    val (p,m) = if (region contains (bound(r))) (r, motion)
                else {
                  val m = motion rotate(rm)
                  ((position + m), m)
                }
    if (region contains (bound (p))) (p, m)
    else (position, motion)
  }
}