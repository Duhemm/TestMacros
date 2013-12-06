package foo

case class A()
case class B(x: Int, y: String)
case class C(x: List[Int])
case class D(x: A, y: B, z: C)

object Test extends App {
  import Macros._
  import reflect.runtime.universe._
  import reflect.api.Liftable

  val c1 = A()
 /* val c2 = B(1, "Second value")
  val c3 = C(List(1, 2, 3))
  val c4 = D(c1, c2, c3)*/

  val r1 = q"$c1"
  /*val r2 = q"$c2"
  val r3 = q"$c3"
  val r4 = q"$c4"*/

  println(r1)
  /*println(r2)
  println(r3)
  println(r4)*/
}