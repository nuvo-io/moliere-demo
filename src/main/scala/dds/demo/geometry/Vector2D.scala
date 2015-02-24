package dds.demo.geometry

class Vector2D (val x: Int, val y: Int) {

  def * (gain: Double) = new Vector2D ((x * gain).toInt, (y * gain).toInt)

  def *: (gain: Double) = this * gain

  def + (other: Vector2D) = new Vector2D (x + other.x, y + other.y)

  def - (other: Vector2D) = new Vector2D (x - other.x, y + other.y)

  def opposite() = new Vector2D (-x, -y)

  def rotate(matrix: Array[Array[Int]]) = {
    val a = (x * matrix(0)(0)) + (y * matrix(1)(0))
    val b = (x * matrix(0)(1)) + (y * matrix(1)(1))
    new Vector2D(a, b)
  }

  override def toString = "("+ x  +", "+ y + ")"

}

