case class A(x: Int)
case class B(x: Int, y: String)
case class C(x: Boolean)
case class D(x: A, y: B, z: C)
case class E(x: F)
case class F(x: E)

object Test extends App {

  import Macros._
  import reflect.api.Liftable
  import scala.reflect.runtime.universe._
  import scala.reflect.runtime.currentMirror
  
  val c1 = A(18)
  val c2 = B(1, "Second value")
  val c3 = C(true)
  val c4 = D(c1, c2, c3)
  //val c5 = E(F(E(null)))

  val r1 = q"$c1"
  val r2 = q"$c2"
  val r3 = q"$c3"
  val r4 = q"$c4"
  //val r5 = q"$c5"
  
  println("RAW : " + showRaw(r1))
  println("RAW : " + showRaw(r2))
  println("RAW : " + showRaw(r3))
  println("RAW : " + showRaw(r4))
  //println("RAW : " + showRaw(r5))

  println(r1)
  println(r2)
  println(r3)
  println(r4)
  //println(r5)
  
}