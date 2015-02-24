package dds.demo.geometry

class Region2D(val x0: Int, val y0: Int, val width: Int, val height: Int) {
  def contains (x: Int, y: Int) =
    (((x >= x0 ) && (x <= x0 + width)) && ((y >= y0) && (y <= y0 + height)))

  def contains (other: Region2D) =
  ((other.x0 >= x0) && (other.y0 >= y0) &&
    (other.x0 + other.width <= x0 + width) && (other.y0 + other.height <= y0 + height))

}